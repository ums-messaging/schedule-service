package com.ums.schedule.repository.cascade;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class SendTargetCascadePersistTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private TargetUploadReport targetUploadReport;
    private SendTargetEntityBuilder targetBuilder;

    private UUID uploadId;
    private UUID messageId;

    @BeforeEach
    public void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        messageId = sendMessage.getId();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        TargetUploadReport targetUploadReport = givenTargetUploadReport(sendRequest);
        targetBuilder = SendTargetEntityBuilder.builder().targetUpload(targetUploadReport);
    }

    @Test
    @DisplayName("send_target 저장 시 email_target_message도 함께 저장된다.")
    void shouldPersistSendRequest_whenSavingEmailSendRequest() {
        EmailSendMessage sendMessage = entityManager.find(EmailSendMessage.class, messageId);
        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                .sendMessage(sendMessage)
                .build();
        SendTarget sendTarget = targetBuilder.targetMessage(targetMessage).build();
        UUID targetId = persistAndGetTargetId(sendTarget);

        SendTarget findSendTarget = entityManager.find(SendTarget.class, targetId);
        assertThat(findSendTarget.getTargetMessage()).isNotNull();
    }

    private UUID persistAndGetTargetId(SendTarget sendTarget) {
        UUID targetId;
        persist(sendTarget);
        targetId = sendTarget.getId();
        entityManager.clear();
        return targetId;
    }
}
