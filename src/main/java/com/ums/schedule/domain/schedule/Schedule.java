package com.ums.schedule.domain.schedule;

import com.ums.schedule.domain.schedule.converter.ScheduleStatusConverter;
import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.code.schedule.ScheduleTypeEnum;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import com.ums.schedule.domain.schedule.exception.ScheduleNameRequiredException;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.status.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.status.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @Transient
    private ScheduleStatus scheduleStatus;

    @Column(name = "status", nullable = false, updatable = false,
            columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE' CHECK (STATUS IN ('ACTIVE', 'RUNNING', 'INACTIVE'))")
    @Convert(converter = ScheduleStatusConverter.class)
    private ScheduleStatusEnum status;

    @Embedded
    private SchedulePeriod schedulePeriod;

    @Getter
    @OneToMany(mappedBy = "schedule", cascade = { CascadeType.ALL })
    private List<SendRequest> sendRequests = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    public static Schedule of(String scheduleName, SchedulePeriod schedulePeriod, ScheduleCyclePolicy cyclePolicy) {
        Schedule schedule = new Schedule();
        schedule.applyScheduleName(scheduleName);
        schedule.applySchedulePeriod(schedulePeriod);
        schedule.applyScheduleCyclePolicy(cyclePolicy);
        schedule.changeScheduleStatus(new ScheduleActiveStatus());
        schedule.applyCreatedBy("jang314");
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
        return this.status == ScheduleStatusEnum.RUNNING && this.schedulePeriod.isScheduleWindow();
    }

    private void applyScheduleCyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        validateSchedulePeriodToReservationDate(cyclePolicy);
        this.cyclePolicy = cyclePolicy;
    }

    private void validateSchedulePeriodToReservationDate(ScheduleCyclePolicy cyclePolicy) {
        if(cyclePolicy.getScheduleType() == ScheduleTypeEnum.RESERVATION) {
            LocalDateTime reservationDate = getParseReservationDate(cyclePolicy);
            if(compareTo(reservationDate)) {
                throw InvalidCycleValueException.compareToReservationDate();
            }
        }
    }

    private LocalDateTime getParseReservationDate(ScheduleCyclePolicy cyclePolicy) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return LocalDateTime.parse(cyclePolicy.getPolicyValue().getCycleValue(), formatter);
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
}
