package com.ums.schedule.repository.constraint.not_null;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestField;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
public class SendRequestNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    private SendRequestEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();

        entityBuilder = SendRequestEntityBuilder.builder()
                .schedule(schedule)
                .sendMessage(sendMessage);
    }

    @Test
    @DisplayName("template_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenTemplateKeyIsNull() {
        SendRequest sendRequest = entityBuilder
                .templateKey(null)
                .build();

        SendRequestField field = SendRequestField.TEMPLATE_KEY;

        assertThatThrownBy(() -> persist(sendRequest))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("channel_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenChannelTypeIsNotNull() {
        SendRequest sendRequest = entityBuilder
                .channelType(null)
                .build();

        SendRequestField field = SendRequestField.CHANNEL_TYPE;

        assertThatThrownBy(() -> persist(sendRequest))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field.name(), ERROR_MESSAGE);
    }

    @Test
    @DisplayName("sender_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenSenderKeyIsNotNull() {
        SendRequest sendRequest = entityBuilder
                .senderKey(null)
                .build();

        SendRequestField field = SendRequestField.SENDER_KEY;

        assertThatThrownBy(() ->  persist(sendRequest))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("state는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenStateIsNotNull() {
        SendRequest sendRequest = entityBuilder
                .state(null)
                .build();

        SendRequestField field = SendRequestField.STATUS;

        assertThatThrownBy(() ->  persist(sendRequest))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }
}
