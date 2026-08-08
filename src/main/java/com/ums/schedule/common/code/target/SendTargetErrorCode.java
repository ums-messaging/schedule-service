package com.ums.schedule.common.code.target;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendTargetErrorCode implements EnumMapperType  {
    INVALID_EMAIL("", "올바른 형식의 이메일이 아님"),
    MESSAGE_PARSING_ERROR("", "메시지 PARSING 실패"),
    TARGET_ROW_READ_FAIL("", "[{0}] 파일 읽기 실패 (cause : {1}) "),
    SUCCESS("", "업로드 성공"),
    DUPLICATED("", "중복 데이터"),
    ETC("", "기타")
    ;

    String value;
    String description;

    SendTargetErrorCode(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return null;
    }

    @Override
    public String value() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
