package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.code.AttachmentType;

public class ConvertedAttachmentBuilder {
    private ConvertTypeEnum convertType;
    private String fileKey;
    private String fileKeyTemplate;
    private Long fileSize;
    private String attachmentName;
    private String downloadName;

    public static ConvertedAttachmentBuilder builder() {
        return new ConvertedAttachmentBuilder();
    }

    private ConvertedAttachmentBuilder() {
        this.convertType = ConvertTypeEnum.PDF;
        this.fileKey = "body.html";
        this.fileKeyTemplate = "${template}.pdf";
        this.fileSize = 10L;
        this.attachmentName = "첨부파일명.pdf";
        this.downloadName = "첨부파일명.pdf";
    }

    public ConvertedAttachmentBuilder convertType(ConvertTypeEnum convertType) {
        this.convertType = convertType;
        return this;
    }

    public ConvertedAttachmentBuilder fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public ConvertedAttachmentBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public ConvertedAttachmentBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public ConvertedAttachmentBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public ConvertedAttachment build() {
        return new ConvertedAttachment(
                this.convertType,
                this.fileKey,
                this.fileKeyTemplate,
                this.fileSize,
                this.attachmentName,
                this.downloadName
        );
    }

}
