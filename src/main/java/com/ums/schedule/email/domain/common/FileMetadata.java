package com.ums.schedule.email.domain.common;

public record FileMetadata(
        String contentType,
        String fileKey,
        Long fileSize
) {
    public static FileMetadata of(String fileKey, Long fileSize) {
        return FileMetadata.of(null, fileKey, fileSize);
    }

    public static FileMetadata of(String objectKey) {
        return new FileMetadata(
                null, objectKey, null
        );
    }

    public static FileMetadata of(String contentType, String fileKey, Long fileSize) {
        return new FileMetadata(
                contentType,
                fileKey,
                fileSize
        );
    }
}
