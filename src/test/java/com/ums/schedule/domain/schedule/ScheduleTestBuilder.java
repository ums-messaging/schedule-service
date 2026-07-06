package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.domain.schedule.code.ScheduleStatusEnum;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.policy.SchedulePeriodTestBuilder;
import com.ums.schedule.domain.schedule.state.ScheduleRunningStatus;
import com.ums.schedule.domain.schedule.state.ScheduleStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ScheduleTestBuilder {
    private String name = "scheduleName";
    private ScheduleCyclePolicy cyclePolicy = ScheduleCyclePolicy.realtimeOf();
    private ScheduleStatus scheduleStatus = new ScheduleRunningStatus();
    private ScheduleStatusEnum status = ScheduleStatusEnum.RUNNING;
    private SchedulePeriod schedulePeriod = SchedulePeriodTestBuilder.builder().build();
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private String createdBy = UUID.randomUUID().toString();

    public static ScheduleTestBuilder builder() {
        return new ScheduleTestBuilder();
    }

    public ScheduleTestBuilder scheduleName(String scheduleName) {
        this.name = scheduleName;
        return this;
    }

    public ScheduleTestBuilder schedulePeriod(SchedulePeriod schedulePeriod) {
        this.schedulePeriod = schedulePeriod;
        return this;
    }

    public ScheduleTestBuilder status(ScheduleStatus status) {
        this.scheduleStatus = status;
        this.status = (scheduleStatus != null) ? status.getCurrentCode() : null;
        return this;
    }

    public ScheduleTestBuilder cyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        this.cyclePolicy = cyclePolicy;
        return this;
    }

    public ScheduleTestBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ScheduleTestBuilder createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ScheduleTestBuilder lastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public Schedule build() {
        return new Schedule(
                null,
                name,
                cyclePolicy,
                scheduleStatus,
                status,
                schedulePeriod,
                createdAt,
                lastUpdatedAt,
                createdBy
        );
    }

    public ScheduleCreateCommand toCommand() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new ScheduleCreateCommand(
                this.name,
                this.schedulePeriod.getScheduleStartAt().format(formatter),
                this.schedulePeriod.getScheduleEndAt().format(formatter),
                this.createdBy
        );
    }

    public ScheduleUpdateCommand toUpdateCommand() {
        return new ScheduleUpdateCommand(this.name);
    }
}
