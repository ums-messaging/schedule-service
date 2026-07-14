package com.ums.schedule.application.sendrequest.context;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.common.code.common.ChannelTypeEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;

public record TargetUploadReportCreateContext(
        SendRequest sendRequest,
        ChannelTypeEnum channelType,
        TargetUploadTypeEnum uploadType,
        EnumMapperValue uploadFormat,
        String uploadkeyPrefix,
        String downloadKeyPrefix
) {
    public static TargetUploadReportCreateContext of(SendRequest sendRequest, TargetUploadCreateCommand command, EnumMapperValue format, TargetUploadProperties properties) {

        return new TargetUploadReportCreateContext(
                sendRequest,
                sendRequest.getChannelType(),
                command.uploadType(),
                format,
                properties.getUploadKey(),
                properties.getDownloadKey()
        );
    }
}
