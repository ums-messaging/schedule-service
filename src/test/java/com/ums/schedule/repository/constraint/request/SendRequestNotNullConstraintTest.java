package com.ums.schedule.repository.constraint.request;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.fixture.field.SendRequestField;
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
public class SendRequestNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();
        this.scheduleId = schedule.getId();

        entityManager.clear();
    }

    @Test
    @DisplayName("template_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenTemplateKeyIsNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest request = SendRequestTestBuilder.builder()
                .templateKey(null)
                .schedule(schedule).build();

        SendRequestField field = SendRequestField.TEMPLATE_KEY;

        assertThatThrownBy(() -> {
            entityManager.persist(request);
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("channel_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenChannelTypeIsNotNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);

        SendRequest request = SendRequestTestBuilder.builder()
                .schedule(schedule)
                .channelType(null)
                .build();

        SendRequestField field = SendRequestField.CHANNEL_TYPE;

        assertThatThrownBy(() -> {
            entityManager.persist(request);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field.name(), ERROR_MESSAGE);
    }

    @Test
    @DisplayName("sender_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenSenderKeyIsNotNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);

        SendRequest request = SendRequestTestBuilder.builder()
                .schedule(schedule)
                .senderKey(null)
                .build();

        SendRequestField field = SendRequestField.SENDER_KEY;

        entityManager.persist(request);

        assertThatThrownBy(() ->  entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }
}
