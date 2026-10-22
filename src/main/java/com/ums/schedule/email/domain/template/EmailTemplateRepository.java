package com.ums.schedule.email.domain.template;

import java.util.Optional;

public interface EmailTemplateRepository {
    EmailTemplate save(EmailTemplate template);
    Optional<EmailTemplate> findById(String id);
}
