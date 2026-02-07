package com.ums.schedule.repository.cascade;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.ScheduleRestrictPolicy;
import com.ums.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ScheduleCascadePersistTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("Schedule저장 시 ScheduleRestrictPolicy도 함께 저장된다.")
    void shouldPersistScheduleRestrictPolicy_whenSavingSchedule() {
        Schedule schedule = ScheduleFixture.ofSchedule();
        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();
        restrictPolicy.applySchedule(schedule);

        Schedule saveSchedule = scheduleRepository.saveAndFlush(schedule);
        Long scheduleId = saveSchedule.getId();
        entityManager.clear();

        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();

        assertThat(findSchedule.getRestrictPolicies()).hasSize(1);
    }

}
