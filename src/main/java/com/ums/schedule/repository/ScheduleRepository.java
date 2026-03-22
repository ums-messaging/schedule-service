package com.ums.schedule.repository;

import com.ums.schedule.common.code.ScheduleStatusEnum;
import com.ums.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
