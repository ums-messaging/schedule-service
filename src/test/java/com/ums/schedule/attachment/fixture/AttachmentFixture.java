package com.ums.schedule.attachment.fixture;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.attachment.fixture.builder.EmailContentResponseBuilder;
import com.ums.schedule.attachment.fixture.builder.EmailMessageCommandBuilder;
import com.ums.schedule.attachment.fixture.builder.SecurityPolicyCommandBuilder;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;


import java.util.List;

import static com.ums.schedule.attachment.code.StorageTypeEnum.S3;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.ATTACHMENT;
import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.BODY;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.FILE;
import static com.ums.schedule.template.domain.code.TemplateContentFormatEnum.HTML;

public class AttachmentFixture {

    public static EmailMessageCommand ofSecurityPolicy() {
        SecurityPolicyCommand securityPolicy = SecurityPolicyCommandBuilder
                .builder()
                .build();
        return EmailMessageCommandBuilder
                .builder()
                .convertType(ConvertTypeEnum.PDF.value())
                .securityPolicy(securityPolicy)
                .build();
    }

    public static EmailMessageCommand ofSecurityPolicyAndConvertTypeIsNull() {
        SecurityPolicyCommand securityPolicy = SecurityPolicyCommandBuilder
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
