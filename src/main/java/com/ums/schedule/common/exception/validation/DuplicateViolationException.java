package com.ums.schedule.common.exception.validation;

public class DuplicateViolationException extends ValidationException {
    protected DuplicateViolationException(String field) {
        super("%s is duplicated.".formatted(field));
    }

    public static DuplicateViolationException fieldOf(String field) {
        return new DuplicateViolationException(field);
    }
}
