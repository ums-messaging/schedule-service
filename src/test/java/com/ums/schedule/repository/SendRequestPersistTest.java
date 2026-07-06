package com.ums.schedule.repository;

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
public class SendRequestPersistTest {
    @Autowired
    private EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();

        this.scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("SEND_REQUEST 등록 테스트")
    void shouldPersistSendRequest_whenCreatingSendRequest() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();

        entityManager.persist(sendRequest);
        entityManager.flush();

        Long requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest expect = entityManager.find(SendRequest.class, requestId);
        assertThat(expect.getId()).isNotNull();
    }
}
