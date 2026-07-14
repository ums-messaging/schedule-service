package com.ums.schedule.domain.sendrequest.target.upload.builder;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.command.TargetFileUploadRequestCommand;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TargetUploadRequestCommandBuilder {
    private String templateId = UUID.randomUUID().toString();
    private String objectKey;
    private String fileName = UUID.randomUUID().toString()+"."+ TargetUploadFormatEnum.CSV.value();
    private Long maxFileSize = 3L;
    private Long fileSize = 1L;
    private boolean isExistFile = true;
    private List<TargetMessageData> targetList = List.of();

    public static TargetUploadRequestCommandBuilder builder() {
        return new TargetUploadRequestCommandBuilder();
    }

    public TargetUploadRequestCommandBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TargetUploadRequestCommandBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public TargetUploadRequestCommandBuilder fileMaxSize(Long fileMaxSize) {
        this.maxFileSize = fileMaxSize;
        return this;
    }

    public TargetUploadRequestCommandBuilder isExistFile(boolean isExistFile) {
        this.isExistFile = isExistFile;
        return this;
    }

    public TargetUploadRequestCommandBuilder targetList(TargetMessageData... targets) {
        this.targetList = Arrays.stream(targets).toList();
        return this;
    }

    public TargetFileUploadRequestCommand build() {
        return new TargetFileUploadRequestCommand(
                templateId,
                objectKey,
                fileName,
                maxFileSize,
                fileSize,
                isExistFile,
                targetList
        );
    }
}
