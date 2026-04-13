package com.ums.schedule.code.send;

import com.ums.schedule.code.EnumMapperType;

public enum TargetUploadStatusEnum implements EnumMapperType  {
    CREATED("CREATED", "PRESIGNED_URL 발급"),
    UPLOAD("UPLOADED", "업로드 완료"),
    PARSING("PARSING", "대상자 처리 진행 중"),
    COMPLETED("COMPLETED", "처리 완료"),
    FAIL("FAIL", "처리 실패")
    ;
    String value;
    String description;

    TargetUploadStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }


    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public String description() {
        return this.description;
    }
}
