package com.ums.schedule.send.application.worker;

import com.ums.schedule.send.application.model.dto.SendTargetRowDto;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.send.domain.request.upload.TargetUpload;

import java.util.List;
import java.util.concurrent.Callable;

public class TargetUploadWorker implements Callable<List<EmailSendTarget>> {
    private final TargetUpload targetUpload;
    private final List<SendTargetRowDto> targetDtos;

    public TargetUploadWorker(TargetUpload targetUpload, List<SendTargetRowDto> targetDtos) {
        this.targetUpload = targetUpload;
        this.targetDtos = targetDtos;
    }

    @Override
    public List<EmailSendTarget> call() throws Exception {
        return null;
    }
}
