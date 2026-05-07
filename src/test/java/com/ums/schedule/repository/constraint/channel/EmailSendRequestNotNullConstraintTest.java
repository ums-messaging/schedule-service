package com.ums.schedule.repository.constraint.channel;

import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.request.SendRequest;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailSendRequestNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired private EntityManager entityManager;
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
    @DisplayName("mail_from은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenMailFromIsNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest(schedule);
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload(sendRequest);
        EmailSendRequest emailSendRequest = EmailSendRequest.of(EmailBody.of(), targetUpload);
        init(emailSendRequest);

        EmailSendRequestField field = EmailSendRequestField.MAIL_FROM;

        ReflectionTestUtils.setField(emailSendRequest, field.value(), null);

        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }



    @Test
    @DisplayName("mail_from_name은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenMailFromNameIsNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest(schedule);
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload(sendRequest);
        EmailSendRequest emailSendRequest = EmailSendRequest.of(EmailBody.of(), targetUpload);
        init(emailSendRequest);

        EmailSendRequestField field = EmailSendRequestField.MAIL_FROM_NAME;

        ReflectionTestUtils.setField(emailSendRequest, field.value(), null);

        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());

    }

    @Test
    @DisplayName("email_template_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenEmailTemplateKeyIsNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest(schedule);
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload(sendRequest);
        EmailSendRequest emailSendRequest = EmailSendRequest.of(EmailBody.of(), targetUpload);
        init(emailSendRequest);

        EmailSendRequestField field = EmailSendRequestField.EMAIL_TEMPLATE_KEY;

        ReflectionTestUtils.setField(emailSendRequest, field.value(), null);

        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("convert_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenConvertTypeIsNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestDomainFixture.createSendRequest(schedule);
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload(sendRequest);
        EmailSendRequest emailSendRequest = EmailSendRequest.of(EmailBody.of(), targetUpload);
        init(emailSendRequest);

        EmailSendRequestField field = EmailSendRequestField.CONVERT_TYPE;
        EmailBody body = EmailBody.of();

        ReflectionTestUtils.setField(body, field.value(), null);
        ReflectionTestUtils.setField(emailSendRequest, EmailSendRequestField.EMAIL_BODY.value(), body);


        entityManager.persist(emailSendRequest);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    private void init(EmailSendRequest emailSendRequest) {
        emailSendRequest.applyTemplateKey(UUID.randomUUID().toString());
        emailSendRequest.applyMailFrom("jang314@naver.com", "jang");
    }

}
