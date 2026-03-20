package com.ums.schedule.attachment.domain;

import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileMetaData {
    private StorageTypeEnum storageType;
    private String contentType;
    private Long fileSize;
    private String fileKey;
    private String originalFileName;

    public static FileMetaData fromResponse(EmailContentResponse response) {
        FileMetaData metaData = new FileMetaData(response.fileKey());
        metaData.applyContentType(response.contentType());
        return metaData;
    }

    private FileMetaData(String fileKey) {
        this.fileKey = fileKey;
    }

    public FileMetaData resolveStorageType(EnumMapperValue storageType) {
        this.storageType = StorageTypeEnum.valueOf(storageType.value());
        return this;
    }

    private void applyContentType(String contentType) {
        this.contentType = contentType;
    }

    private void applyFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    private void applyOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    private void validate() {
        if(!StringUtils.hasText(this.fileKey)) {

        }
    }
}
