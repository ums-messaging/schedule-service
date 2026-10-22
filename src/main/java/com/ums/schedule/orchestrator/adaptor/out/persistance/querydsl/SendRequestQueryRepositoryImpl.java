package com.ums.schedule.orchestrator.adaptor.out.persistance.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.QScheduleEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.QSendRequestEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.QSendRequestUploadEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.SendRequestQueryRepository;
import com.ums.schedule.orchestrator.adaptor.out.persistance.querydsl.mapper.SendRequestQueryMapper;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.QScheduleEntity.scheduleEntity;

@Repository
@RequiredArgsConstructor
public class SendRequestQueryRepositoryImpl implements SendRequestQueryRepository {
    private final SendRequestQueryMapper mapper;
    private final JPAQueryFactory queryFactory;

    public Optional<SendRequestQueryResult> findSendRequestForRequest(Long requestId) {
        QScheduleEntity schedule = scheduleEntity;
        QSendRequestEntity sendRequest = QSendRequestEntity.sendRequestEntity;
        QSendRequestUploadEntity sendRequestUpload = QSendRequestUploadEntity.sendRequestUploadEntity;

        SendRequestEntity result = queryFactory.selectFrom(sendRequest)
                .join(sendRequest.schedule, schedule)
                .join(sendRequest.sendRequestUpload, sendRequestUpload)
                .fetchJoin()
                .where(sendRequest.id.eq(requestId))
                .fetchOne();

        return mapper.toSendRequestResult(result);
    }
}
