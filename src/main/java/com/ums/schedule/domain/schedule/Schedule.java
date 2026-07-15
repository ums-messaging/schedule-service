package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.domain.exception.schedule.InvalidCycleValueException;
import com.ums.schedule.domain.exception.schedule.InvalidScheduleStatusException;
import com.ums.schedule.domain.exception.schedule.ScheduleExpiredException;
import com.ums.schedule.domain.exception.schedule.ScheduleNotExecutableException;
import com.ums.schedule.common.code.schedule.ScheduleEvent;
import com.ums.schedule.domain.exception.validation.RequiredException;
import com.ums.schedule.domain.request.converter.ScheduleStatusConverter;
import com.ums.schedule.common.code.schedule.ScheduleStatus;
import com.ums.schedule.common.code.schedule.ScheduleType;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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
    private com.ums.schedule.domain.schedule.state.ScheduleStatus scheduleStatus;

    @Transient
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
        schedule.applyScheduleName(command.scheduleName());
        schedule.applySchedulePeriod(command.scheduleStartAt(), command.scheduleEndAt());
        schedule.changeScheduleStatus(new ScheduleActiveStatus());
        schedule.applyCreatedBy(command.createdBy());
        schedule.applyScheduleCyclePolicy(cyclePolicy);
        return schedule;
    }

    private void applyCreatedBy(String userId) {
        this.createdBy = userId;
    }

    private void applyScheduleName(String scheduleName) {
        if(!StringUtils.hasText(scheduleName.trim())) {
            throw RequiredException.fieldOf("schedule name");
        }
        this.name = scheduleName;
    }

    public boolean availableSchedulePeriodAndStatus() {
        return this.status == ScheduleStatus.RUNNING && this.schedulePeriod.contains(LocalDateTime.now());
    }

    private void applyScheduleCyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        validateSchedulePeriodToReservationDate(cyclePolicy);
        this.cyclePolicy = cyclePolicy;
    }

    private void validateSchedulePeriodToReservationDate(ScheduleCyclePolicy cyclePolicy) {
        if(cyclePolicy.getScheduleType() == ScheduleType.RESERVATION) {
            LocalDateTime reservationDate = getParseReservationDate(cyclePolicy);
            if(!schedulePeriod.contains(reservationDate)) {
                throw InvalidCycleValueException.compareToReservationDate();
            }
        }
    }

    private LocalDateTime getParseReservationDate(ScheduleCyclePolicy cyclePolicy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return LocalDateTime.parse(cyclePolicy.getPolicyValue().getCycleValue(), formatter);
    }

    private void applySchedulePeriod(String scheduleStartAt, String scheduleEndAt) {
        this.schedulePeriod = SchedulePeriod.of(scheduleStartAt, scheduleEndAt);
    }

    private void changeScheduleStatus(com.ums.schedule.domain.schedule.state.ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public void toStatus(ScheduleEvent event) {
        com.ums.schedule.domain.schedule.state.ScheduleStatus toState = this.scheduleStatus.onEvent(event);
        changeScheduleStatus(toState);
    }

    @PrePersist
    public void prePersist() {
        if(this.status == null) {
            changeScheduleStatus(new ScheduleActiveStatus());
        }
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void update(ScheduleUpdateCommand command) {
        validateState();
        validateExpiredPeriod();
        applyScheduleName(command.scheduleName());
    }

    private void validateExpiredPeriod() {
        if(schedulePeriod.isExpired()) {
            if(!this.scheduleStatus.isInActive()) {
                changeScheduleStatus(new ScheduleInActiveStatus());
            }
            throw ScheduleExpiredException.of(id, schedulePeriod);
       }
    }

    private void validateState() {
        if(this.scheduleStatus.isRunning()) {
            throw InvalidScheduleStatusException.invalidStatus();
        }
    }

    public void checkScheduleAvailability() {
        if(this.scheduleStatus.isInActive()) {
            throw ScheduleNotExecutableException.inActiveOf();
        }
        if(schedulePeriod.isExpired()) {
            throw ScheduleExpiredException.of(id, schedulePeriod);
        }
    }

    public void checkExecutableSchedule(LocalDateTime requestedAt) {
        if(!isExecutable(requestedAt)) {
            throw InvalidScheduleStatusException.notRunning();
        }

    }

    private boolean isExecutable(LocalDateTime requestedAt) {
        return schedulePeriod.contains(requestedAt) &&
                this.scheduleStatus.isRunning() &&
                cyclePolicy.satisfiedCyclePolicy(requestedAt);
    }

}
