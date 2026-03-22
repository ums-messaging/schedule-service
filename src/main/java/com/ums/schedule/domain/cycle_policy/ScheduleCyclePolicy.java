package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;
import com.ums.schedule.common.code.ScheduleTypeEnum;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ums.schedule.common.code.CycleCdEnum.*;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
public abstract class ScheduleCyclePolicy {
    private final CycleCdEnum cycleCd;
    private final String cycleValue;

    protected ScheduleCyclePolicy(CycleCdEnum cycleCdEnum, String cycleValue) {
        this.cycleCd = cycleCdEnum;
        this.cycleValue = cycleValue;
        validate();
    }
    protected abstract void validate();
}
