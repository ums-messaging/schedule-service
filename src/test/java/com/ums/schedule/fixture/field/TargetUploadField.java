package com.ums.schedule.fixture.field;

public enum TargetUploadField {
    UPLOAD_TYPE("uploadType"),
    STATUS("status"),
    CREATED_AT("createdAt"),

    ;

    String field;

    TargetUploadField(String field) {
        this.field = field;
    }

    public String value() {
        return this.field;
    }
}
