package com.ums.schedule.domain.exception.validation;

public class FileNotFoundException extends ValidationException {
    protected FileNotFoundException(String path) {
        super("[%s] 경로의 파일은 존재하지 않습니다.".formatted(path));
    }

    public static FileNotFoundException of(String path) {
        return new FileNotFoundException(path);
    }
}
