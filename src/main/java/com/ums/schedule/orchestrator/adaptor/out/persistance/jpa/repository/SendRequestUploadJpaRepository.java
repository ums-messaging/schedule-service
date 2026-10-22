package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SendRequestUploadJpaRepository extends JpaRepository<SendRequestUploadEntity, Long> {
    Optional<SendRequestUploadEntity> findByCustomerIdAndObjectKeyEndsWith(String customerId, String objectKey);
}
