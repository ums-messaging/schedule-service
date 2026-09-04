package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.convert.ConvertMail;

public class ConvertMailBuilder {
    private ConvertType convertType;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;

    public static ConvertMailBuilder builder() {
        return new ConvertMailBuilder();
    }


    private ConvertMailBuilder() {
        this.convertType = ConvertType.NONE;
        this.attachmentName = "8 month bill.";
        this.downloadName = "8 month bill.";
        this.fileKeyTemplate = "${target_id}.pdf";
    }

    public ConvertMailBuilder convertType(ConvertType convertType) {
        this.convertType = convertType;
        return this;
    }

    public ConvertMailBuilder fileKeyTemplate(String fileKeyTemplate) {
        this.fileKeyTemplate = fileKeyTemplate;
        return this;
    }

    public ConvertMailBuilder attachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public ConvertMailBuilder downloadName(String downloadName) {
        this.downloadName = downloadName;
        return this;
    }

    public ConvertMail build() {
        return new ConvertMail(
                convertType,
                fileKeyTemplate,
                attachmentName,
                downloadName
        );
    }
}
