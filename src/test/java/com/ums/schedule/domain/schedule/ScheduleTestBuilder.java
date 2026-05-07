package com.ums.schedule.domain.schedule;

import com.ums.schedule.application.schedule.factory.cycle_policy.CyclePolicy;
import com.ums.schedule.code.schedule.ScheduleStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.period.SchedulePeriodTestBuilder;
import com.ums.schedule.domain.schedule.status.ScheduleRunningStatus;
import com.ums.schedule.domain.schedule.status.ScheduleStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleTestBuilder {
    private String name = "scheduleName";
    private ScheduleCyclePolicy cyclePolicy = ScheduleCyclePolicy.realtimeOf();
    private ScheduleStatus scheduleStatus = new ScheduleRunningStatus();
    private ScheduleStatusEnum status = ScheduleStatusEnum.RUNNING;
    private SchedulePeriod schedulePeriod = SchedulePeriodTestBuilder.builder().build();
    private List<SendRequest> sendRequests = new ArrayList<>();
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
        this.status = (scheduleStatus != null) ? status.currentScheduleStatus() : null;
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
                sendRequests,
                createdAt,
                lastUpdatedAt,
                createdBy
        );
    }
}
