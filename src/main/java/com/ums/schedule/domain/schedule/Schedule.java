package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.domain.schedule.converter.ScheduleStatusConverter;
import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import com.ums.schedule.domain.schedule.exception.InvalidSchedulePeriodException;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleStatusException;
import com.ums.schedule.domain.schedule.exception.ScheduleNameRequiredException;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.state.StatusState;
import com.ums.schedule.domain.state.schedule.ScheduleActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleInActiveStatus;
import com.ums.schedule.domain.state.schedule.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
    private ScheduleStatus scheduleStatus;

    @Transient
    private ScheduleStatusEnum status;

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
            throw ScheduleNameRequiredException.of();
        }
        this.name = scheduleName;
    }

    public boolean availableSchedulePeriodAndStatus() {
        return this.status == ScheduleStatusEnum.RUNNING && this.schedulePeriod.isScheduleWindow(LocalDateTime.now());
    }

    private void applyScheduleCyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        validateSchedulePeriodToReservationDate(cyclePolicy);
        this.cyclePolicy = cyclePolicy;
    }

    private void validateSchedulePeriodToReservationDate(ScheduleCyclePolicy cyclePolicy) {
        if(cyclePolicy.getScheduleType() == ScheduleTypeEnum.RESERVATION) {
            LocalDateTime reservationDate = getParseReservationDate(cyclePolicy);
            if(!schedulePeriod.isScheduleWindow(reservationDate)) {
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

    private void changeScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public void toStatus(ScheduleEventEnum event) {
        ScheduleStatus toState = this.scheduleStatus.onEvent(event);
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
            throw InvalidSchedulePeriodException.expired();
       }
    }

    private void validateState() {
        if(this.scheduleStatus.isRunning()) {
            throw InvalidScheduleStatusException.invalidStatus();
        }
    }

}
