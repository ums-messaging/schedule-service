package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.ScheduleRestrictPolicy;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.ScheduleRestrictPolicyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class RestrictPolicyScheduleMappingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private ScheduleRestrictPolicyRepository restrictPolicyRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("연관관계 주인인 ScheduleRestrictPolicy에서 설정하면 Schedule FK가 저장된다.")
    void shouldPersistScheduleFK_whenSetByScheduleRestrictPolicy() {
        // given
        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();
        Schedule schedule = scheduleRepository.saveAndFlush(ScheduleFixture.ofSchedule());

        // when
        restrictPolicy.applySchedule(schedule);
        ScheduleRestrictPolicy savePolicy = restrictPolicyRepository.saveAndFlush(restrictPolicy);
        Long policyId = savePolicy.getId();
        entityManager.clear();

        // then
        ScheduleRestrictPolicy findPolicy = restrictPolicyRepository.findById(policyId).orElseThrow();
        assertThat(findPolicy.getSchedule().getId()).isEqualTo(schedule.getId());
    }

    @Test
    @DisplayName("비주인인 스케쥴에서만 연관관계를 설정하면 FK는 저장되지 않는다.")
    void shouldNotPersistScheduleFK_whenSetByInverseSideOnly() {
        // given
        Schedule schedule = ScheduleFixture.ofSchedule();
        ScheduleRestrictPolicy policy = ScheduleFixture.ofScheduleRestrictPolicy();
        Schedule saveSchedule = scheduleRepository.saveAndFlush(schedule);

        // when
        saveSchedule.getRestrictPolicies().add(policy);

        // then
        assertThatThrownBy(() -> restrictPolicyRepository.saveAndFlush(policy))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("FK인 schedule_id는 null을 허용하지 않는다.")
    void shouldFailPersist_scheduleIdIsNull() {
        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();

        assertThatThrownBy(() -> restrictPolicyRepository.saveAndFlush(restrictPolicy))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
