package com.ums.schedule.domain.channel.email.attachment;

import com.ums.schedule.code.email.StorageTypeEnum;

import java.util.UUID;

public class FileMetaDataTestBuilder {
    private StorageTypeEnum storageType = StorageTypeEnum.S3;
    private String contentType = "application/json";
    private Long fileSize = 10L;
    private String fileKey = UUID.randomUUID().toString();
    private String originalFileName;

    public static FileMetaDataTestBuilder builder() {
        return new FileMetaDataTestBuilder();
    }

    public FileMetaDataTestBuilder storageType(StorageTypeEnum storageType) {
        this.storageType = storageType;
        return this;
    }

    public FileMetaDataTestBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public FileMetaDataTestBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public FileMetaDataTestBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public FileMetaDataTestBuilder originalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public FileMetaData build() {
        return new FileMetaData(storageType, contentType, fileSize, fileKey, originalFileName);
    }

}
