package com.ums.schedule.common.code.target;

import com.ums.schedule.common.code.mapper.EnumMapperType;

public enum SendTargetResultCode implements EnumMapperType  {
    INVALID_EMAIL("", "%s는 올바른 형식의 이메일이 아닙니다."),
    MESSAGE_GENERATE_FAIL("", "메시지 생성에 실패했습니다. (원인 : %s)"),
    TARGET_UPLOAD_FAIL("", "대상자 업로드에 실패했습니다. (원인 : %s)"),
    TARGET_DATA_REQUIRED("", "%s은(는) 필수 값 입니다."),
    MESSAGE_PARSING_ERROR("", "[%s] 메시지 치환에 실패했습니다. (원인 : %s)"),
    TARGET_VARIABLE_REQUIRED("", "[%s] 메시지 치환 변수 %s가 존재하지 않습니다."),
    TARGET_ROW_READ_FAIL("", "[%s] 파일 읽기 실패 (cause : %s) "),
    CONVERT_MESSAGE_FAIL("", "메시지 컨버팅 실패 (cause : %s)"),
    SUCCESS("", "업로드 성공"),
    DUPLICATED("", "중복 데이터"),
    TEMPLATE_EMPTY("", "%s is empty."),
    ETC("", "기타")
    ;

    String value;
    String description;

    SendTargetResultCode(String value, String description) {
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
