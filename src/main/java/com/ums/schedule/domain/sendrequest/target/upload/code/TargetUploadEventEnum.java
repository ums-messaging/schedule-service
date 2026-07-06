package com.ums.schedule.domain.sendrequest.target.upload.code;

import com.ums.schedule.common.converter.StatusStateEvent;

public enum TargetUploadEventEnum implements StatusStateEvent {
    TARGET_UPLOAD_CREATED("CREATED", "대상자 업로드 생성됨"),
    TARGET_UPLOAD_READY("WAITING", "대상자 업로드 준비됨"),
    TARGET_UPLOAD_REQUESTED("REQUEST", "대상자 업로드 요청됨"),
    TARGET_UPLOAD_STARTED("PARSING", "대상자 업로드 시작됨"),
    TARGET_UPLOAD_COMPLETED("COMPLETED", "대상자 업로드 완료됨"),
    TARGET_UPLOAD_FAIL("FAIL", "대상자 업로드 실패됨")
    ;

    String value;
    String description;

    TargetUploadEventEnum(String value, String description) {
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