package com.ums.schedule.email.application.message.factory;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.application.message.generator.policy.model.AttachMessage;
import com.ums.schedule.email.application.message.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.email.application.message.generator.resolver.ConvertMessageCommand;
import com.ums.schedule.email.application.message.generator.resolver.ConvertMessageResolver;
import com.ums.schedule.email.application.send.job.model.MailBodyMessage;
import com.ums.schedule.email.application.send.job.model.MessageSendData;
import com.ums.schedule.email.application.message.security.model.MessageFactoryContext;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.template.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MessageSendDataFactory {
    private final ConvertMessageResolver resolver;

    public MessageSendData create(MessageFactoryContext context)  {
        EmailSendMessage message = context.message();
        EnumMapperValue convertType = EnumMapperValue.fromEnumMapperType(message.getConvertMessage().convertType());

        EmailTemplate template = context.template();
        ConvertMessageCommand command = ConvertMessageCommand.of(convertType, message, template);
        EmailConvertPolicy convertMessage = resolver.resolve(command);

        List<AttachMessage> attachMessages = Stream.concat(Stream.ofNullable(convertMessage.attachMessage()), context.attachments()
                .stream().map(AttachMessage::of)).toList();

        MailBodyMessage bodyMessage = MailBodyMessage.of(template, convertMessage);

        return MessageSendData.of(message.getSubject(), bodyMessage, attachMessages);
    }
}
