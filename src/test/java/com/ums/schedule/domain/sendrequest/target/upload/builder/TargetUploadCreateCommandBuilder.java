package com.ums.schedule.domain.sendrequest.target.upload.builder;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;

import java.util.List;

public class TargetUploadCreateCommandBuilder {
    private TargetUploadTypeEnum uploadType;
    private String uploadFormat;

    public static TargetUploadCreateCommandBuilder builder() {
        return new TargetUploadCreateCommandBuilder();
    }

    private TargetUploadCreateCommandBuilder() {
        this.uploadType = TargetUploadTypeEnum.JSON;
    }

    public TargetUploadCreateCommandBuilder uploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadCreateCommandBuilder uploadFormat(String uploadFormat) {
        this.uploadFormat = uploadFormat;
        return this;
    }

    public TargetUploadCreateCommand build() {
        return new TargetUploadCreateCommand(uploadType, uploadFormat);
    }
}
