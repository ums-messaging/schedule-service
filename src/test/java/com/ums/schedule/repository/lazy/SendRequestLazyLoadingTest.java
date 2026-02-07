package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestLazyLoadingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("Schedule 조회 시 SendRequest는 조회되지 않는다.")
    void shouldNotLoadSendRequests_whenFindSchedule() {
        Long scheduleId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);
        scheduleRepository.saveAndFlush(schedule);
        scheduleId = schedule.getId();
        entityManager.clear();

        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();

        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSchedule, "sendRequests")).isFalse();
    }

    @Test
    @DisplayName("Schedule 조회 시 SendRequest에 접근하면 쿼리가 실행된다.")
    void shouldLoadSendRequests_whenAccessed() {
        // given
        Long scheduleId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);
        scheduleRepository.saveAndFlush(schedule);
        scheduleId = schedule.getId();
        entityManager.clear();

        // when
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();
        findSchedule.getSendRequests().size();

        // then
        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSchedule, "sendRequests")).isTrue();
    }

    @Test
    @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
    void shouldThrowLazyInitializationException_whenAccessSendRequestOutsideTransaction() {
        // given
        Long scheduleId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);
        scheduleRepository.saveAndFlush(schedule);
        scheduleId = schedule.getId();
        entityManager.clear();

        // when
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow();
        entityManager.detach(findSchedule);

        // then
        assertThatThrownBy(() -> findSchedule.getSendRequests().size())
                .isInstanceOf(LazyInitializationException.class);
    }

}
