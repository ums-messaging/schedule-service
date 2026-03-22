package com.ums.schedule.domain;

import com.ums.schedule.domain.exception.InvalidSchedulePeriodException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePeriod {
    @Column(name = "schedule_start_at", nullable = false)
    private LocalDateTime scheduleStartAt;
    @Column(name = "schedule_end_at", nullable = false)
    private LocalDateTime scheduleEndAt;

    public static SchedulePeriod of(LocalDateTime scheduleStartAt, LocalDateTime scheduleEndAt) {
        SchedulePeriod period = new SchedulePeriod(scheduleStartAt, scheduleEndAt);
        validateStartAt(scheduleStartAt);
        validateStartAtAndEndAt(scheduleStartAt, scheduleEndAt);
        return period;
    }

    private static void validateStartAtAndEndAt(LocalDateTime scheduleStartAt, LocalDateTime scheduleEndAt) {
        LocalDateTime minScheduleEndAt = scheduleStartAt
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay();

        if(scheduleEndAt.isBefore(minScheduleEndAt) || scheduleEndAt.isEqual(minScheduleEndAt)) {
            throw InvalidSchedulePeriodException.endAtAfterStartAt();
        }
    }

    private static void validateStartAt(LocalDateTime scheduleStartAt) {
        LocalDateTime currentTime = LocalDateTime.now()
                .toLocalDate()
                .atStartOfDay();
        if(scheduleStartAt.isBefore(currentTime)) {
            throw InvalidSchedulePeriodException.startAtBeforeNow();
        }
    }

}
