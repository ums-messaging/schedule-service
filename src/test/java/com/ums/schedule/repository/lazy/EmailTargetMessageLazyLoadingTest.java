package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
public class EmailTargetMessageLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private UUID messageId;

    @BeforeEach
    void setUp() {
        EmailSendMessage sendMessage = givenEmailSendMessage();
        EmailTargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder().sendMessage(sendMessage)
                .build();
        persist(targetMessage);
        messageId = targetMessage.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("email_send_message 조회 테스트")
    class EmailAttachmentLazyLoadingTest {

        @Test
        @DisplayName("email_send_message 조회 시 email_attachment 는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            EmailTargetMessage findTargetMessage = entityManager.find(EmailTargetMessage.class, messageId);
            assertThat(Hibernate.isInitialized(findTargetMessage.getSendMessage())).isFalse();
        }
    }
}
