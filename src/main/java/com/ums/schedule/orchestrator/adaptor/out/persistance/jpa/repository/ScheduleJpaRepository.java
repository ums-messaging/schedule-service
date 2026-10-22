package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {
}
