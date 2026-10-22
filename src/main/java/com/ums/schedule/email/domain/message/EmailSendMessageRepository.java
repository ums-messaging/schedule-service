package com.ums.schedule.email.domain.message;

import java.util.Optional;

public interface EmailSendMessageRepository {
    EmailSendMessage save(EmailSendMessage message);
    Optional<EmailSendMessage> findById(String id);
}
