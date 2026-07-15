package com.ums.schedule.fixture.sendrequest;

import com.ums.schedule.application.ums.common.request.model.SendRequestCreateContext;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.message.SendMessage;

public class SendRequestCreateContextBuilder {
    private ChannelType channelType;
    private Schedule schedule;
    private CustomerRequestKey customerKey;
    private SendMessage sendMessage;
    private String templateKey;
    private String senderKey;
    private Integer retryCnt;

    public static SendRequestCreateContextBuilder builder() {
        return new SendRequestCreateContextBuilder();
    }
    private SendRequestCreateContextBuilder() {
        channelType = ChannelType.EMAIL;
        templateKey = "my_template";
        senderKey = "jang@test.com";
        retryCnt = 3;
    }

    public SendRequestCreateContext build() {
        return new SendRequestCreateContext(
                channelType,
                schedule,
                customerKey,
                sendMessage,
                templateKey,
                senderKey,
                retryCnt
        );
    }

    public SendRequestCreateContextBuilder channelType(ChannelType channelType) {
        this.channelType = channelType;
        return this;
    }

    public SendRequestCreateContextBuilder schedule(Schedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public SendRequestCreateContextBuilder customerKey(CustomerRequestKey customerRequestKey) {
        this.customerKey = customerRequestKey;
        return this;
    }


    public SendRequestCreateContextBuilder sendMessage(SendMessage sendMessage) {
        this.sendMessage = sendMessage;
        return this;
    }
    public SendRequestCreateContextBuilder templateKey(String templateKey) {
        this.templateKey = templateKey;
        return this;
    }
    public SendRequestCreateContextBuilder senderKey(String senderKey) {
        this.senderKey = senderKey;
        return this;
    }

    public SendRequestCreateContextBuilder retryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
        return this;
    }

}
