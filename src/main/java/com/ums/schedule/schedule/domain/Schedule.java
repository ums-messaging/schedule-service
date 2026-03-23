package com.ums.schedule.schedule.domain;

import com.ums.schedule.domain.ScheduleRestrictPolicy;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.schedule.code.ScheduleStatusEnum;
import com.ums.schedule.schedule.code.ScheduleTypeEnum;
import com.ums.schedule.schedule.domain.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.schedule.domain.exception.InvalidCycleValueException;
import com.ums.schedule.schedule.domain.period.SchedulePeriod;
import com.ums.schedule.schedule.domain.status.ScheduleActiveStatus;
import com.ums.schedule.schedule.domain.status.ScheduleStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.ums.schedule.schedule.code.ScheduleTypeEnum.RESERVATION;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
public class Schedule {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_name", nullable = false)
    private String name;

    @Column(name = "schedule_type", nullable = false)
    private ScheduleTypeEnum type;

    @Embedded
    private ScheduleCyclePolicy cyclePolicy;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatusEnum status;

    @Embedded
    private SchedulePeriod schedulePeriod;

    @Getter
    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.PERSIST})
    private List<ScheduleRestrictPolicy> restrictPolicies = new ArrayList<>();

    @Transient
    private ScheduleStatus scheduleStatus;

    @Getter
    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.ALL })
    private List<SendRequest> sendRequests = new ArrayList<>();

    public static Schedule of(String scheduleName, SchedulePeriod schedulePeriod, ScheduleCyclePolicy cyclePolicy) {
        Schedule schedule = new Schedule(scheduleName);
        schedule.applySchedulePeriod(schedulePeriod);
        schedule.applyScheduleCyclePolicy(cyclePolicy);
        schedule.changeScheduleStatus(new ScheduleActiveStatus());
        return schedule;
    }

    private void applyScheduleCyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        validateSchedulePeriodToReservationDate(cyclePolicy);
        this.cyclePolicy = cyclePolicy;
    }

    private void validateSchedulePeriodToReservationDate(ScheduleCyclePolicy cyclePolicy) {
        if(cyclePolicy.scheduleType() == RESERVATION) {
            LocalDateTime reservationDate = getParseReservationDate(cyclePolicy);
            if(compareTo(reservationDate)) {
                throw InvalidCycleValueException.compareToReservationDate();
            }
        }
    }

    private LocalDateTime getParseReservationDate(ScheduleCyclePolicy cyclePolicy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return LocalDateTime.parse(cyclePolicy.policyValue().getCycleValue(), formatter);
    }

    private boolean compareTo(LocalDateTime reservationDate) {
        return reservationDate.isBefore(schedulePeriod.getScheduleStartAt()) || reservationDate.isAfter(schedulePeriod.getScheduleEndAt());
    }

    private void applySchedulePeriod(SchedulePeriod schedulePeriod) {
        this.schedulePeriod = schedulePeriod;
    }

    private Schedule(String scheduleName) {
        this.name = scheduleName;
    }

    public void changeScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
        this.status = scheduleStatus.currentScheduleStatus();
    }
}
