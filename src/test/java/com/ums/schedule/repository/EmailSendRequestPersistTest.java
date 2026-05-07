package com.ums.schedule.repository;

import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.EmailSendRequestTestBuilder;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.fixture.ScheduleDomainFixture;
import com.ums.schedule.domain.request.SendRequestDomainFixture;
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
        Schedule schedule = ScheduleTestBuilder.builder().build();

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
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();

        EmailSendRequest emailSendRequest =
                EmailSendRequestTestBuilder.builder().sendRequest(sendRequest).build();

        // when
        entityManager.persist(emailSendRequest);
        entityManager.flush();

        Long requestId = sendRequest.getId();
        entityManager.clear();

        // then
        EmailSendRequest expect = entityManager.find(EmailSendRequest.class, requestId);
        assertThat(expect.getId()).isNotNull();
    }
}
