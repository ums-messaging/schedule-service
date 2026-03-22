package com.ums.schedule.repository.constraint;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendMessage;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendMessageRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
public class SendMessageUniqueConstraintTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendMessageRepository sendMessageRepository;
    @Autowired private EntityManager entityManager;
    private SendRequest sendRequest;
    private Schedule schedule;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);

        this.schedule = scheduleRepository.saveAndFlush(schedule);
        this.sendRequest = sendRequest;

        entityManager.clear();
    }

    @Test
    @DisplayName("receiver_id는 null 을 허용하지 않는다.")
    void shouldFail_ReceiverIdIsNull() {
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        setField(sendMessage, "receiverId", null);
        sendMessage.applySendRequest(sendRequest);

        assertThatThrownBy(() -> sendMessageRepository.saveAndFlush(sendMessage))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("FK인 send_request_id는 null을 허용하지 않는다.")
    void shouldFail_SendRequestIdIsNull() {
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();

        assertThatThrownBy(() -> sendMessageRepository.saveAndFlush(sendMessage))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("send_request_id에 대해 receiver_id는 중복을 허용하지 않는다.")
    void shouldFailPersistReceiverId_AboutSendRequestId() {
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        SendMessage dupSendMessage = ScheduleFixture.ofSendMessage();

        setField(sendMessage, "receiverId", "jang314");
        setField(dupSendMessage, "receiverId", "jang314");
        sendMessage.applySendRequest(sendRequest);
        dupSendMessage.applySendRequest(sendRequest);

        sendMessageRepository.saveAndFlush(sendMessage);

        assertThatThrownBy(() -> sendMessageRepository.saveAndFlush(dupSendMessage))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
