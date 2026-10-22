package com.ums.schedule.orchestrator.application.schedule.assembler;

import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.ScheduleEntity;
import com.ums.schedule.orchestrator.application.schedule.factory.schedule_policy.SchedulePolicyFactory;
import com.ums.schedule.orchestrator.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.orchestrator.adaptor.in.rest.schedule.request.ScheduleCreateRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ums.schedule.common.code.schedule.ScheduleCode.SCHEDULE_TYPE;

@Component
@RequiredArgsConstructor
public class ScheduleAssembler {
    private final EnumMapperFactory enumMapperFactory;
    private final List<SchedulePolicyFactory> schedulePolicyFactories;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

    public ScheduleEntity createSchedule(ScheduleCreateRequest command) {
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
