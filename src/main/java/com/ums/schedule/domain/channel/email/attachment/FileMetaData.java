package com.ums.schedule.domain.channel.email.attachment;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.code.email.StorageTypeEnum;
import com.ums.schedule.domain.channel.email.exception.AttachmentPolicyRequiredException;
import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FileMetaData {
    private StorageTypeEnum storageType;
    private String contentType;
    private Long fileSize;
    private String fileKey;
    private String originalFileName;

    public static FileMetaData fromResponse(EmailContentResponse response) {
        FileMetaData metaData = new FileMetaData(response.fileKey());
        metaData.applyContentType(response.contentType());
        metaData.applyFileSize(response.fileSize());
        metaData.applyOriginalFileName(response.originalFileName());
        return metaData;
    }

    public static FileMetaData of(AwsS3FileMetadataResponse response, String fileKey, String uploadKey) {
        FileMetaData metaData = new FileMetaData(uploadKey);
        metaData.applyContentType(response.contentType());
        metaData.applyFileSize(response.contentLength());
        metaData.resolveStorageType(EnumMapperValue.fromEnumMapperType(StorageTypeEnum.S3));
        metaData.applyOriginalFileName(fileKey);
        return metaData;
    }

    public static FileMetaData of(File toFile) {
        FileMetaData metaData = new FileMetaData(toFile.getPath()+ toFile.getName());
//        metaData.applyContentType(conver);
        metaData.applyFileSize(toFile.length());
//        metaData.applyOriginalFileName(toFile.);
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
