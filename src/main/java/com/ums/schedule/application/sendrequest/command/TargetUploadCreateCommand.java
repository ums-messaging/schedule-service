package com.ums.schedule.application.sendrequest.command;

import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;


public record TargetUploadCreateCommand(
        TargetUploadTypeEnum uploadType,
        String uploadFormat) {

    public static TargetUploadCreateCommand of(TargetUploadTypeEnum uploadType, String uploadFormat) {
        return new TargetUploadCreateCommand(
                uploadType,
                uploadFormat
        );
    }


}