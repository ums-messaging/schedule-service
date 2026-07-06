package com.ums.schedule.common.exception.validation;

public class RequiredException extends ValidationException {
    protected RequiredException(String field) {
        super("%s is required.".formatted(field));
    }

    public static RequiredException fieldOf(String field) {
        return new RequiredException(field);
    }
}
