package com.ums.schedule.repository.lazy;

import com.netflix.discovery.converters.Auto;
import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendMessage;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestRepository;
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
public class SendMessageLazyLoadingTest {
    @Autowired private SendRequestRepository sendRequestRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("SendRequest 조회 시 SendMessage는 조회되지 않는다.")
    void shouldNotLoadSendMessages_whenFindSendRequest() {
        // given
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        // when
        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();

        // then
        PersistenceUnitUtil util =
                entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSendRequest, "sendMessages")).isFalse();
    }

    @Test
    @DisplayName("SendRequest 조회 시 SendMessage 조회 시 쿼리가 실행된다.")
    void shouldLoadSendMessages_whenAccessed() {
        // given
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        // when
        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();
        findSendRequest.getSendMessages().size();

        // then
        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSendRequest, "sendMessages")).isTrue();
    }


    @Test
    @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
    void shouldThrowLazyInitializationException_whenAccessSendMessagesOutsideTransaction() {
        // given
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        // when
        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();
        entityManager.detach(findSendRequest);

        // then
        assertThatThrownBy(() -> findSendRequest.getSendMessages().size())
                .isInstanceOf(LazyInitializationException.class);
    }
}
