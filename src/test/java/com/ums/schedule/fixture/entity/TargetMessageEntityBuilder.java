package com.ums.schedule.fixture.entity;

import com.ums.schedule.domain.target.SendTarget;

import java.util.UUID;

public class TargetMessageEntityBuilder {
    private UUID id;
    private SendTarget sendTarget;

    public static TargetMessageEntityBuilder builder() {
        return new TargetMessageEntityBuilder();
    }

    public TargetMessageEntityBuilder sendTarget(SendTarget sendTarget) {
        this.sendTarget = sendTarget;
        return this;
    }
}
