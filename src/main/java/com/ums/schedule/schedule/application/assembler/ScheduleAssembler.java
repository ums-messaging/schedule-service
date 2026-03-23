package com.ums.schedule.schedule.application.assembler;

import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.schedule.application.factory.schedule_policy.SchedulePolicyFactory;
import com.ums.schedule.schedule.domain.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.schedule.domain.period.SchedulePeriod;
import com.ums.schedule.schedule.application.command.ScheduleCreateCommand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ums.schedule.schedule.code.ScheduleEnumMapper.SCHEDULE_TYPE;

@Component
@RequiredArgsConstructor
public class ScheduleAssembler {
    private final EnumMapperFactory enumMapperFactory;
    private final List<SchedulePolicyFactory> schedulePolicyFactories;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");

    public Schedule createSchedule(ScheduleCreateCommand command) {
        SchedulePeriod schedulePeriod = parseToSchedulePeriod(command.scheduleStartAt(), command.scheduleEndAt());
        EnumMapperValue scheduleType = enumMapperFactory.findEnumMapperValue(SCHEDULE_TYPE, command.scheduleType());
        ScheduleCyclePolicy cyclePolicy = resolveCyclePolicy(command, scheduleType);
        return Schedule.of(command.scheduleName(), schedulePeriod, cyclePolicy);
    }

    private ScheduleCyclePolicy resolveCyclePolicy(ScheduleCreateCommand command, EnumMapperValue scheduleType) {
        return schedulePolicyFactories.stream()
                .filter(factory -> factory.supports(scheduleType))
                .map(factory -> factory.create(command.cycleCd(), command.cycleValue()))
                .findFirst()
                .orElseThrow();
    }

    private SchedulePeriod parseToSchedulePeriod(String startAt, String endAt) {
        LocalDateTime scheduleStartAt = LocalDateTime.parse(startAt, formatter);
        LocalDateTime scheduleEndAt = LocalDateTime.parse(endAt, formatter);

        return SchedulePeriod.of(scheduleStartAt, scheduleEndAt);
    }


}
