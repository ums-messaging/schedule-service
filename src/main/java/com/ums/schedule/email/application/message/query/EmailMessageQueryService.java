package com.ums.schedule.email.application.message.query;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailMessageQueryRepository;
import com.ums.schedule.email.application.message.query.result.SendMessageQueryResult;
import com.ums.schedule.email.domain.message.EmailSendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailMessageQueryService {
    private final EmailMessageQueryRepository repository;

    public SendMessageQueryResult findWithTemplateById(String id) {
        UUID messageId = UuidCreator.fromString(id);
        SendMessageQueryResult result = repository.findWithTemplateById(messageId)
                .orElseThrow();
        return result;
    }
}
