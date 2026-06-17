package com.ums.schedule.repository;

import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.schedule.state.ScheduleActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleInActiveStatus;
import com.ums.schedule.domain.schedule.state.ScheduleRunningStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SchedulePersistTest {
    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("스케쥴 생성 시 저장된다.")
    void shouldPersistSchedule_whenScheduleCreate() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        entityManager.persist(schedule);

        Long scheduleId = schedule.getId();
        entityManager.flush();
        entityManager.clear();

        Schedule expect = entityManager.find(Schedule.class, scheduleId);
        assertThat(expect.getId()).isNotNull();
    }

    @Nested
    @DisplayName("ScheduleStatus Mapping 테스트")
    class ScheduleStatusMappingTest {
        @Test
        @DisplayName("status에 Active를 입력하면, 조회할 때, Status는 ScheduleActiveStatus를 반환한다.")
        void shouldReturnScheduleActiveStatus_whenStatusCodeIsActive() {
            Schedule schedule = ScheduleTestBuilder.builder().status(new ScheduleActiveStatus()).build();

            entityManager.persist(schedule);
            entityManager.flush();
            Long scheduleId = schedule.getId();
            entityManager.clear();

            Schedule expect = entityManager.find(Schedule.class, scheduleId);

            assertThat(expect.getScheduleStatus()).isInstanceOf(ScheduleActiveStatus.class);
        }

        @Test
        @DisplayName("status에 Running을 입력하면, 조회할 때, Status는 ScheduleRunningStatus를 반환한다.")
        void shouldReturnScheduleRunningStatus_whenStatusCodeIsRunning() {
            Schedule schedule = ScheduleTestBuilder.builder().status(new ScheduleRunningStatus()).build();

            entityManager.persist(schedule);
            entityManager.flush();
            Long scheduleId = schedule.getId();
            entityManager.clear();

            Schedule expect = entityManager.find(Schedule.class, scheduleId);

            assertThat(expect.getScheduleStatus()).isInstanceOf(ScheduleRunningStatus.class);
        }

        @Test
        @DisplayName("status에 InActive를 입력하면, 조회할 때, Status는 ScheduleInActiveStatus를 반환한다.")
        void shouldReturnScheduleInActiveStatus_whenStatusCodeIsInActive() {
            Schedule schedule = ScheduleTestBuilder.builder().status(new ScheduleInActiveStatus()).build();

            entityManager.persist(schedule);
            entityManager.flush();
            Long scheduleId = schedule.getId();
            entityManager.clear();

            Schedule expect = entityManager.find(Schedule.class, scheduleId);

            assertThat(expect.getScheduleStatus()).isInstanceOf(ScheduleInActiveStatus.class);
        }
    }
}
