package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.SchedulePolicyValue;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.code.schedule.CycleCdEnum.DAY;


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