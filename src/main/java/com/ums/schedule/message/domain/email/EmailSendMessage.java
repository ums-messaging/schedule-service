package com.ums.schedule.message.domain.email;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ContentTypeEnum;
import com.ums.schedule.message.code.EncodingTypeEnum;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.template.domain.TemplateTypeContent;
import com.ums.schedule.template.domain.email.EmailTemplate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.message.code.ChannelTypeEnum.EMAIL;

@Getter
public class EmailSendMessage extends SendMessage {
    private ContentTypeEnum contentType;
    private EncodingTypeEnum encodingType;
    private EmailTemplate emailTemplate;
    private List<Attachment> attachments = new ArrayList<>();

    public static EmailSendMessage of(TemplateTypeContent title, EmailTemplate template, List<Attachment> attachments) {
        EmailSendMessage sendMessage = new EmailSendMessage(template, title);
        sendMessage.addAttachment(attachments);
        return sendMessage;
    }

    private void addAttachment(List<Attachment> attachments) {
       attachments.stream().forEach(
               attachment -> attachment.applySendMessage(this)
       );
    }

    private EmailSendMessage(EmailTemplate template, TemplateTypeContent titleContent) {
        super(fromEnumMapperType(EMAIL), titleContent);
        this.emailTemplate = template;
    }

    public void resolveContentTypeEnum(EnumMapperValue contentType) {
        this.contentType = ContentTypeEnum.valueOf(contentType.value());
    }

    public void resolveEncodingTypeEnum(EnumMapperValue encodingType) {
        this.encodingType = EncodingTypeEnum.valueOf(encodingType.value());
    }

}
