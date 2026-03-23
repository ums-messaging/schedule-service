package com.ums.schedule.schedule.application.factory.cycle_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.CyclePolicyValue;
import com.ums.schedule.schedule.domain.cycle_policy.SchedulePolicyValue;
import com.ums.schedule.schedule.domain.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.schedule.code.CycleCdEnum.DAY;

@Component
public class DayCyclePolicyFactory implements CyclePolicy {

    @Override
    public boolean supports(EnumMapperValue cycleCd) {
        return cycleCd.code().equals(DAY.code());
    }

    @Override
    public SchedulePolicyValue create(int day) {
        if(day < 1 || day > 31) {
            throw InvalidCycleValueException.toDay();
        }
        return CyclePolicyValue.of(DAY, day);
    }

}