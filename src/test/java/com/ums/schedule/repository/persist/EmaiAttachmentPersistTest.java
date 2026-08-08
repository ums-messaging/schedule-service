package com.ums.schedule.repository.persist;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmaiAttachmentPersistTest extends EntityJpaTestSupport {
    @Autowired
    private EntityManager entityManager;

    private EmailSendMessage sendMessage;

    @BeforeEach
    void setUp() {
        sendMessage = givenEmailSendMessage();
    }

    @Test
    @DisplayName("EMAIL_ATTACHMENT 등록 테스트")
    void shouldPersistTest_whenEmailAttachmentCreate() {
        EmailAttachment attachment = EmailAttachmentBuilder.builder()
                .sendMessage(sendMessage)
                .build();

        persist(attachment);
        UUID id = attachment.getId();
        entityManager.clear();

        EmailAttachment findAttachment = entityManager.find(EmailAttachment.class, id);
        assertThat(findAttachment).isNotNull();
    }

    @Test
    @DisplayName("EMAIL_SEND_MESSAGE이 NULL이면, 예외가 발생한다.")
    void shouldThrowException_whenSendMessageIsNull() {
        EmailAttachment attachment = EmailAttachmentBuilder.builder()
                .sendMessage(null)
                .build();

        assertThatThrownBy(() -> persist(attachment))
                .isInstanceOf(PersistenceException.class);
    }
}
