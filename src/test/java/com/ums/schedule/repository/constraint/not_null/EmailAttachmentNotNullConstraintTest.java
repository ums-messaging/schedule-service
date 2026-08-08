package com.ums.schedule.repository.constraint.not_null;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailAttachmentNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    private EmailAttachmentBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        EmailSendMessage sendMessage = givenEmailSendMessage();
        entityBuilder = EmailAttachmentBuilder.builder()
                .sendMessage(sendMessage);
    }

    @Test
    @DisplayName("attachment_type은 NULL을 허용하지 않는다.")
    void shouldNotAllowAttachmentType() {
        EmailAttachment attachment = entityBuilder.attachmentType(null).build();

        assertThatThrownBy(() -> persist(attachment))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll("TYPE", ERROR_MESSAGE);
    }

    @Test
    @DisplayName("attachment_name은 NULL을 허용하지 않는다.")
    void shouldNotAllowAttachmentName() {
        EmailAttachment attachment = entityBuilder.attachmentName(null).build();

        assertThatThrownBy(() -> persist(attachment))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll("attachment_name", ERROR_MESSAGE);
    }

    @Test
    @DisplayName("download_name은 NULL을 허용하지 않는다.")
    void shouldNotAllowDownloadName() {
        EmailAttachment attachment = entityBuilder.downloadName(null).build();

        assertThatThrownBy(() -> persist(attachment))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll("download_name", ERROR_MESSAGE);
    }

    @Test
    @DisplayName("file_key는 NULL을 허용하지 않는다.")
    void shouldNotAllowFileKey() {
        EmailAttachment attachment = entityBuilder.attachmentType(null).build();

        assertThatThrownBy(() -> persist(attachment))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll("file_key", ERROR_MESSAGE);
    }
}
