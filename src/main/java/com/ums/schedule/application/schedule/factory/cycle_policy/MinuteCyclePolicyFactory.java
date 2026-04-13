package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.domain.schedule.code.CycleCdEnum.MINUTE;

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
