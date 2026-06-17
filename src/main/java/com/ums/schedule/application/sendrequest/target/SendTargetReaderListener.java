package com.ums.schedule.application.sendrequest.target;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.application.sendrequest.target.assembler.SendTargetAssembler;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.row.SendTargetRow;
import com.ums.schedule.domain.sendrequest.target.event.TargetUploadCreatedEvent;

import java.util.*;

public class SendTargetReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final int BATCH_SIZE = 1000;
    private final List<SendTargetRow> targetList = new ArrayList<>();
    private final SendTargetAssembler factory;
    private final TargetUploadCreatedEvent event;

    private Map<Integer, String> headMap;

    public SendTargetReaderListener(SendTargetAssembler factory, TargetUploadCreatedEvent event) {
        this.factory = factory;
        this.event = event;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        SendTargetRow row = SendTargetRow.of(context.readRowHolder().getRowIndex()+1, headMap, targetData);
        targetList.add(row);
        if (targetList.size() >= BATCH_SIZE) {
            uploadTargets();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 나머지 큐로 빼기
        if(!targetList.isEmpty()) {
            uploadTargets();
        }
    }

    private void uploadTargets() {
        List<TargetMessageData> dtos = targetList.stream()
                .map(SendTargetRow::toTargetData)
                .toList();

        this.targetList.clear();

    }

    public int getTotalCount() {
        return 0;
    }
}