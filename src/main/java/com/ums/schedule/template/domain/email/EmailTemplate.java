package com.ums.schedule.template.domain.email;

import com.ums.schedule.template.application.dto.EmailContentDto;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import java.util.Optional;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String templateKey;
    private EmailContent header;
    private EmailContent body;
    private EmailContent footer;
    private String baseDir;
    private String imageDir;

    public static EmailTemplate of(String templateKey, EmailContentDto dto) {
        EmailTemplate ofTemplate = new EmailTemplate(templateKey);
        ofTemplate.applyTemplate(dto);
        return ofTemplate;
    }

    private void applyTemplate(EmailContentDto dto) {
        this.header = dto.header();
        this.body = Optional.ofNullable(dto.body())
                .orElseThrow(TemplateContentRequiredException::ofBody);
        this.footer = dto.footer();
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

    public String getHeaderContent() {
        return getContent(this.header);
    }

    public String getFooterContent() {
        return getContent(this.footer);
    }

    private String getContent(EmailContent content) {
        return Optional.ofNullable(content)
                .map(h -> h.getContent())
                .orElse("");
    }
}
