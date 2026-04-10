package com.ums.schedule.attachment.application.model;

import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import lombok.Getter;


@Getter
public class AttachmentDto {
    private ConvertTypeEnum convertType;
    private AttachmentPolicy attachmentPolicy;
    private FileMetaData fileMetaData;
    private String objectKey;
    private String contentKey;

    public AttachmentDto(ConvertTypeEnum convertType) {
        this.convertType = convertType;
    }

    public boolean isConvert() {
        return convertType != ConvertTypeEnum.NONE;
    }

    public static AttachmentDto of(EmailContentResponse response, ConvertTypeEnum convertType) {
        AttachmentDto dto = new AttachmentDto(convertType);
        dto.applyAttachmentPolicy(response.attachmentName(), response.downloadName());
        dto.applyFileMetadata(response);
        dto.setKey(response.contentId(), response.fileKey());
        return dto;
    }
    private void setKey(String contentId, String fileKey) {
        this.contentKey = contentId;
        this.objectKey = fileKey;
    }

    private void applyFileMetadata(EmailContentResponse response) {
        this.fileMetaData = FileMetaData.fromResponse(response);
    }

    private void applyAttachmentPolicy(String attachmentName, String downloadName) {
        this.attachmentPolicy = AttachmentPolicy.of(attachmentName, downloadName);
    }
}
