package com.ums.schedule.repository.persist;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
public class EmailSendMessagePersistTest extends EntityJpaTestSupport {
    private final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("EMAIL_SEND_MESSAGE 등록 테스트")
    void shouldPersistTest_whenSendMessageCreate() {
        SendMessage sendMessage = SendMessageBuilder.builder().build();
        EmailSendMessage emailMessage = EmailSendMessageBuilder.builder()
                .sendMessage(sendMessage).build();

        persist(emailMessage);
        entityManager.clear();

        EmailSendMessage findMessage = entityManager.find(EmailSendMessage.class, sendMessage.getId());

        assertThat(findMessage).isNotNull();
    }

    @Test
    @DisplayName("SEND_MESSAGE이 NULL이면, 예외가 발생한다.")
    void shouldThrowException_whenSendMessageIsNull() {
        EmailSendMessage emailMessage = EmailSendMessageBuilder.builder()
                .sendMessage(null).build();

        assertThatThrownBy(() -> persist(emailMessage))
                .isInstanceOf(IdentifierGenerationException.class);
    }
}