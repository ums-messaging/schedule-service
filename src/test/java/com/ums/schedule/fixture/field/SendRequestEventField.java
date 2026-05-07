package com.ums.schedule.fixture.field;

public enum SendRequestEventField {
    EVENT_TYPE("eventType"),
    RESULT_CODE("resultCode"),
    RESULT_MESSAGE("resultMessage"),
    PAYLOAD("payload"),
    ISSUED_AT("issuedAt")
    ;
    String field;
    SendRequestEventField(String field) {
        this.field = field;
    }

    public String value() {
        return this.field;
    }
}
