package com.ums.schedule.repository.constraint.not_null;

import com.ums.schedule.domain.message.email.EmailSendMessage;
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
public class EmailSendMessageNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    private EmailSendMessageBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        entityBuilder = EmailSendMessageBuilder.builder()
                .sendMessage(SendMessageBuilder.builder().build());
    }

    @Test
    @DisplayName("email_type은 NULL을 허용하지 않는다. ")
    void shouldThrowException_whenEmailTypeIsNull() {
        EmailSendMessage message = entityBuilder.emailType(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "email_type")
        ;
    }

    @Test
    @DisplayName("subject는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenSubjectIsNull() {
        EmailSendMessage message = entityBuilder.subject(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "subject")
        ;
    }

    @Test
    @DisplayName("body_template_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenBodyTemplateKeyIsNull() {
        EmailSendMessage message = entityBuilder.bodyTemplateKey(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "body_template_key")
        ;
    }

    @Test
    @DisplayName("convert_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenConvertTypeIsNull() {
        EmailSendMessage message = entityBuilder.convertMail(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "CONVERT_TYPE")
        ;
    }
}
