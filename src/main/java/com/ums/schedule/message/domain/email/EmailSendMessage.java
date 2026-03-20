package com.ums.schedule.message.domain.email;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ContentTypeEnum;
import com.ums.schedule.message.code.EncodingTypeEnum;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.template.domain.code.TemplateTypeEnum;
import com.ums.schedule.template.domain.email.EmailTemplate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.message.code.ChannelTypeEnum.EMAIL;

@Getter
public class EmailSendMessage extends SendMessage {
    private ContentTypeEnum contentType;
    private EncodingTypeEnum encodingType;
    private EmailTemplate template;
    private List<Attachment> attachments = new ArrayList<>();

    public static EmailSendMessage of(EnumMapperValue templateType, String title, EmailTemplate template, List<Attachment> attachments) {
        EmailSendMessage sendMessage = new EmailSendMessage(templateType, template, title);
        sendMessage.toTemplate(template);
        sendMessage.addAttachment(attachments);
        return sendMessage;
    }

    private void toTemplate(EmailTemplate template) {
        String header = template.getHeader().getContent();
        String body = template.getBody().getContent();
        String footer = template.getFooter().getContent();

        writeTemplate(header+body+footer);
    }


    private void addAttachment(List<Attachment> attachments) {
       attachments.stream().forEach(
               attachment -> attachment.applySendMessage(this)
       );
    }

    private EmailSendMessage(EnumMapperValue templateType, EmailTemplate template, String title) {
        super(fromEnumMapperType(EMAIL), templateType, title);
        this.template = template;
    }

    public void resolveContentTypeEnum(EnumMapperValue contentType) {
        this.contentType = ContentTypeEnum.valueOf(contentType.value());
    }

    public void resolveEncodingTypeEnum(EnumMapperValue encodingType) {
        this.encodingType = EncodingTypeEnum.valueOf(encodingType.value());
    }

}
