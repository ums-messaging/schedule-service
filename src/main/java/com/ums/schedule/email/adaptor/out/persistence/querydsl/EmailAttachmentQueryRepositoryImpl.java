package com.ums.schedule.email.adaptor.out.persistence.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailAttachmentEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailAttachmentEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailAttachmentQueryRepository;
import com.ums.schedule.email.domain.attachment.EmailAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EmailAttachmentQueryRepositoryImpl implements EmailAttachmentQueryRepository {
    private JPAQueryFactory queryFactory;

    @Override
    public List<EmailAttachment> findListByMessageId(UUID messageId) {
        QEmailAttachmentEntity attachment = QEmailAttachmentEntity.emailAttachmentEntity;
        return queryFactory.selectFrom(attachment)
                .where(attachment.message.id.eq(messageId))
                .fetch()
                .stream()
                .map(EmailAttachmentEntity::toDomain)
                .toList();
    }
}
