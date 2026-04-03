package com.ums.schedule.send.application.worker;

import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.domain.target.SendTarget;
import com.ums.schedule.send.domain.request.upload.TargetUpload;

import java.util.List;
import java.util.concurrent.Callable;

public class TargetUploadWorker implements Callable<List<SendTarget>> {
    private final TargetUpload targetUpload;
    private final List<SendTargetDto> targetDtos;

    public TargetUploadWorker(TargetUpload targetUpload, List<SendTargetDto> targetDtos) {
        this.targetUpload = targetUpload;
        this.targetDtos = targetDtos;
    }

    @Override
    public List<SendTarget> call() throws Exception {
        return null;
    }
}
