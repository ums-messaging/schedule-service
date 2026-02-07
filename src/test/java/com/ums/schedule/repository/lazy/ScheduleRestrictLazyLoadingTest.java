package com.ums.schedule.repository.lazy;


import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.ScheduleRestrictPolicy;
import com.ums.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import jakarta.persistence.PersistenceUtil;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class ScheduleRestrictLazyLoadingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private EntityManager entityManager;


    @Test
    @DisplayName("Schedule 조회 시 ScheduleRestrictPolicy는 조회되지 않는다.")
    void shouldNotLoadRestrictPolicies_whenFindSchedule() {
        // given
        Long scheduleId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();
        restrictPolicy.applySchedule(schedule);
        scheduleRepository.saveAndFlush(schedule);
        scheduleId = schedule.getId();
        entityManager.clear();

        // when
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();

        // then
        PersistenceUtil util =
                entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSchedule, "restrictPolicies")).isFalse();
    }

    @Test
    @DisplayName("Schedule조회 시 ScheduleRestrictPolicy에 접근하면 쿼리가 실행된다.")
    void shouldLoadRestrictPolicies_whenAccessed() {
        // given
        Long scheduleId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();
        restrictPolicy.applySchedule(schedule);
        scheduleRepository.saveAndFlush(schedule);
        scheduleId = schedule.getId();
        entityManager.clear();

        //when
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();
        findSchedule.getRestrictPolicies().size();

        // then
        PersistenceUnitUtil util =
                entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSchedule, "restrictPolicies")).isTrue();
    }

    @Test
    @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
    void shouldThrowLazyInitializationException_whenAccessRestrictPoliciesOutsideTransaction() {
        // given
        Long scheduleId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();
        restrictPolicy.applySchedule(schedule);
        scheduleRepository.saveAndFlush(schedule);
        scheduleId = schedule.getId();
        entityManager.clear();

        //when
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();
        entityManager.detach(findSchedule);

        assertThatThrownBy(() -> findSchedule.getRestrictPolicies().size())
                .isInstanceOf(LazyInitializationException.class);
    }
}
