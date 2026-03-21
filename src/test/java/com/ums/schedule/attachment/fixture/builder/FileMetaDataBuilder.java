package com.ums.schedule.attachment.fixture.builder;

import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.attachment.domain.FileMetaData;

public class FileMetaDataBuilder {
    private StorageTypeEnum storageType;
    private String contentType;
    private Long fileSize;
    private String fileKey;
    private String originalFileName;

    private FileMetaDataBuilder() { }

    public static FileMetaDataBuilder builder() {
        return new FileMetaDataBuilder();
    }

    public FileMetaDataBuilder storageType(StorageTypeEnum storageType) {
        this.storageType = storageType;
        return this;
    }
    public FileMetaDataBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public FileMetaDataBuilder fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public FileMetaDataBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public FileMetaDataBuilder originalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public FileMetaData build() {
        return new FileMetaData(
                storageType,
                contentType,
                fileSize,
                fileKey,
                originalFileName
        );
    }

}
