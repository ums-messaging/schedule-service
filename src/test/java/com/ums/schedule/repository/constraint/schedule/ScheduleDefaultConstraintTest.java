package com.ums.schedule.repository.constraint.schedule;

import com.ums.schedule.common.code.schedule.ScheduleStatus;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ScheduleDefaultConstraintTest {
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("status의 기본값은 ACTIVE이다.")
    void shouldReturnStatusIsActive_whenScheduleStatusIsNull() {
        Schedule schedule = ScheduleEntityBuilder.builder()
                .status(null)
                .build();

        entityManager.persist(schedule);
        entityManager.flush();
        entityManager.clear();

        Schedule expect = entityManager.createQuery("select s from Schedule s", Schedule.class)
                .getResultList()
                .get(0);

        assertThat(expect.getStatus()).isEqualTo(ScheduleStatus.ACTIVE);
    }

    @Test
    @DisplayName("created_at의 기본 값은 현재 시각이다.")
    void shouldReturnCreatedAtIsCurrentTime_whenCreatedAtIsNull() {
        Schedule schedule = ScheduleEntityBuilder.builder().createdAt(null).build();

        entityManager.persist(schedule);
        entityManager.flush();
        entityManager.clear();

        Schedule expect = entityManager.createQuery("select s from Schedule s", Schedule.class)
                .getResultList()
                .get(0);

        assertThat(expect.getCreatedAt().toLocalDate().atStartOfDay())
                .isEqualTo(LocalDate.now().atStartOfDay());
    }
}
