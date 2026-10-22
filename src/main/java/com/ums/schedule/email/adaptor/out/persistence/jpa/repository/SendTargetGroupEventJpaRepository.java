package com.ums.schedule.email.adaptor.out.persistence.jpa.repository;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetGroupEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendTargetGroupEventJpaRepository extends JpaRepository<SendTargetGroupEventEntity, Long> {
}