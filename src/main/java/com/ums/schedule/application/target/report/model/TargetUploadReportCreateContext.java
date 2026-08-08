package com.ums.schedule.application.target.report.model;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadType;

import java.util.Map;

public record TargetUploadReportCreateContext(
        SendRequest sendRequest,
        ChannelType channelType,
        TargetUploadType uploadType,
        EnumMapperValue uploadFormat,
        String uploadkeyPrefix,
        String downloadKeyPrefix
) {
    public static TargetUploadReportCreateContext of(SendRequest sendRequest, TargetUploadCreateCommand command, EnumMapperValue format, Map<TargetUploadConfiguration, String> uploadMap) {

        return new TargetUploadReportCreateContext(
                sendRequest,
                sendRequest.getChannelType(),
                command.uploadType(),
                format,
                uploadMap.get(TargetUploadConfiguration.UPLOAD_KEY),
                uploadMap.get(TargetUploadConfiguration.DOWNLOAD_KEY)
        );
    }
}
