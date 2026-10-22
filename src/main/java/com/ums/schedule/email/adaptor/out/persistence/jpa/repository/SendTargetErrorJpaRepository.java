package com.ums.schedule.email.adaptor.out.persistence.jpa.repository;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendTargetErrorJpaRepository extends JpaRepository<SendTargetErrorEntity, Long> {
    long countBySendRequestId(Long sendRequestId);
}
