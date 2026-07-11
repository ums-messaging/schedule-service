package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.code.AttachmentType;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;

public class ConvertedAttachmentBuilder {
    private ConvertTypeEnum convertType;
    private AttachmentType type;
    private String key;

    public static ConvertedAttachmentBuilder builder() {
        return new ConvertedAttachmentBuilder();
    }

    private ConvertedAttachmentBuilder() {
        this.convertType = ConvertTypeEnum.NONE;
        this.type = AttachmentType.DIRECT;
        this.key = "attachment.html";
    }

    public ConvertedAttachmentBuilder convertType(ConvertTypeEnum convertType) {
        this.convertType = convertType;
        return this;
    }

    public ConvertedAttachmentBuilder attachmentType(AttachmentType attachmentType) {
        this.type = attachmentType;
        return this;
    }

    public ConvertedAttachmentBuilder key(String key) {
        this.key = key;
        return this;
    }

    public ConvertedAttachment build() {
        return new ConvertedAttachment(
                this.convertType,
                this.type,
                this.key
        );
    }

}
