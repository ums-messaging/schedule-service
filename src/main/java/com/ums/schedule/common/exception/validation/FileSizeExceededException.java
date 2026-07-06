package com.ums.schedule.common.exception.validation;

public class FileSizeExceededException extends ValidationException {
    protected FileSizeExceededException(Long maxSize, Long fileSize) {
        super("업로드 한 파일은 %d MB 용량을 초과 할 수 없습니다. (현 용량 : %d MB)".formatted(maxSize, fileSize));
    }

    public static FileSizeExceededException of(Long maxSize, Long fileSize) {
        return new FileSizeExceededException(maxSize, fileSize);
    }
}
