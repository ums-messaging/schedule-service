package com.ums.schedule.orchestrator.domain.schedule;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.schedule.CycleCd;
import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.ScheduleEntity;
import com.ums.schedule.orchestrator.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.orchestrator.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.orchestrator.domain.event.converter.ScheduleStatusConverter;
import com.ums.schedule.orchestrator.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import com.ums.schedule.orchestrator.domain.schedule.exception.InvalidSchedulePeriodException;
import com.ums.schedule.orchestrator.domain.schedule.exception.InvalidScheduleStateException;
import com.ums.schedule.orchestrator.domain.schedule.exception.SchedulePolicyViolationException;
import com.ums.schedule.orchestrator.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.orchestrator.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.orchestrator.domain.schedule.state.ScheduleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Schedule {
    private Long id;
    private String name;

    private ScheduleCyclePolicy cyclePolicy;

    private ScheduleStatus status;

    private SchedulePeriod schedulePeriod;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private String createdBy;

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
    public boolean isRealtime() {
        return cyclePolicy.getScheduleType() == ScheduleType.REALTIME;
    }

    public static Schedule fromEntity(ScheduleEntity entity) {
        return new Schedule(entity);
    }
    private Schedule(ScheduleEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.schedulePeriod = new SchedulePeriod(entity.getScheduleStartAt(), entity.getScheduleEndAt());
        this.cyclePolicy = new ScheduleCyclePolicy(ScheduleType.valueOf(entity.getScheduleType()),
                CycleCd.valueOf(entity.getCycleCd()), entity.getCycleValue(), null);
        this.status = ScheduleState.valueOf(entity.getStatus()).createStatus();
        this.createdAt = entity.getCreatedAt();
        this.createdBy = entity.getCreatedBy();

    }

    public String getSendRequestedAt() {
        ScheduleCyclePolicy cyclePolicy = this.cyclePolicy;
        cyclePolicy.getCycleValue();
        if(isRealtime()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
            return formatter.format(LocalDateTime.now());
        }
        return cyclePolicy.getCycleValue();
    }
}
