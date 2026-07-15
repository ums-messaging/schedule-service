package com.ums.schedule.domain.request;

import com.ums.schedule.domain.request.customer.CustomerRequestKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestRepository extends JpaRepository<SendRequest, Long> {
    boolean existsByCustomerRequestKey(CustomerRequestKey customerRequestKey);
}
