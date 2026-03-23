package com.ums.schedule.schedule.application.factory.cycle_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.CyclePolicyValue;
import com.ums.schedule.schedule.domain.cycle_policy.SchedulePolicyValue;
import com.ums.schedule.schedule.domain.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.schedule.code.CycleCdEnum.HOUR;

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
