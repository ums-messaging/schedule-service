package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository;

import com.ums.schedule.orchestrator.application.requests.result.SendRequestQueryResult;

import java.util.Optional;

public interface SendRequestQueryRepository {
    Optional<SendRequestQueryResult> findSendRequestForRequest(Long requestId);
}
