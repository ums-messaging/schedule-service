package com.ums.schedule.application.target.reader;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.application.ChannelFactory;
import com.ums.schedule.application.target.upload.TargetDbUploadService;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.application.target.dto.SendTargetRowDto;
import com.ums.schedule.domain.target.event.TargetUploadCreatedEvent;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUpload;

import java.util.*;

public class SendTargetReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final int BATCH_SIZE = 1000;
    private final List<SendTargetRowDto> targetList = new ArrayList<>();
    private final TargetDbUploadService uploadService;
    private final ChannelFactory factory;
    private final TargetUploadCreatedEvent event;

    private Map<Integer, String> headMap;

    public SendTargetReaderListener(ChannelFactory factory, TargetUploadCreatedEvent event, TargetDbUploadService uploadService) {
        this.factory = factory;
        this.event = event;
        this.uploadService = uploadService;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        SendTargetRowDto row = SendTargetRowDto.of(context.readRowHolder().getRowIndex()+1, headMap, targetData);
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
        List<SendTargetDto> dtos = targetList.stream()
                .map(row -> SendTargetDto.of(row))
                .toList();

        List<SendTarget> targetList = factory.makeMessage(event.uploadId(), event.template(), dtos);
        uploadService.create(event.uploadId(), targetList);

        this.targetList.clear();

    }

    public int getTotalCount() {
        return 0;
    }
}