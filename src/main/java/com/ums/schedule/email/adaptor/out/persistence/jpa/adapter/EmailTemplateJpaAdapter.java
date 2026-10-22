package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailTemplateEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailTemplateJpaRepository;
import com.ums.schedule.email.domain.template.EmailTemplate;
import com.ums.schedule.email.domain.template.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class EmailTemplateJpaAdapter implements EmailTemplateRepository {
    private final EmailTemplateJpaRepository repository;

    @Override
    public EmailTemplate save(EmailTemplate template) {
        EmailTemplateEntity entity = repository.save(template.toEntity());
        return entity.toDomain();
    }

    @Override
    public Optional<EmailTemplate> findById(String id) {
        UUID byId = UuidCreator.fromString(id);
        return repository.findById(byId)
                .map(EmailTemplateEntity::toDomain);
    }
 }
