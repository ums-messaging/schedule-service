package com.ums.schedule.orchestrator.domain.upload;

import java.util.Optional;

public interface SendRequestUploadRepository {
    Optional<SendRequestUpload> findById(Long id);
    SendRequestUpload save(SendRequestUpload upload);
    SendRequestUpload findByCustomerIdAndObjectKey(String customerId, String objectkey);
}
