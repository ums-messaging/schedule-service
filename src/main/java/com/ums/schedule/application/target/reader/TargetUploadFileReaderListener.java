package com.ums.schedule.application.target.reader;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.application.target.reader.model.SendTargetRow;
import com.ums.schedule.application.target.reader.model.TargetRowResult;

import java.util.*;
import java.util.function.Consumer;

public class TargetUploadFileReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final List<TargetRowResult> targetList = new ArrayList<>();
    private final Consumer<List<TargetRowResult>> consumer;
    private final int batchSize;

    private Map<Integer, String> headMap;

    public TargetUploadFileReaderListener(Consumer<List<TargetRowResult>> consumer, Integer batchSize) {
        this.consumer = consumer;
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
                consumer.accept(List.copyOf(targetList));
                targetList.clear();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        int totalCount = context.readRowHolder().getRowIndex() + 1;

        if(!targetList.isEmpty()) {
            consumer.accept(targetList);
        }
    }
}