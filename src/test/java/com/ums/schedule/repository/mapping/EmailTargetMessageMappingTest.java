package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
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
public class EmailTargetMessageMappingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private EmailTargetMessageEntityBuilder targetMessageEntityBuilder;
    private UUID targetId;
    private UUID messageId;

    private EmailSendMessage persistSendMessage() {
        EmailSendMessage sendMessage = givenEmailSendMessage();
        this.messageId = sendMessage.getId();
        return sendMessage;
    }

    @Nested
    @DisplayName("email_send_message 연관관계 테스트")
     class WhenTargetMessageMappingTest {
        private SendTarget sendTarget;
        private EmailSendMessage sendMessage;
        private EmailTargetMessageEntityBuilder entityBuilder;

        private UUID messageId;

        @BeforeEach
        void setUp() {
            EmailSendMessage sendMessage = givenEmailSendMessage();
            messageId = sendMessage.getId();
            entityManager.clear();
        }

        @Test
        @DisplayName("email_target_message 에서 email_send_message와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkEmailSendMessage() {
            EmailSendMessage sendMessage = entityManager.find(EmailSendMessage.class, messageId);
            EmailTargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                    .sendMessage(sendMessage)
                    .build();

            assertThat(targetMessage.getSendMessage()).isNotNull();
        }
    }
}