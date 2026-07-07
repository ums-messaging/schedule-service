package com.ums.schedule.domain.sendrequest.target.upload.builder;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;


public class TargetUploadCreateCommandBuilder {
    private ChannelTypeEnum channelType;
    private TargetUploadTypeEnum uploadType;
    private String uploadFormat;

    public static TargetUploadCreateCommandBuilder builder() {
        return new TargetUploadCreateCommandBuilder();
    }

    private TargetUploadCreateCommandBuilder() {
        this.uploadType = TargetUploadTypeEnum.JSON;
    }

    public TargetUploadCreateCommandBuilder channelType(ChannelTypeEnum channelType) {
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
