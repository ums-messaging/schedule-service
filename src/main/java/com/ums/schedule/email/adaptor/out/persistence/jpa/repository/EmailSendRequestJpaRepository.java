package com.ums.schedule.email.adaptor.out.persistence.jpa.repository;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailSendRequestJpaRepository extends JpaRepository<EmailSendRequestEntity, Long> {
    Optional<EmailSendRequestEntity> findByRequestId(Long requestId);
    Optional<EmailSendRequestEntity> findByIdAndStatus(Long id, String status);
}
