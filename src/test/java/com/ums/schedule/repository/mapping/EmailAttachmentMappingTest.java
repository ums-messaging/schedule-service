package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
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
public class EmailAttachmentMappingTest extends EntityJpaTestSupport {
    @Autowired
    private EntityManager entityManager;

    private EmailAttachmentBuilder entityBuilder ;

    @BeforeEach
    void setUp() {
        entityBuilder = EmailAttachmentBuilder.builder();
    }

    @Test
    @DisplayName("email_send_message 에서 연관 관계를 설정하면 FK는 저장되지 않는다.")
    void shouldNotPersistSendRequestFK_whenSetByInverseOnlySide() {
        EmailSendMessage sendMessage = EmailSendMessageBuilder.builder()
                .sendMessage(SendMessageBuilder.builder().build())
                .build();
        sendMessage.addAttachments(entityBuilder.build());
        persist(sendMessage);

        UUID id = sendMessage.getId();
        entityManager.clear();

        EmailSendMessage findSendMessage = entityManager.find(EmailSendMessage.class, id);

        assertThat(findSendMessage.getAttachments()).hasSize(0);
    }

    @Test
    @DisplayName("email_attachment에서 email_send_message와 연관관계를 설정하면 FK가 저장된다.")
    void shouldPersistEmailSendMessageFk() {
        UUID id = persistEmailSendMessageAndGetId();
        EmailSendMessage findSendMessage = entityManager.find(EmailSendMessage.class, id);

        EmailAttachment attachment = EmailAttachmentBuilder.builder()
                .sendMessage(findSendMessage)
                .build();
        persist(attachment);
        entityManager.clear();

        EmailSendMessage expect = entityManager.find(EmailSendMessage.class, id);

        assertThat(expect.getAttachments()).hasSize(1);
    }

    private UUID persistEmailSendMessageAndGetId() {
        EmailSendMessage sendMessage = givenEmailSendMessage();
        UUID id = sendMessage.getId();
        entityManager.clear();
        return id;
    }
}
