package com.ums.schedule.repository.constraint.event;

import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.event.SendRequestEventTestBuilder;
import com.ums.schedule.domain.sendrequest.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.fixture.field.SendRequestEventField;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestEventNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired private EntityManager entityManager;
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
    @DisplayName("event_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenEventTypeIsNull() {
        SendRequest sendRequest = entityManager.getReference(SendRequest.class, requestId);
        SendGroupEvent event = SendRequestEventTestBuilder.builder().sendRequest(sendRequest)
                .eventType(null)
                .build();

        SendRequestEventField field = SendRequestEventField.EVENT_TYPE;

        assertThatThrownBy(() -> {
            entityManager.persist(event);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());

    }

    @Test
    @DisplayName("result_code는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenResultCodeIsNull() {
        SendRequest sendRequest = entityManager.getReference(SendRequest.class, requestId);
        SendGroupEvent event = SendRequestEventTestBuilder.builder().sendRequest(sendRequest)
                .resultCode(null)
                .build();

        SendRequestEventField field = SendRequestEventField.RESULT_CODE;

        assertThatThrownBy(() -> {
            entityManager.persist(event);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }
}
