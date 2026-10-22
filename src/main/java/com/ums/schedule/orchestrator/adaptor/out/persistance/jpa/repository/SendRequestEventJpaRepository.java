package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestEventJpaRepository extends JpaRepository<SendRequestEntity, Long> {
    boolean existsByCustomerIdAndCustomerKey(String customerId, String customerKey);
}
