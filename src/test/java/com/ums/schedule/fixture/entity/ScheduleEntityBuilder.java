package com.ums.schedule.fixture.entity;

import com.ums.schedule.application.schedule.dto.ScheduleCreateCommand;
import com.ums.schedule.application.schedule.dto.ScheduleUpdateCommand;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.common.code.schedule.ScheduleState;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.state.ScheduleRunningStatus;
import com.ums.schedule.fixture.schedule.SchedulePeriodEntityBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ScheduleEntityBuilder {
    private String name = "scheduleName";
    private ScheduleCyclePolicy cyclePolicy = ScheduleCyclePolicy.realtimeOf();
    private com.ums.schedule.domain.schedule.state.ScheduleStatus scheduleStatus = new ScheduleRunningStatus();
    private ScheduleState status = ScheduleState.RUNNING;
    private SchedulePeriod schedulePeriod = SchedulePeriodEntityBuilder.builder().build();
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private String createdBy = UUID.randomUUID().toString();
    private List<SendRequest> sendRequests = new ArrayList<>();

    public static ScheduleEntityBuilder builder() {
        return new ScheduleEntityBuilder();
    }

    public ScheduleEntityBuilder scheduleName(String scheduleName) {
        this.name = scheduleName;
        return this;
    }

    public ScheduleEntityBuilder schedulePeriod(SchedulePeriod schedulePeriod) {
        this.schedulePeriod = schedulePeriod;
        return this;
    }

    public ScheduleEntityBuilder status(com.ums.schedule.domain.schedule.state.ScheduleStatus status) {
        this.scheduleStatus = status;
        this.status = (scheduleStatus != null) ? status.getCurrentCode() : null;
        return this;
    }

    public ScheduleEntityBuilder cyclePolicy(ScheduleCyclePolicy cyclePolicy) {
        this.cyclePolicy = cyclePolicy;
        return this;
    }

    public ScheduleEntityBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ScheduleEntityBuilder createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ScheduleEntityBuilder sendRequests(SendRequest... sendRequests) {
        this.sendRequests = Arrays.stream(sendRequests).toList();
        return this;
    }

    public ScheduleEntityBuilder lastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public Schedule build() {
        return new Schedule(
                null,
                name,
                cyclePolicy,
                scheduleStatus,
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
