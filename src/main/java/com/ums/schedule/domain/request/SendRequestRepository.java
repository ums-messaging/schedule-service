package com.ums.schedule.domain.request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestRepository extends JpaRepository<SendRequest, Long> {
//    boolean existsByCustomerIdAAndCustomerRequestId(String customerRequestId, String customerId);
}
