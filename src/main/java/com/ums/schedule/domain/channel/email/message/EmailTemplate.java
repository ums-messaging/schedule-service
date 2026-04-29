package com.ums.schedule.domain.channel.email.message;

import com.ums.schedule.code.email.ConvertTypeEnum;
import com.ums.schedule.code.email.EmailTemplateSectionEnum;
import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.adapter.api.template.email.EmailTemplateDetailResponse;
import com.ums.schedule.domain.channel.ChannelTemplate;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.attachment.FileMetaData;
import com.ums.schedule.domain.channel.email.exception.TemplateContentRequiredException;
import com.ums.schedule.domain.target.SendTarget;
import freemarker.template.Template;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate implements ChannelTemplate  {
    private String templateKey;
    private String title;
    private EmailBody emailBody;
    private Template header;
    private Template body;
    private Template footer;
    private String imageDir;
    private List<AttachmentDto> attachments = new ArrayList<>();

    public static EmailTemplate of(String templateKey, Map<EmailTemplateSectionEnum, Template> templateMap) {
        EmailTemplate ofTemplate = new EmailTemplate(templateKey);
        ofTemplate.applyTemplate(templateMap);
        return ofTemplate;
    }

    private void applyTemplate(Map<EmailTemplateSectionEnum, Template> templateMap) {
        this.header = templateMap.getOrDefault(EmailTemplateSectionEnum.HEADER, null);
        this.body = Optional.ofNullable(templateMap.get(EmailTemplateSectionEnum.BODY))
                .orElseThrow(TemplateContentRequiredException::ofBody);
        this.footer = templateMap.getOrDefault(EmailTemplateSectionEnum.FOOTER, null);;
    }
    private EmailTemplate(String templateKey) {
        if(!StringUtils.hasText(templateKey)) {
            throw TemplateContentRequiredException.ofTemplateKey();
        }
        this.templateKey = templateKey;
    }

    public void defineImageDir(String imageDir) {
        if(!StringUtils.hasText(imageDir)) {
            throw TemplateContentRequiredException.ofImageDir();
        }
        this.imageDir = imageDir;
    }

    public void defineTitle(EmailTitle title) {
        this.title = title.title();
    }

    public String getTitle(SendTarget target) {
        return target.parse(this.title);
    }

    public void defineAttachment(EmailBody body, EmailTemplateDetailResponse template) {
        this.attachments = Stream.concat(
                Optional.ofNullable(AttachmentDto.of(template.getBody(), body))
                        .map(Stream::of)
                        .orElseGet(Stream::empty),
                template.getAttachmentList()
                        .stream()
                        .map(content -> AttachmentDto.of(content, null))
                ).toList();
    }

    @Override
    public String compile(SendTarget target) {
        String header = target.compile(this.header);
        String body = target.compile(this.body);
        String footer = target.compile(this.footer);
        return header + body + footer;
    }
}
