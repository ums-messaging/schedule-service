package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailTemplateEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailSendMessageJpaRepository;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailTemplateJpaRepository;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import com.ums.schedule.email.domain.message.EmailSendMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailSendMessageJpaAdapter implements EmailSendMessageRepository {
    private final EmailTemplateJpaRepository templateRepository;
    private final EmailSendMessageJpaRepository repository;

    @Override
    public EmailSendMessage save(EmailSendMessage message) {
        UUID templateId = UuidCreator.fromString(message.getTemplateId());
        EmailTemplateEntity template = templateRepository.findById(templateId).orElseThrow();
        EmailSendMessageEntity entity = message.toEntity();
        entity.assignTemplate(template);
        EmailSendMessageEntity saved = repository.save(entity);

        return saved.toDomain();
    }

    @Override
    public Optional<EmailSendMessage> findById(String id) {
        UUID byId = UuidCreator.fromString(id);
        return repository.findById(byId)
                .map(EmailSendMessageEntity::toDomain);
    }
}