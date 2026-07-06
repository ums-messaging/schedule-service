package com.ums.schedule.repository.constraint.schedule;

import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.policy.cycle.ScheduleCyclePolicyTestBuilder;
import com.ums.schedule.domain.schedule.policy.SchedulePeriod;
import com.ums.schedule.domain.schedule.policy.SchedulePeriodTestBuilder;
import com.ums.schedule.fixture.field.ScheduleField;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class ScheduleNotNullConstraintTest {
    private final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired private EntityManager entityManager;


    @Test
    @DisplayName("schedule_name 필드는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenScheduleNameIsNull() {
        Schedule schedule = ScheduleTestBuilder.builder().scheduleName(null).build();
        ScheduleField field = ScheduleField.SCHEDULE_NAME;

        assertThatThrownBy(() -> entityManager.persist(schedule))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.value());
    }

    @Test
    @DisplayName("schedule_type 필드는 NULL을 허용하지 않는다.")
    void shouldFail_whenScheduleTypeIsNull() {
        ScheduleCyclePolicy policy = ScheduleCyclePolicyTestBuilder.builder()
                .scheduleType(null)
                .build();

        Schedule schedule = ScheduleTestBuilder.builder().cyclePolicy(policy).build();
        ScheduleField field = ScheduleField.SCHEDULE_TYPE;

        assertThatThrownBy(() -> entityManager.persist(schedule))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("cycle_cd는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenCycleCyIsNull(){
        ScheduleCyclePolicy policy = ScheduleCyclePolicyTestBuilder.builder()
                .cycleCd(null)
                .build();

        Schedule schedule = ScheduleTestBuilder.builder().cyclePolicy(policy).build();
        ScheduleField field = ScheduleField.CYCLE_CD;


        assertThatThrownBy(() -> entityManager.persist(schedule))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("schedule_start_at은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenScheduleStartAtIsNull() {
        SchedulePeriod period = SchedulePeriodTestBuilder.builder().scheduleStartAt(null).build();
        Schedule schedule = ScheduleTestBuilder.builder().schedulePeriod(period).build();
        ScheduleField field = ScheduleField.SCHEDULE_START_AT;

        assertThatThrownBy(() -> entityManager.persist(schedule))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("schedule_end_at은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenScheduleEndAtIsNull() {
        SchedulePeriod period = SchedulePeriodTestBuilder.builder().scheduleEndAt(null).build();
        Schedule schedule = ScheduleTestBuilder.builder().schedulePeriod(period).build();
        ScheduleField field = ScheduleField.SCHEDULE_END_AT;

        assertThatThrownBy(() -> entityManager.persist(schedule))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("created_by는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenCreatedByIsNull() {
        Schedule schedule = ScheduleTestBuilder.builder().createdBy(null).build();
        ScheduleField field = ScheduleField.CREATED_BY;

        ReflectionTestUtils.setField(schedule, field.value(), null);

        assertThatThrownBy(() -> entityManager.persist(schedule))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }
}
