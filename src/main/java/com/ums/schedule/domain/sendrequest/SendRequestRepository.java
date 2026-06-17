package com.ums.schedule.domain.sendrequest;

import com.ums.schedule.domain.sendrequest.customer.CustomerRequestKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestRepository extends JpaRepository<SendRequest, Long> {
    boolean existsByCustomerRequestKey(CustomerRequestKey customerRequestKey);
}
