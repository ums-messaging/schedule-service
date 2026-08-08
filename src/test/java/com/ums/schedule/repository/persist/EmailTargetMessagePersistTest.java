package com.ums.schedule.repository.persist;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.fixture.entity.TargetMessageEntityBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
class EmailTargetMessagePersistTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    private EmailTargetMessageEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        TargetUploadReport targetUploadReport = givenTargetUploadReport(sendRequest);
        SendTarget sendTarget = givenSendTarget(targetUploadReport);


        entityBuilder = EmailTargetMessageEntityBuilder.builder()
                .sendMessage(sendMessage);
    }

    @Test
    @DisplayName("EMAIL_TARGET_MESSAGE가 저장된다.")
    void shouldPersist_whenEmailTargetMessageCreated() {
        EmailTargetMessage targetMessage = entityBuilder
                .build();

        persist(targetMessage);

        UUID id = targetMessage.getId();
        entityManager.clear();

        EmailTargetMessage findMessage = entityManager.find(EmailTargetMessage.class, id);
        assertThat(findMessage).isNotNull();
    }


    @Test
    @DisplayName("EMAIL_SEND_MESSAGE가 NULL이면 예외가 발생한다.")
    void shouldThrowException_whenEmailSendMessageIsNull() {
        EmailTargetMessage targetMessage = entityBuilder
                .sendMessage(null)
                .build();

        assertThatThrownBy(() -> persist(targetMessage))
                .isInstanceOf(PersistenceException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "MESSAGE_ID");
    }
}