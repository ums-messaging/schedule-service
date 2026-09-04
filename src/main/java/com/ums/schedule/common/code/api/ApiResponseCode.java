package com.ums.schedule.common.code.api;

import com.ums.schedule.common.code.mapper.EnumMapperType;
import org.springframework.http.HttpStatus;

public enum ApiResponseCode implements EnumMapperType {
    CREATED(HttpStatus.CREATED, "정상 처리 되었습니다."),
    SUCCESS(HttpStatus.OK, "정상 처리 되었습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "필수 값 누락 또는 형식 오류"),
    CONFLICT(HttpStatus.CONFLICT, "처리할 수 없는 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 존재하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인하지 않았거나 토큰이 만료되었습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 처리 중 오류가 발생했습니다.")
    ;

    HttpStatus value;
    String description;

    ApiResponseCode(HttpStatus value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String value() {
        return this.value.name();
    }

    @Override
    public String description() {
        return this.description;
    }

    public HttpStatus getHttpStatus() {
        return this.value;
    }
}
