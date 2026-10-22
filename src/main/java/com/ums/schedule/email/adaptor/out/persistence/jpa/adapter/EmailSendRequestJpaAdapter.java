package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailAttachmentEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendMessageEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailSendMessageJpaRepository;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailSendRequestJpaRepository;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailSendRequestQueryRepository;
import com.ums.schedule.email.adaptor.out.persistence.mapper.SendRequestMapper;
import com.ums.schedule.email.application.requests.result.SendRequestQueryResult;
import com.ums.schedule.email.application.requests.result.SendRequestReportQueryResult;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import com.ums.schedule.email.domain.requests.EmailSendRequest;
import com.ums.schedule.email.domain.requests.EmailSendRequestRepository;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;
import com.ums.schedule.email.domain.template.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailSendRequestJpaAdapter implements EmailSendRequestRepository {
    private final EmailSendMessageJpaRepository messageRepository;
    private final EmailSendRequestJpaRepository repository;
    private final EmailSendRequestQueryRepository queryRepository;


    @Override
    public EmailSendRequest save(EmailSendRequest sendRequest) {
        EmailSendMessageEntity message = messageRepository.findById(sendRequest.getMessageId())
                .orElseThrow();

        EmailSendRequestEntity entity = sendRequest.toEntity();
        entity.assignSendMessage(message);

        EmailSendRequestEntity saved = repository.save(entity);

        return saved.toDomain();
    }

    @Override
    public Optional<EmailSendRequest> findById(Long id) {
        return repository.findById(id)
                .map(EmailSendRequestEntity::toDomain);
    }

    @Override
    public Optional<SendRequestQueryResult> findByIdForSendRequest(Long id) {
        EmailSendRequestEntity entity = repository.findByRequestId(id).orElseThrow();
        EmailSendMessageEntity message = entity.getMessage();
        EmailTemplate template = message.getTemplate().toDomain();
        List<EmailAttachment> attachments = message.getAttachmentList().stream()
                .map(EmailAttachmentEntity::toDomain)
                .toList();

        return Optional.ofNullable(entity)
                .map(v -> SendRequestQueryResult.of(
                        v.toDomain(),
                        message.toDomain(),
                        template,
                        attachments)
                );
    }

    @Override
    public void updateStatus(EmailSendRequest sendRequest) {
        EmailSendRequestEntity entity = repository.findById(sendRequest.getId()).orElseThrow();
        entity.updateStatus(sendRequest);
    }

    @Override
    public EmailSendRequest updateRequestReport(Long requestId, SendRequestReport report) {
        EmailSendRequestEntity entity = repository.findById(requestId).orElseThrow();
        entity.updateSendRequestReport(report);
        return entity.toDomain();
    }
}
