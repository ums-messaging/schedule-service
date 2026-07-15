package com.ums.schedule.domain.request.target.upload.builder;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;


public class TargetUploadCreateCommandBuilder {
    private ChannelType channelType;
    private TargetUploadTypeEnum uploadType;
    private String uploadFormat;

    public static TargetUploadCreateCommandBuilder builder() {
        return new TargetUploadCreateCommandBuilder();
    }

    private TargetUploadCreateCommandBuilder() {
        this.uploadType = TargetUploadTypeEnum.JSON;
    }

    public TargetUploadCreateCommandBuilder channelType(ChannelType channelType) {
        this.channelType = channelType;
        return this;
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
        return new TargetUploadCreateCommand(channelType, uploadType, uploadFormat);
    }
}
