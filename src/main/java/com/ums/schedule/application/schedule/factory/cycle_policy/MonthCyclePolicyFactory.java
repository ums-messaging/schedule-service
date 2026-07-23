package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import com.ums.schedule.domain.schedule.policy.cycle.CyclePolicyValue;
import org.springframework.stereotype.Component;

import static com.ums.schedule.common.code.schedule.CycleCd.MONTH;

@Component
public class MonthCyclePolicyFactory implements CyclePolicy {

    @Override
    public boolean supports(EnumMapperValue cycleCd) {
        return cycleCd.code().equals(MONTH.code());
    }

    @Override
    public CyclePolicyValue create(int month) {
        if(month < 1 || month > 12) {
            throw InvalidScheduleCyclePolicyException.of(ScheduleErrorCode.CYCLE_VALUE_NOT_MONTH);

        }
        return CyclePolicyValue.of(MONTH, month);
    }
}
