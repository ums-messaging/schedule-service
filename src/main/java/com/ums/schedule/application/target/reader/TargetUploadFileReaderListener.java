package com.ums.schedule.application.target.reader;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.application.target.reader.model.SendTargetRow;
import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class TargetUploadFileReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final Map<String, List<TargetUploadRowResult>> groupMap = new HashMap<>();
    private final List<TargetUploadRowResult> targetList = new ArrayList<>();

    private final Consumer<List<SendTargetGroupedList>> consumer;
    private final String customerId;
    private final Integer partitionSize;
    private final int batchSize;
    private Map<Integer, String> headMap;

    public TargetUploadFileReaderListener(
            Consumer<List<SendTargetGroupedList>> consumer,
                                          String customerId,
                                          Integer partitionSize,
                                          Integer batchSize) {
        this.consumer = consumer;
        this.customerId = customerId;
        this.partitionSize = partitionSize;
        this.batchSize = batchSize;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        int rowNo = context.readRowHolder().getRowIndex() + 1;

        try {
            SendTargetRow row = SendTargetRow.of(rowNo, headMap, targetData);
            TargetUploadRowResult rowResult = extractDomain(customerId, row);
            List<TargetUploadRowResult> rowList = groupMap.getOrDefault(rowResult.groupKey(), new ArrayList<>());
            rowList.add(rowResult);

            if(rowList.size() == batchSize) {
                List<SendTargetGroupedList> list = toContext(List.copyOf(rowList));
                consumer.accept(list);
                rowList.clear();
            }
            groupMap.put(rowResult.groupKey(), rowList);
        } catch (Exception e) {
            TargetUploadRowResult result = TargetUploadRowResult.of(rowNo, SendTargetResultCode.TARGET_ROW_READ_FAIL);
            targetList.add(result);
        } finally {
            if (targetList.size() == batchSize) {
                List<SendTargetGroupedList> list = toContext(List.copyOf(targetList));
                consumer.accept(list);
                targetList.clear();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        groupMap.entrySet()
                .forEach(entry -> {
                    if(!entry.getValue().isEmpty()) {
                        List<SendTargetGroupedList> list = toContext(List.copyOf(entry.getValue()));
                        consumer.accept(list);
                    }
                });
        groupMap.clear();
    }

    private List<SendTargetGroupedList> toContext(List<TargetUploadRowResult> targetList) {
        return groupedSendTargetList(targetList, partitionSize).entrySet()
                .stream()
                .map(v -> SendTargetGroupedList.of(v.getKey(), v.getValue()))
                .toList();
    }

    private Map<Integer, List<TargetUploadRowResult>> groupedSendTargetList(List<TargetUploadRowResult> targetList, int partitionSize) {
        return IntStream.range(0, targetList.size())
                .boxed()
                .collect(Collectors.groupingBy(
                        idx -> idx / partitionSize,
                        Collectors.mapping(targetList::get, Collectors.toList())
                ));
    }

    private TargetUploadRowResult extractDomain(String customerId, SendTargetRow row) {
        Map<SendTargetColumn, String> targetData = row.toTargetData(customerId).targetData();
        String email = targetData.get(SendTargetColumn.TARGET_EMAIL);
        String[] splitEmail = email.split("@");

        if(splitEmail.length < 2) {
            return TargetUploadRowResult.of(row.rowNo(), SendTargetResultCode.INVALID_EMAIL, email);
        }
        return TargetUploadRowResult.of(row, customerId, splitEmail[1]);
    }
}