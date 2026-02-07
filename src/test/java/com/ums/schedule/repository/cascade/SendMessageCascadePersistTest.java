package com.ums.schedule.repository.cascade;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendMessageRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendMessageCascadePersistTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendMessageRepository sendMessageRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("SendMessage 저장 시 SendMessageEvent도 함께 저장된다.")
    void shouldPersistSendMessageEvent_whenSavingSendMessage() {
        Long messageId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        SendMessageEvent messageEvent = ScheduleFixture.ofSendMessageEvent();

        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        messageEvent.applySendMessage(sendMessage);

        scheduleRepository.saveAndFlush(schedule);
        messageId = sendMessage.getId();
        entityManager.clear();

        SendMessage findSendMessage = sendMessageRepository.findById(messageId).orElseThrow();

        assertThat(findSendMessage.getSendMessageEvents()).hasSize(1);
    }
}
