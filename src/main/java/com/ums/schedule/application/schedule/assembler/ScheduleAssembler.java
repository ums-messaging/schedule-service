package com.ums.schedule.application.schedule.assembler;

import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.application.schedule.factory.schedule_policy.SchedulePolicyFactory;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.adapter.api.schedule.ScheduleCreateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ums.schedule.common.code.schedule.ScheduleEnumMapper.SCHEDULE_TYPE;

@Component
@RequiredArgsConstructor
public class ScheduleAssembler {
    private final EnumMapperFactory enumMapperFactory;
    private final List<SchedulePolicyFactory> schedulePolicyFactories;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

    public Schedule createSchedule(ScheduleCreateRequest command) {
        EnumMapperValue scheduleType = enumMapperFactory.findEnumMapperValue(SCHEDULE_TYPE, command.scheduleType());
        ScheduleCyclePolicy cyclePolicy = resolveCyclePolicy(command, scheduleType);
        return null;
    }

    private ScheduleCyclePolicy resolveCyclePolicy(ScheduleCreateRequest command, EnumMapperValue scheduleType) {
        return schedulePolicyFactories.stream()
                .filter(factory -> factory.supports(scheduleType))
                .map(factory -> factory.create(command.cycleCd(), command.cycleValue()))
                .findFirst()
                .orElseThrow();
    }

}
