package com.ums.schedule.application.schedule.factory.cycle_policy;

import com.ums.schedule.common.code.api.ScheduleErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.schedule.exception.InvalidScheduleCyclePolicyException;
import com.ums.schedule.domain.schedule.policy.cycle.CyclePolicyValue;
import com.ums.schedule.domain.schedule.policy.cycle.SchedulePolicyValue;
import org.springframework.stereotype.Component;

import static com.ums.schedule.common.code.schedule.CycleCd.DAY;


@Component
public class DayCyclePolicyFactory implements CyclePolicy {

    @Override
    public boolean supports(EnumMapperValue cycleCd) {
        return cycleCd.code().equals(DAY.code());
    }

    @Override
    public SchedulePolicyValue create(int day) {
        if(day < 1 || day > 31) {
            throw InvalidScheduleCyclePolicyException.of(ScheduleErrorCode.CYCLE_VALUE_NOT_DAY);
        }
        return CyclePolicyValue.of(DAY, day);
    }

}