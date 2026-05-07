package com.ums.schedule.repository;

import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.fixture.ScheduleDomainFixture;
import com.ums.schedule.domain.schedule.ScheduleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
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
}
