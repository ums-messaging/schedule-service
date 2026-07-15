package com.ums.schedule.domain.exception.validation;

public class InvalidFileExtensionException extends ValidationException {

    protected InvalidFileExtensionException(String format) {
        super("%s is not supported".formatted(format));
    }

    public static InvalidFileExtensionException of(String format) {
        return new InvalidFileExtensionException(format);
    }
}
