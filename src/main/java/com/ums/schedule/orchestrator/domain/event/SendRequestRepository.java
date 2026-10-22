package com.ums.schedule.orchestrator.domain.event;

import com.ums.schedule.orchestrator.application.requests.result.SendRequestSaveResult;


import java.util.Optional;

public interface SendRequestRepository {
    Optional<SendRequest> findById(Long id);
    boolean existsByCustomerIdAndCustomerKey(String customerId, String customerKey);
    SendRequestSaveResult save(SendRequest sendRequest, String objectKey);
    void updateStatus(SendRequest sendRequest);
}
