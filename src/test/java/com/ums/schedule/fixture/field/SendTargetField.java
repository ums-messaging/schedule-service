package com.ums.schedule.fixture.field;

public enum SendTargetField {
    TARGET_KEY("targetKey"),
    TARGET_NAME("targetName"),
    CONTACT("contact"),
    CONTENT_TYPE("content_type"),
    STATUS("status"),
    CONTENT("content"),
    ATTEMPT_NO("attemptNo"),
    CREATED_AT("createdAt"),
    ;

    String field;

    SendTargetField(String field) {
        this.field = field;
    }

    public String value() {
        return this.field;
    }
}
