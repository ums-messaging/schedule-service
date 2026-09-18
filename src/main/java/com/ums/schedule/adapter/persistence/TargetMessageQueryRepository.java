package com.ums.schedule.adapter.persistence;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ums.schedule.application.sendrequest.target.query.TargetDuplicatedQuery;
import com.ums.schedule.application.ums.common.send.model.EmailTargetGroupQueryResult;
import com.ums.schedule.domain.send.email.job.DomainGroupTarget;
import com.ums.schedule.domain.target.QTargetMessage;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.QEmailTargetMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TargetMessageQueryRepository {
    private final JPAQueryFactory factory;

    public List<TargetMessage> findByTargetKeysAndContacts(TargetDuplicatedQuery query) {
        QTargetMessage targetMessage = QTargetMessage.targetMessage;
        return factory.selectFrom(targetMessage)
                .where(
                        targetMessage.targetUploadReport.id.eq(query.uploadId()),
                        targetMessage.targetKey.in(query.targetKeyList())
                                .or(targetMessage.contact.in(query.contacts()))
                )
                .fetch();
    }
    public List<EmailTargetGroupQueryResult> findTargetGrouping(UUID uploadId) {
        QEmailTargetMessage targetMessage = QEmailTargetMessage.emailTargetMessage;
        return factory.select(Projections.constructor(
                        EmailTargetGroupQueryResult.class,
                        targetMessage.groupId,
                        targetMessage.domain,
                        targetMessage.count()))
                .from(targetMessage)
                .where(
                        targetMessage.targetUploadReport.id.eq(uploadId)
                )
                .groupBy(targetMessage.groupId, targetMessage.domain)
                .fetch();
    }

    public List<TargetGroupQueryResult> findGroupByGroupIdAndDomain(String uploadId, Long groupId, String domain) {
        QEmailTargetMessage targetMessage = QEmailTargetMessage.emailTargetMessage;
        return factory.select(
                Projections.constructor(
                        TargetGroupQueryResult.class,
                        targetMessage.id,
                        targetMessage.groupId,
                        targetMessage.domain,
                        targetMessage.targetKey,
                        targetMessage.targetName,
                        targetMessage.contact,
                        targetMessage.subject,
                        targetMessage.headerMessage,
                        targetMessage.bodyMessage,
                        targetMessage.footerMessage,
                        targetMessage.attachments
                    )
                )
                .from(targetMessage)
                .where(
                        targetMessage.targetUploadReport.id.eq(UUID.fromString(uploadId))
                                .and(targetMessage.groupId.eq(groupId))
                                .and(targetMessage.domain.eq(domain)))
                .fetch();
    }
}
