package com.ums.schedule.repository.constraint.event;

import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.send.group.SendGroupTestBuilder;
import com.ums.schedule.domain.sendrequest.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.fixture.field.SendRequestEventField;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendRequestEventDefaultConstraintTest {
    @Autowired
    private EntityManager entityManager;

    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest request = SendRequestTestBuilder.builder().schedule(schedule).build();

        entityManager.persist(schedule);
        entityManager.persist(request);
        entityManager.flush();

        requestId = request.getId();
        entityManager.clear();
    }
    @Test
    @DisplayName("issued_at의 기본 값은 현재 시각이다.")
    void shouldReturnIssuedAtIsCurrentTime_whenIssuedAtIsNull() {
        SendRequest sendRequest = entityManager.getReference(SendRequest.class, requestId);
        SendRequestEventField field = SendRequestEventField.ISSUED_AT;

        SendGroupEvent event =
                SendGroupTestBuilder.builder()
                        .sendRequest(sendRequest)
                        .issuedAt(null)
                        .build();

        entityManager.persist(event);
        entityManager.flush();

        assertThat(event.getIssuedAt().toLocalDate().atStartOfDay())
                .isEqualTo(LocalDateTime.now().toLocalDate().atStartOfDay());
    }
}
