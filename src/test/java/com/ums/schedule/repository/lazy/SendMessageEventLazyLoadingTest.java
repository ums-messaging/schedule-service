package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendMessageRepository;
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
public class SendMessageEventLazyLoadingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendMessageRepository sendMessageRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("SendMessage 조회 시 SendMeesageEvent는 조회되지 않는다.")
    void shouldNotLoadSendMessageEvents_whenFindSendMessage() {
        Long messageId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        sendMessageEvent.applySendMessage(sendMessage);

        scheduleRepository.saveAndFlush(schedule);
        messageId = sendMessage.getId();
        entityManager.clear();

        SendMessage findMessage = sendMessageRepository.findById(messageId).orElseThrow();

        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findMessage, "sendMessageEvents"))
                .isFalse();
    }

    @Test
    @DisplayName("SendMessage 조회 시 SendMessageEvent에 접근하면 쿼리가 실행된다.")
    void shouldLoadSendMessageEvent_whenAccessed() {
        Long messageId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        sendMessageEvent.applySendMessage(sendMessage);

        scheduleRepository.saveAndFlush(schedule);
        messageId = sendMessage.getId();
        entityManager.clear();

        SendMessage findMessage = sendMessageRepository.findById(messageId).orElseThrow();
        findMessage.getSendMessageEvents().size();

        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findMessage, "sendMessageEvents"))
                .isTrue();
    }

    @Test
    @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
    void shouldThrowLazyInitialization_whenAccessSendMessageEventOutsideTransaction() {
        // given
        Long messageId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        sendMessageEvent.applySendMessage(sendMessage);

        scheduleRepository.saveAndFlush(schedule);
        messageId = sendMessage.getId();
        entityManager.clear();

        SendMessage findMessage = sendMessageRepository.findById(messageId).orElseThrow();
        entityManager.detach(findMessage);

        assertThatThrownBy(() -> findMessage.getSendMessageEvents().size())
                .isInstanceOf(LazyInitializationException.class);
    }
}
