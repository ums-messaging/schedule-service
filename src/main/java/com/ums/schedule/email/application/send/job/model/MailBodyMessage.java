package com.ums.schedule.email.application.send.job.model;

import com.ums.schedule.email.application.message.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.email.domain.common.FileMetadata;
import com.ums.schedule.email.domain.template.EmailTemplate;

public record MailBodyMessage(
   String header,
   String body,
   String footer
) {
    public static MailBodyMessage of(EmailTemplate template, EmailConvertPolicy convertMessage) {
        FileMetadata header = template.getHeader();
        FileMetadata body = convertMessage.body();
        FileMetadata footer = template.getFooter();

        return new MailBodyMessage(
                header.fileKey(),
                body.fileKey(),
                footer.fileKey()
        );
    }
}
