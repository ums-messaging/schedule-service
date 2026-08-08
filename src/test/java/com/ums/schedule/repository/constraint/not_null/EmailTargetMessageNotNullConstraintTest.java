package com.ums.schedule.repository.constraint.not_null;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailTargetMessageNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    private EmailTargetMessageEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        TargetUploadReport targetUpload = givenTargetUploadReport(sendRequest);
        SendTarget sendTarget = givenSendTarget(targetUpload);

        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder().build();
        entityBuilder = EmailTargetMessageEntityBuilder.builder()
                .sendMessage(sendMessage)
                .targetMessage(targetMessage);
    }

    @Test
    @DisplayName("subject는 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenSubjectIsNull() {
        EmailTargetMessage message = entityBuilder.subject(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "SUBJECT");
    }

    @Test
    @DisplayName("body_message는 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenBodyMessageIsNull() {
        EmailTargetMessage message = entityBuilder.bodyMessage(null).build();

        assertThatThrownBy(() -> persist(message))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "BODY_MESSAGE");
    }
}