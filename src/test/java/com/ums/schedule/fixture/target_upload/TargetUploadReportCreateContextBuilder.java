package com.ums.schedule.fixture.target_upload;

import com.ums.schedule.application.sendrequest.context.TargetUploadReportCreateContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.code.ChannelTypeEnum;
import com.ums.schedule.domain.sendrequest.state.SendRequestCreateState;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;

public class TargetUploadReportCreateContextBuilder {
    private SendRequest sendRequest;
    private ChannelTypeEnum channelType;
    private TargetUploadTypeEnum uploadType;
    private EnumMapperValue uploadFormat;
    private String uploadkeyPrefix;
    private String downloadKeyPrefix;

    public static TargetUploadReportCreateContextBuilder builder() {
        return new TargetUploadReportCreateContextBuilder();
    }

    private TargetUploadReportCreateContextBuilder() {
        this.sendRequest = createSendRequest();
        this.channelType = ChannelTypeEnum.EMAIL;
        this.uploadType = TargetUploadTypeEnum.JSON;
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

    public TargetUploadReportCreateContextBuilder channelType(ChannelTypeEnum channelType) {
        this.channelType = channelType;
        return this;
    }

    public TargetUploadReportCreateContextBuilder uploadType(TargetUploadTypeEnum uploadType) {
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
