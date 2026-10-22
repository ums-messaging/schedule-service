package com.ums.schedule.email.adaptor.out.persistence.jpa.repository;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SendTargetRequestJpaRepository extends JpaRepository<SendTargetRequestEntity, Long> {
}
