package com.ums.schedule.schedule.application.service;

import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.schedule.domain.period.SchedulePeriod;
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
        LocalDateTime now = LocalDateTime.now();
        SchedulePeriod schedulePeriod = schedule.getSchedulePeriod();
        schedulePeriod.compareToNow(now);
        return schedule;
    }
}
