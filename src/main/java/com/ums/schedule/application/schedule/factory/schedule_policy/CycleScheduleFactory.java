package com.ums.schedule.application.schedule.factory.schedule_policy;

import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.application.schedule.factory.cycle_policy.CyclePolicy;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.policy.cycle.SchedulePolicyValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ums.schedule.domain.schedule.code.ScheduleEnumMapper.CYCLE_CD;
import static com.ums.schedule.domain.schedule.code.ScheduleTypeEnum.CYCLE;


@Component
@RequiredArgsConstructor
public class CycleScheduleFactory implements SchedulePolicyFactory {
    private final EnumMapperFactory factory;
    private final List<CyclePolicy> policyList;
    @Override
    public boolean supports(EnumMapperValue scheduleType) {
        return scheduleType.code().equals(CYCLE.code());
    }

    @Override
    public ScheduleCyclePolicy create(String cycleCd, String cycleValue) {
        EnumMapperValue enumMapperValue = factory.findEnumMapperValue(CYCLE_CD, cycleCd);
        CyclePolicy cyclePolicy = policyList.stream()
                .filter(policy -> policy.supports(enumMapperValue))
                .findFirst()
                .orElseThrow();
        SchedulePolicyValue value = cyclePolicy.create(cycleValue);

        return ScheduleCyclePolicy.of(value);
    }
}
