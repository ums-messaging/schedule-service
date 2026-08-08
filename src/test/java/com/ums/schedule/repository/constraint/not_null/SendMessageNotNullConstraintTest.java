package com.ums.schedule.repository.constraint.not_null;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendMessageNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    private SendMessageBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        entityBuilder = SendMessageBuilder.builder();
    }

    @Test
    @DisplayName("message_type은 NULL을 허용하지 않는다. ")
    void shouldThrowException_whenMessageTypeIsNull() {
        SendMessage message = entityBuilder
                .messageType(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "message_type");
    }

    @Test
    @DisplayName("template_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenTemplateKeyIsNull() {
        SendMessage message = entityBuilder
                .messageType(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "template_key")
        ;
    }
}
