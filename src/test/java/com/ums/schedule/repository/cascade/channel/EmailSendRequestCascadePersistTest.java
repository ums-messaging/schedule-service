package com.ums.schedule.repository.cascade.channel;

import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.EmailSendRequestTestBuilder;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmailSendRequestCascadePersistTest {
    @Autowired private EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();
        scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("email_send_request 저장 시 send_request도 함께 저장된다.")
    void shouldPersistSendRequest_whenSavingEmailSendRequest() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);

        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        EmailSendRequest emailSendRequest = EmailSendRequestTestBuilder.builder().sendRequest(sendRequest).build();

        entityManager.persist(emailSendRequest);
        entityManager.flush();

        Long requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest find = entityManager.find(SendRequest.class, requestId);

        assertThat(find.getId()).isNotNull();
    }
}
