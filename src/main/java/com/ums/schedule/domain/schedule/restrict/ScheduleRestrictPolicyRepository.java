package com.ums.schedule.domain.schedule.restrict;


import com.ums.schedule.domain.schedule.restrict.ScheduleRestrictPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRestrictPolicyRepository extends JpaRepository<ScheduleRestrictPolicy, Long> {
}
