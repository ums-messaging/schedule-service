package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
public class SendTargetMappingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private TargetUploadReport targetUploadReport;
    private SendTargetEntityBuilder targetBuilder;

    private UUID uploadId;
    private UUID messageId;

    @BeforeEach
    public void setUp() {
        SendRequest sendRequest = givenSendMessageAndGetMessageId();
        givenTargetUploadReportAndGetUploadId(sendRequest);
        entityManager.clear();

        targetUploadReport = entityManager.find(TargetUploadReport.class, uploadId);
        targetBuilder = SendTargetEntityBuilder.builder()
                .targetUpload(targetUploadReport);
    }

    private void givenTargetUploadReportAndGetUploadId(SendRequest sendRequest) {
        TargetUploadReport targetUpload = givenTargetUploadReport(sendRequest);
        uploadId = targetUpload.getId();
    }

    private SendRequest givenSendMessageAndGetMessageId() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        this.messageId = sendMessage.getId();
        return sendRequest;
    }
    private UUID persistAndGetTargetId(SendTarget sendTarget) {
        persist(sendTarget);
        UUID targetId = sendTarget.getId();
        entityManager.clear();
        return targetId;
    }
    @Nested
    @DisplayName("target_upload 연관관계 테스트")
    class TargetUploadMappingTest {

        @Test
        @DisplayName("send_target에서 target_upload와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkTargetUpload_whenSetBySendTarget() {
            UUID targetId = persistAndGetTargetId(targetBuilder.build());

            SendTarget findSendTarget = entityManager.find(SendTarget.class, targetId);

            assertThat(findSendTarget.getTargetUpload()).isNotNull();
        }
    }

    @Nested
    @DisplayName("target_message 연관관계 테스트")
     class WhenTargetMessageMappingTest {
        private EmailSendMessage sendMessage;
        private EmailTargetMessageEntityBuilder entityBuilder;

        @BeforeEach
        void setUp() {
            sendMessage = entityManager.find(EmailSendMessage.class, messageId);
            entityBuilder = EmailTargetMessageEntityBuilder.builder()
                    .sendMessage(sendMessage);

        }
        @Test
        @DisplayName("send_target에서 target_message와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkTargetMessage() {
            TargetMessage targetMessage = persistAndFindTargetMessage();

            SendTarget sendTarget = targetBuilder.targetMessage(targetMessage).build();
            UUID targetId = persistAndGetTargetId(sendTarget);

            SendTarget findSendTarget = entityManager.find(SendTarget.class, targetId);
            assertThat(findSendTarget.getTargetMessage()).isNotNull();
        }

        private TargetMessage persistAndFindTargetMessage() {
            TargetMessage targetMessage = entityBuilder.build();
            persist(targetMessage);
            UUID targetMessageId = targetMessage.getId();
            TargetMessage findTargetMessage = entityManager.find(TargetMessage.class, targetMessageId);
            return findTargetMessage;
        }


        @Test
        @DisplayName("target_message에서 에서 연관 관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistTargetMessageFK_whenSetByInverseOnlySide() {
            SendTarget sendTarget = targetBuilder.build();
            UUID targetId = persistAndGetTargetId(sendTarget);

            SendTarget findSendTarget = findSendTargetAndPersistTargetMessage(targetId);

            assertThat(findSendTarget.getTargetMessage()).isNull();
        }

        private SendTarget findSendTargetAndPersistTargetMessage(UUID targetId) {
            SendTarget findSendTarget = entityManager.find(SendTarget.class, targetId);
            EmailTargetMessage targetMessage = entityBuilder.build();
            targetMessage.assignSendTarget(findSendTarget);
            persist(targetMessage);
            entityManager.clear();
            return entityManager.find(SendTarget.class, targetId);
        }
    }
}