package com.ums.schedule.repository.constraint.channel;

import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.EmailSendRequestTestBuilder;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.channel.email.message.EmailBodyTestBuilder;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import com.ums.schedule.domain.request.SendRequestDomainFixture;
import com.ums.schedule.fixture.field.EmailSendRequestField;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailSendRequestNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.flush();

        this.requestId = sendRequest.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("mail_from은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenMailFromIsNull() {
        SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);
        EmailSendRequest emailSendRequest = EmailSendRequestTestBuilder.builder()
                .sendRequest(sendRequest)
                .mailFrom(null).build();

        EmailSendRequestField field = EmailSendRequestField.MAIL_FROM;

        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }



    @Test
    @DisplayName("mail_from_name은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenMailFromNameIsNull() {
        SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);
        EmailSendRequest emailSendRequest = EmailSendRequestTestBuilder.builder()
                .sendRequest(sendRequest)
                .mailFromName(null).build();

        EmailSendRequestField field = EmailSendRequestField.MAIL_FROM_NAME;

        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());

    }

    @Test
    @DisplayName("email_template_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenEmailTemplateKeyIsNull() {
        SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);
        EmailSendRequest emailSendRequest = EmailSendRequestTestBuilder.builder()
                .sendRequest(sendRequest)
                .emailTemplateKey(null)
                .build();

        EmailSendRequestField field = EmailSendRequestField.EMAIL_TEMPLATE_KEY;


        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("convert_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenConvertTypeIsNull() {
        SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);
        EmailBody body = EmailBodyTestBuilder.builder().convertType(null).build();
        EmailSendRequest emailSendRequest = EmailSendRequestTestBuilder.builder()
                .sendRequest(sendRequest)
                .emailBody(body)
                .build();

        EmailSendRequestField field = EmailSendRequestField.CONVERT_TYPE;

        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }
}
