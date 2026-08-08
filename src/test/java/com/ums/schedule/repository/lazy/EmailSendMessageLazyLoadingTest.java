package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmailSendMessageLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private UUID messageId;

    @BeforeEach
    void setUp() {
        EmailSendMessage sendMessage = givenEmailSendMessage();
        messageId = sendMessage.getId();

        EmailAttachment attachment = EmailAttachmentBuilder.builder().sendMessage(sendMessage).build();
        persist(attachment);
        entityManager.clear();
    }

    @Nested
    @DisplayName("email_attachment 조회 테스트")
    class EmailAttachmentLazyLoadingTest {

        @Test
        @DisplayName("email_send_message 조회 시 email_attachment 는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            EmailSendMessage findSendMessage = entityManager.find(EmailSendMessage.class, messageId);
            assertThat(Hibernate.isInitialized(findSendMessage.getAttachments())).isFalse();
        }
    }
}
