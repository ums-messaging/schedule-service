package com.ums.schedule.repository.constraint.unique;

import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailAttachmentUniqueConstraintTest extends EntityJpaTestSupport {
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();
    private EmailAttachmentBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        EmailSendMessage sendMessage = givenEmailSendMessage();

        entityBuilder = EmailAttachmentBuilder.builder();
        EmailAttachment attachment = entityBuilder
                .sendMessage(sendMessage)
                .fileKey("test.pdf")
                .build();
        persist(attachment);
    }

    @Test
    @DisplayName("message_id와 file_key가 중복되면, 예외가 발생한다.")
    void shouldThrowException_whenMessageIdAdnFileKeyAreDuplicated() {
        EmailAttachment attachment = entityBuilder.fileKey("test.pdf")
                .build();

        assertThatThrownBy(() -> persist(attachment))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_MESSAGE_FILE_KEY");
    }

    @Test
    @DisplayName("message_id와 file_key가 중복되지 않으면 저장된다.")
    void shouldPersist_whenMessageIdAndFileKeyAreNotDuplicated() {
        EmailAttachment attachment = entityBuilder.fileKey("test1.pdf")
                .build();

        EmailAttachment findAttachment = persist(attachment);

        assertThat(findAttachment).isNotNull();
    }
}
