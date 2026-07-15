package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.schedule.policy.cycle.CyclePolicyValue;
import com.ums.schedule.domain.exception.schedule.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.common.code.schedule.CycleCd.MINUTE;


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
