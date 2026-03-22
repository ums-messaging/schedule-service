package com.ums.schedule.domain.cycle_policy;

import com.ums.schedule.common.code.CycleCdEnum;

public class RealtimeCyclePolicy extends ScheduleCyclePolicy {

    protected RealtimeCyclePolicy() {
        super(CycleCdEnum.ALWAYS, null);
    }

    @Override
    protected void validate() {

    }
}
