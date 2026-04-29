package com.ums.schedule.application.channel.email.dto;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.code.email.ConvertTypeEnum;
import com.ums.schedule.domain.channel.email.attachment.AttachmentPolicy;
import com.ums.schedule.domain.channel.email.attachment.FileMetaData;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import lombok.Getter;


@Getter
public class AttachmentDto {
    private EmailBody body;
    private AttachmentPolicy attachmentPolicy;
    private FileMetaData fileMetaData;
    private String objectKey;
    private String contentKey;

    private AttachmentDto(EmailBody body) {
        this.body = body;
    }

    public boolean isConvert() {
        return !body.getConvertType().value().equals(ConvertTypeEnum.NONE.value());
    }

    public static AttachmentDto of(EmailContentResponse response, EmailBody body) {
        AttachmentDto dto = new AttachmentDto(body);
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
