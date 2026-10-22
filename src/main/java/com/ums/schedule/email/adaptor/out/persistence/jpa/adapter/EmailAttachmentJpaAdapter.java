package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailAttachmentEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailAttachmentJpaRepository;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailSendMessageJpaRepository;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.attachment.EmailAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailAttachmentJpaAdapter implements EmailAttachmentRepository {
    private final EmailSendMessageJpaRepository messageRepository;
    private final EmailAttachmentJpaRepository repository;

    @Override
    public void save(EmailAttachmentEntity attachment) {
        repository.save(attachment);
    }

    @Override
    public Optional<EmailAttachment> findById(String id) {
        UUID uuid = UuidCreator.fromString(id);
        return repository.findById(uuid)
                .map(EmailAttachmentEntity::toDomain);
    }

    @Override
    public List<EmailAttachment> saveAll(UUID messageId, List<EmailAttachment> attachments) {
        EmailSendMessageEntity message = messageRepository.findById(messageId).orElseThrow();

        List<EmailAttachmentEntity> entities = attachments.stream()
                .map(v -> {
                    EmailAttachmentEntity entity = v.toEntity();
                    entity.assignSendMessage(message);
                    return entity;
                }).toList();

        return repository.saveAll(entities).stream()
                .map(EmailAttachmentEntity::toDomain)
                .toList();
    }
}
