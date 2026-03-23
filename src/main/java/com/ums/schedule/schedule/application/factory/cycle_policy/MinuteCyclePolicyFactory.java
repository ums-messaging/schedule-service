package com.ums.schedule.schedule.application.factory.cycle_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.CyclePolicyValue;
import com.ums.schedule.schedule.domain.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.schedule.code.CycleCdEnum.MINUTE;

@Component
public class MinuteCyclePolicyFactory implements CyclePolicy {

    @Override
    public boolean supports(EnumMapperValue cycleCd) {
        return cycleCd.code().equals(MINUTE.code());
    }

    @Override
    public CyclePolicyValue create(int minute) {
        if(minute < 0 || minute > 59) {
            throw InvalidCycleValueException.toMinute();
        }
        return CyclePolicyValue.of(MINUTE, minute);
    }
}
