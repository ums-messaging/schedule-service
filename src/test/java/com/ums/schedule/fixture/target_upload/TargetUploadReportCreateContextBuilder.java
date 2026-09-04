package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.target.report.model.TargetUploadReportCreateContext;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;

public class TargetUploadReportCreateContextBuilder {
    private SendRequest sendRequest;
    private ChannelType channelType;
    private TargetUploadType uploadType;
    private EnumMapperValue uploadFormat;
    private String uploadkeyPrefix;
    private String downloadKeyPrefix;

    public static TargetUploadReportCreateContextBuilder builder() {
        return new TargetUploadReportCreateContextBuilder();
    }

    private TargetUploadReportCreateContextBuilder() {
        this.sendRequest = createSendRequest();
        this.channelType = ChannelType.EMAIL;
        this.uploadType = TargetUploadType.JSON;
        this.uploadFormat = EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.EXCEL);
        this.uploadkeyPrefix = "/target/upload/";
        this.downloadKeyPrefix = "/target/upload/download";
    }

    private SendRequest createSendRequest() {
        return SendRequestEntityBuilder.builder()
                .state(new SendRequestCreateState())
                .build();
    }

    public TargetUploadReportCreateContextBuilder sendRequest(SendRequest sendRequest) {
        this.sendRequest = sendRequest;
        return this;
    }

    public TargetUploadReportCreateContextBuilder channelType(ChannelType channelType) {
        this.channelType = channelType;
        return this;
    }

    public TargetUploadReportCreateContextBuilder uploadType(TargetUploadType uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public TargetUploadReportCreateContextBuilder uploadFormat(EnumMapperValue uploadFormat) {
        this.uploadFormat = uploadFormat;
        return this;
    }

    public TargetUploadReportCreateContextBuilder uploadKeyPrefix(String uploadKeyPrefix) {
        this.uploadkeyPrefix = uploadKeyPrefix;
        return this;
    }

    public TargetUploadReportCreateContextBuilder downloadKeyPrefix(String downloadKeyPrefix) {
        this.downloadKeyPrefix = downloadKeyPrefix;
        return this;
    }

    public TargetUploadReportCreateContext build() {
        return new TargetUploadReportCreateContext(
                sendRequest,
                channelType,
                uploadType,
                uploadFormat,
                uploadkeyPrefix,
                downloadKeyPrefix
        );
    }
}
