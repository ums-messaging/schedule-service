package com.ums.schedule.domain.schedule.policy.cycle;

import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.domain.exception.schedule.InvalidCycleValueException;
import com.ums.schedule.domain.exception.validation.InvalidDateFormatException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.time.LocalDateTime.now;

public class ReservationPolicyValue implements SchedulePolicyValue {
    private LocalDateTime reservationDt;

    public static ReservationPolicyValue of(String cycleValue) {
        try {
            // 현재 시간으로 부터 60분 기준 이전일 수 없다.
            LocalDateTime reservationDate = LocalDateTime.parse(cycleValue, DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"))
                    .withSecond(0)
                    .withNano(0);
            LocalDateTime compareDate = LocalDateTime.now().plusHours(1).withSecond(0).withNano(0);
            if(reservationDate.isBefore(compareDate)) {
                throw InvalidCycleValueException.toReservationDate();
            }
            return new ReservationPolicyValue(reservationDate);
        } catch (DateTimeParseException e) {
            throw InvalidDateFormatException.ofReservationDate();
        }
    }

    private ReservationPolicyValue (LocalDateTime reservationDt) {
        this.reservationDt = reservationDt;
    }

    @Override
    public ScheduleType getScheduleType() {
        return ScheduleType.RESERVATION;
    }

    @Override
    public CycleCd getCycleCdEnum() {
        return CycleCd.ONCE;
    }

    @Override
    public String getCycleValue() {
        return reservationDt.format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
    }
}
