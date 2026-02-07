package com.ums.schedule.repository.constraint;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendMessageEventRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
public class SendMessageEventUniqueConstraintTest {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SendMessageEventRepository sendMessageEventRepository;
    @Autowired
    private EntityManager entityManager;

    private SendMessage sendMessage;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();

        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);

        scheduleRepository.saveAndFlush(schedule);

        this.sendMessage = sendMessage;

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("status는 null을 허용하지 않는다.")
    void shouldFail_StatusIsNull() {
        // given
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        setField(sendMessageEvent, "status", null);
        sendMessageEvent.applySendMessage(sendMessage);

        assertThatThrownBy(() -> sendMessageEventRepository.saveAndFlush(sendMessageEvent))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("error_code는 null을 허용하지 않는다.")
    void shouldFail_ErrorCodeIsNull() {
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        setField(sendMessageEvent, "errorCode", null);
        sendMessageEvent.applySendMessage(sendMessage);

        assertThatThrownBy(() -> sendMessageEventRepository.saveAndFlush(sendMessageEvent))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("attempt_order_no는 null을 허용하지 않는다.")
    void shouldFail_AttemptOrderNoIsNull() {
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        setField(sendMessageEvent, "attemptOrderNo", null);
        sendMessageEvent.applySendMessage(sendMessage);

        assertThatThrownBy(() -> sendMessageEventRepository.saveAndFlush(sendMessageEvent))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("evented_at은 null을 허용하지 않는다.")
    void shouldFail_EventedAtIsNull() {
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        setField(sendMessageEvent, "eventedAt", null);
        sendMessageEvent.applySendMessage(sendMessage);

        assertThatThrownBy(() -> sendMessageEventRepository.saveAndFlush(sendMessageEvent))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
