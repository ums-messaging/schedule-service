package com.ums.schedule.repository.persist;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
class TargetMessagePersistTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private TargetUploadReport targetUploadReport;
    private TargetMessage targetMessage;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        targetUploadReport = givenTargetUploadReport(sendRequest);
        targetMessage = EmailTargetMessageEntityBuilder.builder()
                .sendMessage(sendMessage)
                .build();
        initializeTargetMessage(targetMessage, 1L);
    }

    private final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    @Test
    @DisplayName("EMAIL_TARGET_MESSAGE가 저장된다.")
    void shouldPersist_whenEmailTargetMessageCreated() {
        ReflectionTestUtils.setField(targetMessage, "id", 1L);
        ReflectionTestUtils.setField(targetMessage, "targetUploadReport", targetUploadReport);

        persist(targetMessage);

        Long id = targetMessage.getId();
        entityManager.clear();

        TargetMessage findMessage = entityManager.find(TargetMessage.class, id);
        assertThat(findMessage).isNotNull();
    }


    @Test
    @DisplayName("EMAIL_SEND_MESSAGE가 NULL이면 예외가 발생한다.")
    void shouldThrowException_whenEmailSendMessageIsNull() {
        ReflectionTestUtils.setField(targetMessage, "sendMessage", null);

        assertThatThrownBy(() -> persist(targetMessage))
                .isInstanceOf(PersistenceException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "MESSAGE_ID");
    }

    private void initializeTargetMessage(TargetMessage targetMessage, Long id) {
        ReflectionTestUtils.setField(targetMessage, "id", id);
        ReflectionTestUtils.setField(targetMessage, "groupId", TsidCreator.getTsid().toLong());
        ReflectionTestUtils.setField(targetMessage, "targetUploadReport", targetUploadReport);
        ReflectionTestUtils.setField(targetMessage, "state", new SendTargetCreateState());
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang314");
        ReflectionTestUtils.setField(targetMessage, "contact", "jang314@test.com");
    }
}