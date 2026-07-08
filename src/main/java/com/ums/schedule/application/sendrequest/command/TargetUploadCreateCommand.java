package com.ums.schedule.application.sendrequest.command;

import com.ums.schedule.application.sendrequest.context.TargetUploadReportCreateContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;


public record TargetUploadCreateCommand(
        ChannelTypeEnum channelType,
        TargetUploadTypeEnum uploadType,
        String uploadFormat) {

    public static TargetUploadCreateCommand of(SendRequest sendRequest, SendRequestCreateCommand command) {
        return new TargetUploadCreateCommand(
                sendRequest.getChannelType(),
                command.uploadType(),
                command.uploadFormat()
        );
    }

    public TargetUploadReportCreateContext toContext(SendRequest sendRequest, EnumMapperValue format, TargetUploadProperties properties) {
        return TargetUploadReportCreateContext.of(sendRequest, this, format, properties);
    }

}