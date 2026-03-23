package com.ums.schedule.schedule.application.factory.cycle_policy;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.cycle_policy.CyclePolicyValue;
import com.ums.schedule.schedule.domain.exception.InvalidCycleValueException;
import org.springframework.stereotype.Component;

import static com.ums.schedule.schedule.code.CycleCdEnum.MONTH;
import static java.util.Arrays.stream;

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
