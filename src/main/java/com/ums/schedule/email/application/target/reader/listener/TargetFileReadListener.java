package com.ums.schedule.email.application.target.reader.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import com.ums.schedule.email.application.target.reader.model.SendTargetReadContext;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetReadRow;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.group.DomainGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class TargetFileReadListener extends AnalysisEventListener<Map<Long, String>> {
    private final Map<DomainGroup, List<SendTargetRequest>> groupMap = new HashMap<>();
    private final List<SendTargetError> failedTargetList = new ArrayList<>();
    private final SendTargetReadContext context;

    private Map<Integer, String> headMap;

    public TargetFileReadListener(SendTargetReadContext context) {
        this.context = context;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }
    
    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        int rowNo = context.readRowHolder().getRowIndex() + 1;
        try {
            SendTargetReadRow row = SendTargetReadRow.of(rowNo, headMap, targetData);
            DomainGroup group = findDomainGroup(row);
            List<SendTargetRequest> targets = groupMap.getOrDefault(group, new ArrayList<>());
            SendTargetRequest target = SendTargetRequest.of(group, row);
            groupingTargetsAndExecuteWorker(group, target, targets);
        } catch (Exception e) {
            SendTargetReadRow row = SendTargetReadRow.of(rowNo, headMap, targetData);
            processSendTargetError(rowNo, row.getMessageVariable(), e.getMessage());
            this.context.incrementFailCount();
        }
    }

    private DomainGroup findDomainGroup(SendTargetReadRow row) {
        String groupKey = this.context.getGroupKey(row);
        DomainGroup group = findDomainGroup(groupKey);
        return group;
    }

    private DomainGroup findDomainGroup(String groupKey) {
        Set<DomainGroup> groups = groupMap.keySet();
        return groups.stream()
                .filter(v -> v.groupKey().equals(groupKey))
                .findFirst()
                .orElseGet(() -> DomainGroup.of(groupKey));
    }
    private void processSendTargetError(int rowNo, Map<String, Object> targetData, String errorMessage) {
        Integer batchSize = context.getBatchSize();
        List<SendTargetError> failureList = getFailureSendTargetList(rowNo, targetData, errorMessage);
        if(batchSize == failureList.size()) {
            failureList.clear();
        }
    }

    private List<SendTargetError> getFailureSendTargetList(int rowNo, Map<String, Object> targetData, String errorMessage) {
        SendTargetRequest target = SendTargetRequest.failureOf(context.getRequestId(), rowNo, targetData);
        SendTargetError errorTarget = SendTargetError.of(target, SendTargetErrorTypeCode.VALIDATION, errorMessage);
        failedTargetList.add(errorTarget);
        return failedTargetList;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Set<DomainGroup> keySet = groupMap.keySet();
        for (DomainGroup group : keySet) {
            try {
                executeWorker(group, List.copyOf(groupMap.get(group)));
            } catch (Exception e) {
                List<SendTargetRequest> targets = groupMap.get(group);
                for (SendTargetRequest target : targets) {
                    processSendTargetError(target.getRow(), target.getMessageVariable(), e.getMessage());
                }
                this.context.incrementFailCount(targets.size());
            }
        }
        finalizeProcessing(context.readRowHolder().getRowIndex());
    }

    private void finalizeProcessing(int lastRow) {
        this.context.setGroupCount(groupMap.size());
        this.context.setTotalCount(lastRow);
        reset();
    }
    private void reset() {
        groupMap.clear();
    }

    private void groupingTargetsAndExecuteWorker(DomainGroup group, SendTargetRequest target, List<SendTargetRequest> targets) {
        Integer batchSize = context.getBatchSize();
        if(targets.size() == batchSize) {
            executeWorker(group, targets);
            groupMap.remove(group);
            this.context.incrementGroupCount();
        }
        targets.add(target);
        groupMap.put(group, targets);
    }

    private void executeWorker(DomainGroup group, List<SendTargetRequest> targets) {
        Consumer<List<SendTargetGroupedList>> worker = context.getWorker();
        List<SendTargetGroupedList> list = createGroupList(group, List.copyOf(targets));
        worker.accept(list);
        this.context.incrementSuccessCount(targets.size());
    }
    private List<SendTargetGroupedList> createGroupList(DomainGroup group, List<SendTargetRequest> targetList) {
        return groupedSendTargetList(targetList, context.getPartitionSize())
                .entrySet()
                .stream()
                .map(v -> SendTargetGroupedList.of(v.getKey(), group, v.getValue()))
                .toList();
    }
    private Map<Integer, List<SendTargetRequest>> groupedSendTargetList(List<SendTargetRequest> targetList, int partitionSize) {
        return IntStream.range(0, targetList.size())
                .boxed()
                .collect(Collectors.groupingBy(
                        idx -> idx / partitionSize,
                        Collectors.mapping(targetList::get, Collectors.toList())
                ));
    }
}
