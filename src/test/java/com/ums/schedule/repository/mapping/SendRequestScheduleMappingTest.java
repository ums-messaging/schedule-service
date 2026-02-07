package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestScheduleMappingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendRequestRepository sendRequestRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("연관관계 주인인 SendRequest에서 설정하면 Schedule FK가 저장된다.")
    void shouldPersistScheduleFK_whenSetBySendRequest() {
        Schedule schedule = scheduleRepository.saveAndFlush(ScheduleFixture.ofSchedule());
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);
        entityManager.clear();

        SendRequest saveRequest = sendRequestRepository.saveAndFlush(sendRequest);

        assertThat(saveRequest.getSchedule().getId()).isEqualTo(schedule.getId());
    }

    @Test
    @DisplayName("비주인인 스케쥴에서만 연관관계를 설정하면 FK는 저장되지 않는다.")
    void shouldNotPersistScheduleFK_whenSetByInverseSideOnly() {
        Schedule schedule = scheduleRepository.saveAndFlush(ScheduleFixture.ofSchedule());
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        Schedule saveSchedule = scheduleRepository.saveAndFlush(schedule);

        saveSchedule.getSendRequests().add(sendRequest);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("FK인 schedule_id는 null을 허용하지 않는다.")
    void shouldFailPersist_scheduleIdIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
