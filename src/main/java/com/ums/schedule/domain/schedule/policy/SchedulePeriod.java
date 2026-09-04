package com.ums.schedule.domain.schedule.policy;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SchedulePeriod {
    @Column(name = "schedule_start_at", nullable = false)
    private LocalDate scheduleStartAt;
    @Column(name = "schedule_end_at", nullable = false)
    private LocalDate scheduleEndAt;

    public static SchedulePeriod of(String scheduleStartAt, String scheduleEndAt) {
        SchedulePeriod period = new SchedulePeriod(scheduleStartAt, scheduleEndAt);
        validateStartAt(period.getScheduleStartAt());
        validateStartAtAndEndAt(period.getScheduleStartAt(), period.getScheduleEndAt());
        return period;
    }

    protected SchedulePeriod(String scheduleStartAt, String scheduleEndAt) {
        this.scheduleStartAt = parseToLocalDate(scheduleStartAt);
        this.scheduleEndAt = parseToLocalDate(scheduleEndAt);
    }

    private LocalDate parseToLocalDate(String scheduleDate) {
        try {
            return LocalDate.parse(scheduleDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw InvalidSchedulePeriodException.of(ScheduleErrorCode.INVALID_PERIOD_FORMAT);
        }
    }
    private static void validateStartAtAndEndAt(LocalDate scheduleStartAt, LocalDate scheduleEndAt) {
        LocalDate minScheduleEndAt = scheduleStartAt
                .plusDays(1);

        if(scheduleEndAt.isBefore(minScheduleEndAt)) {
            throw InvalidSchedulePeriodException.of(ScheduleErrorCode.INVALID_PERIOD_RANGE);
        }
    }

    private static void validateStartAt(LocalDate scheduleStartAt) {
        LocalDate currentTime = LocalDate.now();
        if(scheduleStartAt.isBefore(currentTime)) {
            throw InvalidSchedulePeriodException.of(ScheduleErrorCode.START_AT_BEFORE_NOW);
        }
    }

    public boolean contains(LocalDateTime dateTime) {
        return compareStartAt(dateTime) && compareEndAt(dateTime);
    }

    private boolean compareStartAt(LocalDateTime dateTime) {
        return dateTime.toLocalDate().isEqual(scheduleStartAt) || dateTime.toLocalDate().isAfter(scheduleStartAt);
    }

    private boolean compareEndAt(LocalDateTime dateTime) {
        return dateTime.toLocalDate().isEqual(scheduleEndAt) || dateTime.toLocalDate().isBefore(scheduleEndAt);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(scheduleEndAt);
    }

}
