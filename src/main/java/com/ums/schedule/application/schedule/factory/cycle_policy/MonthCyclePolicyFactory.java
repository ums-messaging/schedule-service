package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.code.schedule.CycleCdEnum.MONTH;

@Component
public class MonthCyclePolicyFactory implements CyclePolicy {

    @Override
    public boolean supports(EnumMapperValue cycleCd) {
        return cycleCd.code().equals(MONTH.code());
    }

    @Override
    public CyclePolicyValue create(int month) {
        if(month < 1 || month > 12) {
            throw InvalidCycleValueException.toMonth();
        }
        return CyclePolicyValue.of(MONTH, month);
    }
}
