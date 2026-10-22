package com.ums.schedule.email.adaptor.out.persistence.querydsl.mapper;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailTemplateEntity;
import com.ums.schedule.email.application.message.query.result.SendMessageQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendMessageQueryMapper {

    public SendMessageQueryResult toMessageWithTemplate(EmailSendMessageEntity entity) {
        EmailTemplateEntity template = entity.getTemplate();
        return SendMessageQueryResult.of(entity.getId(), entity.getSubject(), template.toDomain());
    }
}
