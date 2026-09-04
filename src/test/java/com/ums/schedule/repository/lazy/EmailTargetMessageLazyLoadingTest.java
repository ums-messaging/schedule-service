package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
public class EmailTargetMessageLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private Long messageId;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        TargetUploadReport targetUploadReport = givenTargetUploadReport(sendRequest);
        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                .sendMessage(sendMessage)
                .build();
        initializeEmailTargetMessage(targetUploadReport, targetMessage);
        persist(targetMessage);
        messageId = targetMessage.getId();
        entityManager.clear();
    }

    private void initializeEmailTargetMessage(TargetUploadReport targetUploadReport, TargetMessage targetMessage) {
        ReflectionTestUtils.setField(targetMessage, "id", 1L);
        ReflectionTestUtils.setField(targetMessage, "groupId", 3L);
        ReflectionTestUtils.setField(targetMessage, "state", new SendTargetCreateState());
        ReflectionTestUtils.setField(targetMessage, "targetUploadReport", targetUploadReport);
    }

    @Test
    @DisplayName("email_target_message 조회 시 email_send_message 는 조회되지 않는다.")
    void shouldNotLoadEmailSendMessage_whenFindEmailTargetMessage() {
        EmailTargetMessage findTargetMessage = entityManager.find(EmailTargetMessage.class, messageId);
        assertThat(Hibernate.isInitialized(findTargetMessage.getSendMessage())).isFalse();
    }

    @Test
    @DisplayName("email_target_message 조회 시 target_upload_report는 조회되지 않는다.")
    void shouldNotLoadTargetUploadReport_whenFindEmailTargetMessage() {
        EmailTargetMessage findTargetMessage = entityManager.find(EmailTargetMessage.class, messageId);
        assertThat(Hibernate.isInitialized(findTargetMessage.getTargetUploadReport())).isFalse();
    }
}
