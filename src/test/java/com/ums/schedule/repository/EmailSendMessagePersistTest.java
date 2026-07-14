package com.ums.schedule.repository;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.sendrequest.message.email.EmailSendMessageBuilder;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.message.SendMessageBuilder;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmailSendMessagePersistTest {
    @Autowired
    private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = persistSchedule();
        SendRequest sendRequest = persistSendRequest(schedule);
        flushAndClear(sendRequest);
    }

    @Test
    @DisplayName("EMAIL_SEND_MESSAGE 등록 테스트")
    void shouldPersistTest_whenSendMessageCreate() {
        SendRequest findRequest = entityManager.find(SendRequest.class, requestId);
        SendMessage sendMessage = SendMessageBuilder.builder().sendRequest(findRequest).build();
        EmailSendMessage emailMessage = EmailSendMessageBuilder.builder().sendMessage(sendMessage).build();

        entityManager.persist(emailMessage);
        entityManager.flush();
        entityManager.clear();

        EmailSendMessage findMessage = entityManager.find(EmailSendMessage.class, sendMessage.getId());

        assertThat(findMessage).isNotNull();
    }

    private void flushAndClear(SendRequest sendRequest) {
        entityManager.flush();
        requestId = sendRequest.getId();
        entityManager.clear();
    }

    private SendRequest persistSendRequest(Schedule schedule) {
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();
        entityManager.persist(sendRequest);
        return sendRequest;
    }

    private Schedule persistSchedule() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        entityManager.persist(schedule);
        return schedule;
    }
}
