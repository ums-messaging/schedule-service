package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.generator.AttachmentMetadata;
import com.ums.schedule.common.code.email.ConvertType;

public class AttachmentMetadataBuilder {
    private ConvertType convertType;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;

    public static AttachmentMetadataBuilder builder() {
        return new AttachmentMetadataBuilder();
    }

    private AttachmentMetadataBuilder() {
        this.convertType = ConvertType.PDF;
        this.fileKeyTemplate = "${template}.pdf";
        this.attachmentName = "첨부파일명.pdf";
        this.downloadName = "첨부파일명.pdf";
    }

    public AttachmentMetadataBuilder convertType(ConvertType convertType) {
        this.convertType = convertType;
        return this;
    }


    public AttachmentMetadataBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public AttachmentMetadataBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public AttachmentMetadataBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public AttachmentMetadata build() {
        return new AttachmentMetadata(
                this.convertType,
                this.fileKeyTemplate,
                this.attachmentName,
                this.downloadName
        );
    }

}
