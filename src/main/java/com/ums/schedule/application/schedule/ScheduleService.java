package com.ums.schedule.application.schedule;

import com.ums.schedule.domain.schedule.ScheduleRepository;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Schedule findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        return schedule;
    }
}
