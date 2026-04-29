package com.ums.schedule.domain.schedule;

import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import com.ums.schedule.domain.schedule.restrict.ScheduleRestrictPolicy;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.status.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_name", nullable = false)
    private String name;

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

    private Schedule(String scheduleName) {
        this.name = scheduleName;
    }

    public static Schedule of(String scheduleName, SchedulePeriod schedulePeriod, ScheduleCyclePolicy cyclePolicy) {
        Schedule schedule = new Schedule(scheduleName);
        schedule.applySchedulePeriod(schedulePeriod);
        schedule.applyScheduleCyclePolicy(cyclePolicy);
        schedule.changeScheduleStatus(new ScheduleActiveStatus());
        return schedule;
    }

    public boolean availableSchedulePeriodAndStatus() {
        return this.scheduleStatus.isRunning() && this.schedulePeriod.isScheduleWindow();
    }

    private void applyScheduleCyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        validateSchedulePeriodToReservationDate(cyclePolicy);
        this.cyclePolicy = cyclePolicy;
    }

    private void validateSchedulePeriodToReservationDate(ScheduleCyclePolicy cyclePolicy) {
        if(cyclePolicy.scheduleType() == ScheduleTypeEnum.RESERVATION) {
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

    private void changeScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
        this.status = scheduleStatus.currentScheduleStatus();
    }

    public void toRunning() {
        ScheduleStatus status = this.scheduleStatus.toRunning();
        changeScheduleStatus(status);
    }

    public void toInActive() {
        ScheduleStatus status = this.scheduleStatus.toInActive();
        changeScheduleStatus(status);
    }

    public void toActive() {
        ScheduleStatus status = this.scheduleStatus.toActive();
        changeScheduleStatus(status);
    }
}
