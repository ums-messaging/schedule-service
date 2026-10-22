package com.ums.schedule.orchestrator.adaptor.out.persistance.querydsl.mapper;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestEntity;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestQueryResult;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SendRequestQueryMapper {

    public Optional<SendRequestQueryResult> toSendRequestResult(SendRequestEntity entity) {
        return Optional.ofNullable(entity)
                .map(v -> SendRequestQueryResult.of(
                        v.getSchedule().toDomain(),
                        v.toDomain(),
                        v.getSendRequestUpload().toDomain()
                ));
    }
}
