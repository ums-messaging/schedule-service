package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendMessageEventRepository;
import com.ums.schedule.repository.SendMessageRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendMessageEventMappingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendMessageRepository sendMessageRepository;
    @Autowired private SendMessageEventRepository eventRepository;
    @Autowired private EntityManager entityManager;

    private SendMessage sendMessage ;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);

        sendMessage = ScheduleFixture.ofSendMessage();
        scheduleRepository.saveAndFlush(schedule);
        sendMessage.applySendRequest(sendRequest);
        entityManager.clear();
    }

    @Test
    @DisplayName("연관관계 주인인 SendMessageEvent 에서 설정하면 FK가 저장된다.")
    void shouldPersistSendMessageFK_whenSetBySendMessageEvent() {
        // given
        SendMessage sendMessage = sendMessageRepository.saveAndFlush(this.sendMessage);
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        sendMessageEvent.applySendMessage(sendMessage);

        // when
        SendMessageEvent saveMessageEvent = eventRepository.saveAndFlush(sendMessageEvent);

        // then
        assertThat(saveMessageEvent.getSendMessage().getId()).isEqualTo(sendMessage.getId());
    }

    @Test
    @DisplayName("비주인인 SendMessage 에서만 연관관계를 설정하면 FK는 저장되지 않는다.")
    void shouldNotPersistSendMessageFK_WhenSetByInverseSideOnly() {
        // given
        SendMessage sendMessage = sendMessageRepository.saveAndFlush(this.sendMessage);
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();
        sendMessage.getSendMessageEvents().add(sendMessageEvent);

        assertThatThrownBy(() -> eventRepository.saveAndFlush(sendMessageEvent))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("FK인 message_id는 null을 허용하지 않는다.")
    void shouldFailPersist_sendMessageIdIsNull() {
        SendMessageEvent sendMessageEvent = ScheduleFixture.ofSendMessageEvent();

        assertThatThrownBy(()->eventRepository.saveAndFlush(sendMessageEvent))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
