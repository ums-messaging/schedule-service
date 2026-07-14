package com.ums.schedule.fixture.sendrequest;

import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.common.code.common.ChannelTypeEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;

public class SendRequestCreateCommandBuilder {
    private Long scheduleId;
    private ChannelTypeEnum channelType;
    private TargetUploadTypeEnum uploadType;
    private String uploadFormat;
    private String customerId;
    private String customerKey;
    private String templateKey;
    private String senderKey;
    private Integer retryCnt;

    public static SendRequestCreateCommandBuilder builder() {
        return new SendRequestCreateCommandBuilder();
    }

    private SendRequestCreateCommandBuilder() {
        this.scheduleId = 1L;
        this.channelType = ChannelTypeEnum.EMAIL;
        this.uploadType = TargetUploadTypeEnum.JSON;
        this.customerId = String.valueOf(SendRequestField.CUSTOMER_ID.givenValue);
        this.customerKey = String.valueOf(SendRequestField.CUSTOMER_REQUEST_ID.givenValue);
        this.senderKey = "jang314@test.com";
        this.templateKey = String.valueOf(SendRequestField.TEMPLATE_KEY.givenValue);
        this.retryCnt = 3;
    }

    public SendRequestCreateCommandBuilder scheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
        return this;
    }

    public SendRequestCreateCommandBuilder channelType(ChannelTypeEnum channelType) {
        this.channelType = channelType;
        return this;
    }

    public SendRequestCreateCommandBuilder uploadType(TargetUploadTypeEnum uploadType) {
        this.uploadType = uploadType;
        return this;
    }

    public SendRequestCreateCommandBuilder uploadFormat(String uploadFormat) {
        this.uploadFormat = uploadFormat;
        return this;
    }

    public SendRequestCreateCommandBuilder customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public SendRequestCreateCommandBuilder customerKey(String customerKey) {
        this.customerKey = customerKey;
        return this;
    }

    public SendRequestCreateCommandBuilder templateKey(String templateKey) {
        this.templateKey = templateKey;
        return this;
    }

    public SendRequestCreateCommandBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
        return this;
    }

    public SendRequestCreateCommandBuilder retryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
        return this;
    }

    public SendRequestCreateCommand build() {
        return new SendRequestCreateCommand(
                scheduleId,
                channelType,
                uploadType,
                uploadFormat,
                customerId,
                customerKey,
                templateKey,
                senderKey,
                retryCnt
        );
    }
}
