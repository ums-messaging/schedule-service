package com.ums.schedule.application.target.reader;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.application.target.reader.model.SendTargetRow;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedListContext;
import com.ums.schedule.domain.target.SendTarget;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class TargetUploadFileReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final List<TargetRowResult> targetList = new ArrayList<>();
    private final Consumer<List<SendTargetGroupedListContext>> consumer;
    private final Integer partitionSize;
    private final int batchSize;
    private Map<Integer, String> headMap;
    private List<CompletableFuture<Void>> futures = new ArrayList<>();

    public TargetUploadFileReaderListener(Consumer<List<SendTargetGroupedListContext>> consumer,
                                          Integer partitionSize,
                                          Integer batchSize) {
        this.consumer = consumer;
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
            targetList.add(TargetRowResult.of(row));
        } catch (Exception e) {
            targetList.add(TargetRowResult.of(rowNo, e.getMessage()));
        } finally {
            if (targetList.size() == batchSize) {
                    List<SendTargetGroupedListContext> list = toContext(List.copyOf(targetList));
                    consumer.accept(list);
                    targetList.clear();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if(!targetList.isEmpty()) {
                List<SendTargetGroupedListContext> list = toContext(List.copyOf(targetList));
                consumer.accept(list);
        }
    }

    private List<SendTargetGroupedListContext> toContext(List<TargetRowResult> targetList) {
        return groupedSendTargetList(targetList, partitionSize).entrySet()
                .stream()
                .map(v -> SendTargetGroupedListContext.of(v.getKey(), v.getValue()))
                .toList();
    }

    private Map<Integer, List<TargetRowResult>> groupedSendTargetList(List<TargetRowResult> targetList, int partitionSize) {
        return targetList.stream()
                    .collect(Collectors.groupingBy(i ->
                        (targetList.indexOf(i) / partitionSize)));
    }
}