package com.ums.schedule.attachment.fixture;

import com.ums.schedule.adapter.api.send.email.EmailSecurityPolicyRequest;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.attachment.fixture.builder.SecurityPolicyCommandBuilder;
import com.ums.schedule.adapter.api.send.email.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.template.email.EmailContentResponse;


import java.util.List;

import static com.ums.schedule.domain.email.attachment.code.StorageTypeEnum.S3;
import static com.ums.schedule.domain.template.domain.code.EmailTemplateSectionEnum.ATTACHMENT;
import static com.ums.schedule.domain.template.domain.code.EmailTemplateSectionEnum.BODY;
import static com.ums.schedule.domain.template.domain.code.TemplateContentFormatEnum.FILE;
import static com.ums.schedule.domain.template.domain.code.TemplateContentFormatEnum.HTML;

public class AttachmentFixture {

    public static EmailSendCreateRequest ofSecurityPolicy() {
        EmailSecurityPolicyRequest securityPolicy = SecurityPolicyCommandBuilder
                .builder()
                .build();
        return EmailMessageCommandBuilder
                .builder()
                .convertType(ConvertTypeEnum.PDF.value())
                .securityPolicy(securityPolicy)
                .build();
    }

    public static EmailSendCreateRequest ofSecurityPolicyAndConvertTypeIsNull() {
        EmailSecurityPolicyRequest securityPolicy = SecurityPolicyCommandBuilder
                .builder()
                .build();
        return EmailMessageCommandBuilder
                .builder()
                .convertType(null)
                .securityPolicy(securityPolicy)
                .build();
    }

    public static EmailContentResponse bodyOfHtml() {
        return EmailContentResponseBuilder.builder()
                .section(BODY)
                .format(HTML)
                .content(null)
                .fileKey("template/email/body.html")
                .attachmentName("첨부파일명")
                .downloadName("다운로드 파일명")
                .originalFileName("body.html")
                .fileSize(10L)
                .storageType(S3)
                .build();
    }

    public static List<EmailContentResponse> ofAttachmentList(int count) {

        EmailContentResponse attachment = EmailContentResponseBuilder.builder()
                .section(ATTACHMENT)
                .format(FILE)
                .content(null)
                .fileKey("template/email/body.html")
                .attachmentName("첨부파일명")
                .downloadName("다운로드 파일명")
                .originalFileName("body.html")
                .fileSize(10L)
                .storageType(S3)
                .build();

        return List.of(attachment, attachment, attachment);
    }

}
