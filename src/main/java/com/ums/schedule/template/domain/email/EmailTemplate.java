package com.ums.schedule.template.domain.email;

import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
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

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String templateKey;
    private String title;
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
        this.header = templateMap.getOrDefault(HEADER, null);
        this.body = Optional.ofNullable(templateMap.get(BODY))
                .orElseThrow(TemplateContentRequiredException::ofBody);
        this.footer = templateMap.getOrDefault(FOOTER, null);;
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

    public void defineAttachment(EmailBody body, EmailTemplateDetailResponse template) {
        this.attachments = Stream.concat(
                Optional.ofNullable(body.toAttachmentDto(template.getBody()))
                        .map(Stream::of)
                        .orElseGet(Stream::empty),
                template.getAttachmentList()
                        .stream()
                        .map(content -> AttachmentDto.of(content, ConvertTypeEnum.NONE))
                ).toList();
    }
}
