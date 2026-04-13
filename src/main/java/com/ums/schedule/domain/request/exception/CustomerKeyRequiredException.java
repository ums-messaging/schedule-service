package com.ums.schedule.domain.request.exception;

public class CustomerKeyRequiredException extends RequiredException {

    public CustomerKeyRequiredException(String message) {
        super(message);
    }

    public static CustomerKeyRequiredException of() {
        return new CustomerKeyRequiredException("customer_key와 customer_id는 필수 값 입니다. ");
    }
}
