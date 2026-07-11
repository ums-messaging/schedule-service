package com.ums.schedule.domain.sendrequest.template.email;

import com.ums.schedule.common.util.ValidationUtils;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.template.ChannelTemplate;
import freemarker.template.Template;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate implements ChannelTemplate  {
    private String title;
    private Template body;
    private List<EmailAttachment> attachmentList = new ArrayList<>();

    public static EmailTemplate of(String title, Template html, List<EmailAttachment> attachments) {
        EmailTemplate template = new EmailTemplate();
        template.assignTitle(title);
        template.assignMessage(html);
        template.assignAttachments(attachments);
        return template;
    }

    private void assignAttachments(List<EmailAttachment> attachments) {
        this.attachmentList = attachments;
    }

    private void assignMessage(Template content)  {
        ValidationUtils.isEmpty("body", content);
        this.body = content;
    }

    public void assignTitle(String title) {
        ValidationUtils.isEmpty("title", title);
        this.title = title;
    }
}
