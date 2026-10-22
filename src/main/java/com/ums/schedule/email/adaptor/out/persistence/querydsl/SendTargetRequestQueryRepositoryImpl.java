package com.ums.schedule.email.adaptor.out.persistence.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailSendRequestEntity;
import com.ums.schedule.email.application.target.query.model.SendTargetGroupQuery;
import com.ums.schedule.email.application.target.query.model.TargetDuplicatedQuery;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QSendTargetRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.SendTargetRequestQueryRepository;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailSendRequestEntity.emailSendRequestEntity;
import static com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QSendTargetRequestEntity.sendTargetRequestEntity;

@Repository
@RequiredArgsConstructor
public class SendTargetRequestQueryRepositoryImpl implements SendTargetRequestQueryRepository {
    private final JPAQueryFactory factory;

    public List<SendTargetRequest> findByTargetKeysAndContacts(TargetDuplicatedQuery query) {
        QSendTargetRequestEntity target = sendTargetRequestEntity;
        return factory.selectFrom(target)
                .where(target.sendRequest.requestId.eq(query.sendRequestId())
                        .and(target.targetKey.in(query.targetKeyList())
                                        .or(target.email.in(query.contacts())))
                )
                .fetch()
                .stream()
                .map(SendTargetRequestEntity::toDomain)
                .toList();
    }

    @Override
    public List<SendTargetRequestEntity> findListBySendRequestIdAndGroupId(SendTargetGroupQuery query) {
        QEmailSendRequestEntity sendRequest = emailSendRequestEntity;
        QSendTargetRequestEntity sendTargetRequest = sendTargetRequestEntity;

        List<SendTargetRequestEntity> results = factory.selectFrom(sendTargetRequest)
                .join(sendTargetRequest.sendRequest, sendRequest)
                .fetchJoin()
                .where(sendRequest.id.eq(query.sendRequestId())
                        .and(sendTargetRequest.groupId.eq(query.groupId()))
                        .and(sendTargetRequest.groupKey.eq(query.groupKey()))
                ).fetch();
        return results;
    }
//    public List<EmailTargetGroupQueryResult> findTargetGrouping(Long requestId) {
//        QEmailTargetMessage targetMessage = QEmailTargetMessage.emailTargetMessage;
//        QTargetUploadReport targetUploadReport = QTargetUploadReport.targetUploadReport;
//        QSendRequest sendRequest = QSendRequest.sendRequest;
//
//        return factory.select(Projections.constructor(
//                        EmailTargetGroupQueryResult.class,
//                        targetMessage.groupId,
//                        sendRequest.messageId,
//                        targetMessage.domain,
//                        targetMessage.id.count()))
//                .from(targetMessage)
//                .join(targetUploadReport).on(targetMessage.uploadId.eq(targetUploadReport.id.toString()))
//                .join(sendRequest).on(sendRequest.currentTargetUpload.id.eq(targetUploadReport.id))
//                .where(
//                        sendRequest.id.eq(requestId)
//                )
//                .groupBy(targetMessage.groupId, targetMessage.domain)
//                .fetch();
//    }
//
//    public List<TargetGroupQueryResult> findGroupByGroupIdAndDomain(String requestId, Long groupId, String domain) {
//        QEmailTargetMessage targetMessage = QEmailTargetMessage.emailTargetMessage;
//        QTargetUploadReport targetUploadReport = QTargetUploadReport.targetUploadReport;
//        QSendRequest sendRequest = QSendRequest.sendRequest;
//        long id = Long.parseLong(requestId);
//        return factory.select(
//                Projections.constructor(
//                        TargetGroupQueryResult.class,
//                        targetMessage.id,
//                        targetMessage.groupId,
//                        targetMessage.domain,
//                        targetMessage.targetKey,
//                        targetMessage.targetName,
//                        targetMessage.contact,
//                        targetMessage.subject,
//                        targetMessage.headerMessage,
//                        targetMessage.bodyMessage,
//                        targetMessage.footerMessage,
//                        targetMessage.attachments
//                    )
//                )
//                .from(targetMessage)
//                .join(targetUploadReport).on(targetMessage.uploadId.eq(targetUploadReport.id.toString()))
//                .join(sendRequest).on(sendRequest.currentTargetUpload.id.eq(targetUploadReport.id))
//                .where(
//                        sendRequest.id.eq(id)
//                                        .and(targetMessage.groupId.eq(groupId))
//                                .and(targetMessage.domain.eq(domain)))
//                .fetch();
//    }
}
