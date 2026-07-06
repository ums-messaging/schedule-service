package com.ums.schedule.application.schedule;

import com.ums.schedule.domain.schedule.ScheduleJpaRepository;
import com.ums.schedule.domain.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleJpaRepository scheduleRepository;

    public Schedule findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        return schedule;
    }
}
