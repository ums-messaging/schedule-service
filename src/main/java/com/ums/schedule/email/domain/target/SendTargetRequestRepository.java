package com.ums.schedule.email.domain.target;

import com.ums.schedule.email.application.target.query.model.SendTargetGroupQuery;
import com.ums.schedule.email.common.code.EmailSendStatusCode;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SendTargetRequestRepository {
    List<SendTargetRequest> saveAll(Long requestId, EmailSendStatusCode status, List<SendTargetRequest> targets);
    void save(SendTargetRequest request);
    Optional<SendTargetRequest> findById(Long id);
    Map<Integer, List<SendTargetRequest>> findListAndGrouping(SendTargetGroupQuery query);
}
