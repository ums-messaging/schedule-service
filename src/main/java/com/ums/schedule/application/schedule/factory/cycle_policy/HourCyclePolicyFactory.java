package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.schedule.policy.cycle.CyclePolicyValue;
import com.ums.schedule.domain.schedule.policy.cycle.SchedulePolicyValue;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.domain.schedule.code.CycleCdEnum.HOUR;


@Component
public class HourCyclePolicyFactory implements CyclePolicy {

    @Override
    public boolean supports(EnumMapperValue cycleCd) {
        return cycleCd.code().equals(HOUR.code());
    }

    @Override
    public SchedulePolicyValue create(int hour) {
        if(hour < 0 || hour > 12) {
            throw InvalidCycleValueException.toHour();
        }
        return CyclePolicyValue.of(HOUR, hour);
    }
}
