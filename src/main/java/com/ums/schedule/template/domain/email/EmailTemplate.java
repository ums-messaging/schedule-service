package com.ums.schedule.template.domain.email;

import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import freemarker.template.Template;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String templateKey;
    private Template header;
    private Template body;
    private Template footer;
    private Template convertTemplate;
    private String baseDir;
    private String imageDir;

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
}
