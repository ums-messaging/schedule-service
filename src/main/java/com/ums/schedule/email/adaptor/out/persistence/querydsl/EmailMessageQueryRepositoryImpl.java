package com.ums.schedule.email.adaptor.out.persistence.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailTemplateEntity;
import com.ums.schedule.email.adaptor.out.persistence.querydsl.mapper.SendMessageQueryMapper;
import com.ums.schedule.email.application.message.query.result.SendMessageQueryResult;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailMessageQueryRepository;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailSendMessageEntity.emailSendMessageEntity;
import static com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailTemplateEntity.emailTemplateEntity;


@Component
@RequiredArgsConstructor
public class EmailMessageQueryRepositoryImpl implements EmailMessageQueryRepository {
    private final SendMessageQueryMapper mapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SendMessageQueryResult> findWithTemplateById(UUID messageId) {
        QEmailSendMessageEntity sendMessage = emailSendMessageEntity;
        QEmailTemplateEntity template = emailTemplateEntity;

        EmailSendMessageEntity findMessage = queryFactory.selectFrom(sendMessage)
                .join(sendMessage.template, template)
                .fetchJoin()
                .where(sendMessage.id.eq(messageId))
                .fetchOne();

        return Optional.ofNullable(findMessage)
                .map(v -> mapper.toMessageWithTemplate(v));
    }
}
