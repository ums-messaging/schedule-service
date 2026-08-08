package com.ums.schedule.application.sendrequest.command;

import com.ums.schedule.application.target.report.model.TargetUploadReportCreateContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadType;

import java.util.Map;


public record TargetUploadCreateCommand(
        ChannelType channelType,
        TargetUploadType uploadType,
        String uploadFormat) {

    public static TargetUploadCreateCommand of(SendRequest sendRequest, SendRequestCreateCommand command) {
        return new TargetUploadCreateCommand(
                sendRequest.getChannelType(),
                command.uploadType(),
                command.uploadFormat()
        );
    }

    public TargetUploadReportCreateContext toContext(SendRequest sendRequest, Map<TargetUploadConfiguration, String> uploadDirMap, EnumMapperValue format) {
        return TargetUploadReportCreateContext.of(sendRequest, this, format, uploadDirMap);
    }

}