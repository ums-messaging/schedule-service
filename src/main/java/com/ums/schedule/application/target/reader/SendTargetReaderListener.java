package com.ums.schedule.application.target.reader;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ums.schedule.application.target.upload.TargetDbUploadService;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.application.target.dto.SendTargetRowDto;
import com.ums.schedule.domain.target.upload.TargetUpload;

import java.util.*;

public class SendTargetReaderListener extends AnalysisEventListener<Map<Long, String>> {
    private final List<SendTargetRowDto> targetList = new ArrayList<>();
    private final TargetUpload targetUpload;
    private final TargetDbUploadService uploadService;

    private Map<Integer, String> headMap;

    public SendTargetReaderListener(TargetUpload targetUpload, TargetDbUploadService uploadService) {
        this.uploadService = uploadService;
        this.targetUpload = targetUpload;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
    }

    @Override
    public void invoke(Map<Long, String> targetData, AnalysisContext context) {
        SendTargetRowDto row = SendTargetRowDto.of(context.readRowHolder().getRowIndex(), headMap, targetData);
        targetList.add(row);
        if (targetList.size() % 10000 == 0) {
            uploadTargets();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 나머지 큐로 빼기
        if(!targetList.isEmpty()) {
            uploadTargets();
        }
        targetUpload.completed();
    }

    private void uploadTargets() {
        List<SendTargetDto> dtos = targetList.stream()
                .map(targetRow -> SendTargetDto.of(targetRow, targetUpload.getUploadId()))
                .toList();
        uploadService.create(this.targetUpload.getSendRequest(), dtos);
        this.targetList.clear();
    }
}
