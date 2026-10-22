package com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query;


import com.ums.schedule.email.application.message.query.result.SendMessageQueryResult;

import java.util.Optional;
import java.util.UUID;

public interface EmailMessageQueryRepository {
    Optional<SendMessageQueryResult> findWithTemplateById(UUID messageId);
}
