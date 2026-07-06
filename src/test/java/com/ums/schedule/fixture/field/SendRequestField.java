package com.ums.schedule.fixture.field;

public enum SendRequestField {
    TEMPLATE_KEY("templateKey"),
    RETRY_CNT("retryCnt"),
    SENDER_KEY("senderKey"),
    CHANNEL_TYPE("channelType"),
    CUSTOMER_REQUEST_KEY("CustomerRequestKey"),
    CUSTOMER_REQUEST_ID("customerRequestId"),
    CUSTOMER_ID("customerId"),
    STATUS("status")
    ;
    String field;
    SendRequestField(String field) {
        this.field = field;
    }

    public String value() {
        return this.field;
    }
}
