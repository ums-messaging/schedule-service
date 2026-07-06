package com.ums.schedule.repository;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachmentBuilder;
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
public class EmailSendRequestPersistTest {
    @Autowired private EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();

        entityManager.persist(schedule);
        entityManager.flush();
        scheduleId = schedule.getId();

        entityManager.clear();
    }

    @Test
    @DisplayName("email_send_request 생성 시 저장된다.")
    void shouldPersist_whenEmailSendRequestCreate() {
        // given
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();

        EmailAttachment emailSendRequest =
                EmailAttachmentBuilder.builder().sendRequest(sendRequest).build();

        // when
        entityManager.persist(emailSendRequest);
        entityManager.flush();

        Long requestId = sendRequest.getId();
        entityManager.clear();

        // then
        EmailAttachment expect = entityManager.find(EmailAttachment.class, requestId);
        assertThat(expect.getId()).isNotNull();
    }
}
