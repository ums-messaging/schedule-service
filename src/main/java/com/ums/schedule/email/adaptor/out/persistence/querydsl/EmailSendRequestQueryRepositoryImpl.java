package com.ums.schedule.email.adaptor.out.persistence.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailSendRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QSendTargetErrorEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QSendTargetRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.projection.SendRequestReportQueryProjection;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.EmailSendRequestQueryRepository;
import com.ums.schedule.email.application.requests.result.SendRequestReportQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QEmailSendRequestEntity.emailSendRequestEntity;
import static com.ums.schedule.email.adaptor.out.persistence.jpa.entity.QSendTargetErrorEntity.sendTargetErrorEntity;

@Repository
@RequiredArgsConstructor
public class EmailSendRequestQueryRepositoryImpl implements EmailSendRequestQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SendRequestReportQueryProjection> countSendRequestReport(Long requestId) {
        SendRequestReportQueryProjection projection = queryFactory.select(
                        Projections.constructor(SendRequestReportQueryProjection.class,
                                emailSendRequestEntity,
                                JPAExpressions.select(sendTargetErrorEntity.count())
                                        .from(sendTargetErrorEntity)
                                        .where(sendTargetErrorEntity.sendRequestId.eq(requestId))
                        )).from(emailSendRequestEntity)
                .where(emailSendRequestEntity.id.eq(requestId))
                .fetchOne();

        return Optional.ofNullable(projection);
    }
}
