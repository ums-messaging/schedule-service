package com.ums.schedule.repository;

import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.send.group.SendGroupTestBuilder;
import com.ums.schedule.domain.sendrequest.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
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
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();

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

        SendGroupEvent event = SendGroupTestBuilder.builder()
                .sendRequest(request).build();

        entityManager.persist(event);
        entityManager.flush();

        Long id = event.getEventId();
        entityManager.clear();

        SendGroupEvent expect = entityManager.find(SendGroupEvent.class, id);
        assertThat(expect.getEventId()).isNotNull();
    }
}
