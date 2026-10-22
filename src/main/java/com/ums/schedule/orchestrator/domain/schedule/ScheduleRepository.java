package com.ums.schedule.orchestrator.domain.schedule;

import java.util.Optional;

public interface ScheduleRepository {
    Optional<Schedule> findById(Long id);
}
