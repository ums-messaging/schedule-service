package com.ums.schedule.domain.schedule;

import com.ums.schedule.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
