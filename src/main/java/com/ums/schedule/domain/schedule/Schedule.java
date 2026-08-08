package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.domain.request.converter.ScheduleStatusConverter;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStateException;
import com.ums.schedule.domain.schedule.exception.SchedulePolicyViolationException;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_name", nullable = false, length = 1000)
    private String name;

    @Embedded
    private ScheduleCyclePolicy cyclePolicy;

    @Column(name = "status", nullable = false, updatable = false)
    @Convert(converter = ScheduleStatusConverter.class)
    private ScheduleStatus status;

    @Embedded
    private SchedulePeriod schedulePeriod;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    public static Schedule of(ScheduleCreateCommand command, ScheduleCyclePolicy cyclePolicy) {
        Schedule schedule = new Schedule();
        schedule.assignScheduleName(command.scheduleName());
        schedule.assignSchedulePeriod(command.scheduleStartAt(), command.scheduleEndAt());
        schedule.assignCreatedBy(command.createdBy());
        schedule.assignScheduleCyclePolicy(cyclePolicy);
        schedule.changeScheduleStatus(new ScheduleActiveStatus());
        return schedule;
    }

    private void assignCreatedBy(String userId) {
        this.createdBy = userId;
    }

    private void assignScheduleName(String scheduleName) {
        this.name = scheduleName;
    }

    public boolean availableSchedulePeriodAndStatus() {
        return this.status.getCurrentCode() == ScheduleState.RUNNING && this.schedulePeriod.contains(LocalDateTime.now());
    }

    private void assignScheduleCyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        validateSchedulePeriodToReservationDate(cyclePolicy);
        this.cyclePolicy = cyclePolicy;
    }

    private void validateSchedulePeriodToReservationDate(ScheduleCyclePolicy cyclePolicy) {
        if(cyclePolicy.getScheduleType() == ScheduleType.RESERVATION) {
            LocalDateTime reservationDate = getParseReservationDate(cyclePolicy);
            if(!schedulePeriod.contains(reservationDate)) {
                throw InvalidScheduleCyclePolicyException.of(ScheduleErrorCode.NOT_IN_PERIOD_RESERVATION_DATE);
            }
        }
    }

    private LocalDateTime getParseReservationDate(ScheduleCyclePolicy cyclePolicy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return LocalDateTime.parse(cyclePolicy.getPolicyValue().getCycleValue(), formatter);
    }

    private void assignSchedulePeriod(String scheduleStartAt, String scheduleEndAt) {
        this.schedulePeriod = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);
    }

    private void changeScheduleStatus(ScheduleStatus scheduleStatus) {
        this.status = scheduleStatus;
    }

    public void toStatus(ScheduleEvent event) {
        ScheduleStatus toState = this.status.onEvent(event);
        changeScheduleStatus(toState);
    }

    public void update(ScheduleUpdateCommand command) {
        validateState();
        validateExpiredPeriod();
        assignScheduleName(command.scheduleName());
    }

    private void validateExpiredPeriod() {
        if(schedulePeriod.isExpired()) {
            if(!this.status.isInActive()) {
                changeScheduleStatus(new ScheduleInActiveStatus());
            }
            throw InvalidSchedulePeriodException.of(id, ScheduleErrorCode.EXPIRED_SCHEDULE);
       }
    }

    private void validateState() {
        if(this.status.isRunning()) {
            throw InvalidScheduleStateException.of(id, ScheduleState.RUNNING);
        }
    }

    public void checkScheduleAvailability() {
        if(this.status.isInActive()) {
            throw InvalidScheduleStateException.of(id, ScheduleState.INACTIVE);
        }
        if(schedulePeriod.isExpired()) {
            throw InvalidSchedulePeriodException.of(ScheduleErrorCode.EXPIRED_SCHEDULE, id);
        }
    }

    public void checkExecutableSchedule(LocalDateTime requestedAt) {
        if(!isExecutable(requestedAt)) {
            throw SchedulePolicyViolationException.of(id, ScheduleErrorCode.NOT_EXECUTE_SCHEDULE);
        }
    }

    private boolean isExecutable(LocalDateTime requestedAt) {
        return schedulePeriod.contains(requestedAt) &&
                this.status.isRunning() &&
                cyclePolicy.satisfiedCyclePolicy(requestedAt);
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if(this.status == null) {
            changeScheduleStatus(new ScheduleActiveStatus());
        }
        this.createdAt = LocalDateTime.now();
    }
}
