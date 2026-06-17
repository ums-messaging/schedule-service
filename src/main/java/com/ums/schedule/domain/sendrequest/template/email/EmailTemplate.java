package com.ums.schedule.domain.sendrequest.template.email;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.template.ChannelTemplate;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import freemarker.template.Template;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate implements ChannelTemplate  {
    private String title;
    private Template body;

    public static EmailTemplate of(String title, Template html, List<EmailAttachment> messages) {
        EmailTemplate template = new EmailTemplate();
        template.assignTitle(title);
        template.assignMessage(html);
        return template;
    }

    private void assignMessage(Template content)  {
        this.body = content;
    }

    public void assignTitle(String title) {
        this.title = title;
    }

    public String getTitle(SendTarget target) {
        return target.parse(this.title);
    }

    @Override
    public String compile(SendTarget target) {
        return target.compile(body);
    }
}
