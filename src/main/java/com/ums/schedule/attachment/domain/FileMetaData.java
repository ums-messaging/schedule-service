package com.ums.schedule.attachment.domain;

import com.ums.schedule.attachment.code.StorageTypeEnum;
import com.ums.schedule.attachment.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FileMetaData {
    private StorageTypeEnum storageType = StorageTypeEnum.S3;
    private String contentType = "html/text";
    private Long fileSize = 1L;
    private String fileKey = "template/template.html";
    private String originalFileName = "template.html";

    public static FileMetaData fromResponse(EmailContentResponse response) {
        FileMetaData metaData = new FileMetaData(response.fileKey());
        metaData.applyContentType(response.contentType());
        metaData.applyFileSize(response.fileSize());
        metaData.applyOriginalFileName(response.originalFileName());
        return metaData;
    }

    private void applyOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        validate(this.originalFileName, "Original file name ");
    }

    private void applyFileSize(Long fileSize) {
        this.fileSize = Optional.ofNullable(fileSize)
                .orElseThrow(() -> AttachmentPolicyRequiredException.ofFileMetadata("File size "));


    }

    private FileMetaData(String fileKey) {
        this.fileKey = fileKey;
        validate(fileKey, "File key ");
    }

    public FileMetaData resolveStorageType(EnumMapperValue storageType) {
        this.storageType = StorageTypeEnum.valueOf(storageType.value());
        return this;
    }

    private void applyContentType(String contentType) {
        this.contentType = contentType;
        validate(this.contentType, "Content type ");
    }

    public void validate(String target, String message) {
        if(!StringUtils.hasText(target)) {
            throw AttachmentPolicyRequiredException.ofFileMetadata(message);
        }
    }
}
