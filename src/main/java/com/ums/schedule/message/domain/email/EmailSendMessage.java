package com.ums.schedule.message.domain.email;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ChannelTypeEnum;
import com.ums.schedule.message.code.ContentTypeEnum;
import com.ums.schedule.message.code.EncodingTypeEnum;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.template.domain.email.EmailTemplate;

import static com.ums.schedule.common.code.EnumMapperValue.fromEnumMapperType;
import static com.ums.schedule.message.code.ChannelTypeEnum.EMAIL;

public class EmailSendMessage extends SendMessage {
    private ContentTypeEnum contentType;
    private EncodingTypeEnum encodingType;
    private String mimeType;
    private EmailTemplate template;

    public static EmailSendMessage of(EmailTemplate template) {
        EmailSendMessage sendMessage = new EmailSendMessage(template);
        return sendMessage;
    }

    private EmailSendMessage(EmailTemplate template) {
        super(fromEnumMapperType(EMAIL));
        this.template = template;
    }

    public void resolveContentTypeEnum(EnumMapperValue contentType) {
        this.contentType = ContentTypeEnum.valueOf(contentType.value());
    }

    public void resolveEncodingTypeEnum(EnumMapperValue encodingType) {
        this.encodingType = EncodingTypeEnum.valueOf(encodingType.value());
    }

}
