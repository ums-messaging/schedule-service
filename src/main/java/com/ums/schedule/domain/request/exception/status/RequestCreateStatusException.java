package com.ums.schedule.domain.request.exception.status;

public class RequestCreateStatusException extends SendStatusException {
    private RequestCreateStatusException(String message) {
        super(message);
    }

    public static RequestCreateStatusException of() {
        return new RequestCreateStatusException(String.format("Create 상태로 변경 불가능합니다."));
    }
}
