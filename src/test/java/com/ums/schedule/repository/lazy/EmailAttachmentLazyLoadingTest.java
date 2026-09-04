package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
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
public class EmailAttachmentLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private UUID attachmentId;

    @BeforeEach
    void setUp() {
        EmailSendMessage sendMessage = givenEmailSendMessage();

        EmailAttachment attachment = EmailAttachmentBuilder.builder().sendMessage(sendMessage).build();
        persist(attachment);
        attachmentId = attachment.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("email_send_message 조회 테스트")
    class EmailSendMessageLazyLoadingTest {

        @Test
        @DisplayName("email_attachment 조회 시 email_send_message 는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            EmailAttachment findAttachment = entityManager.find(EmailAttachment.class, attachmentId);
            assertThat(Hibernate.isInitialized(findAttachment.getSendMessage())).isFalse();
        }
    }
}
