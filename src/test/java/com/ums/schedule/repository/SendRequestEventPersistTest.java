package com.ums.schedule.repository;

import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.*;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.fixture.ScheduleDomainFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendRequestEventPersistTest {
    @Autowired
    private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.flush();

        requestId = sendRequest.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("send_request_event 생성 시 저장된다.")
    void shouldPersist_whenSendRequestEventCreate() {
        SendRequest request = entityManager.getReference(SendRequest.class, requestId);

        SendRequestEvent event = SendRequestEventTestBuilder.builder().sendRequest(request).build();

        entityManager.persist(event);
        entityManager.flush();

        Long id = event.getEventId();
        entityManager.clear();

        SendRequestEvent expect = entityManager.find(SendRequestEvent.class, id);
        assertThat(expect.getEventId()).isNotNull();
    }
}
