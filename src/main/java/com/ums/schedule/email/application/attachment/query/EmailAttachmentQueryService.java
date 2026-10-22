package com.ums.schedule.email.application.attachment.query;

import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailAttachmentQueryRepository;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailAttachmentQueryService {
    private final EmailAttachmentQueryRepository repository;

}
