package com.ums.schedule.template.domain.email;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.template.application.dto.EmailContentDto;
import com.ums.schedule.template.application.response.TemplateResponse;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateDetailResponse;
import com.ums.schedule.template.application.response.email.EmailTemplateResponse;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.code.EmailTemplateSectionEnum;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.exception.TemplateContentRequiredException;
import com.ums.schedule.template.exception.TitleRequiredException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.template.domain.code.EmailTemplateSectionEnum.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplate {
    private String templateKey;
    private String templateTitle;
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

    public void defineTitle(String msgTitle) {
        if(!StringUtils.hasText(msgTitle)) {
            throw TemplateContentRequiredException.ofTitle();
        }
        this.templateTitle = msgTitle;
    }
}
