package com.ums.schedule.fixture.sendrequest;

import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import com.ums.schedule.domain.request.state.SendRequestCreateState;

public enum SendRequestField {
    TEMPLATE_KEY("templateKey", "my_template"),
    RETRY_CNT("retryCnt", 3),
    SENDER_KEY("senderKey", "jang314@naver.com"),
    CHANNEL_TYPE("channelType", ChannelType.EMAIL),
    CUSTOMER_REQUEST_KEY("CustomerRequestKey", new CustomerRequestKey("hyejin_company", "jang314")),
    CUSTOMER_REQUEST_ID("customerRequestId", "hyejin_company"),
    CUSTOMER_ID("customerId", "jang314"),
    STATUS("status", new SendRequestCreateState())

    ;
    String field;
    Object givenValue;
    SendRequestField(String field, Object givenValue) {
        this.field = field;
        this.givenValue = givenValue;
    }

    public String value() {
        return this.field;
    }
    public Object getGivenValue() {
        return this.givenValue;
    }
}
