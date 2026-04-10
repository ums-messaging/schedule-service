package com.ums.schedule.repository;

import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.SendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SendRequestRepository extends JpaRepository<SendRequest, Long> {
    boolean existsByCustomerIdAAndCustomerRequestId(String customerRequestId, String customerId);
    Optional<EmailSendRequest> findEmailById(Long requestId);
}
