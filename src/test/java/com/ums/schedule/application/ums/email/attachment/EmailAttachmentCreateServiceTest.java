package com.ums.schedule.application.ums.email.attachment;

import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.attachment.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.fixture.template.EmailTemplateContentResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailAttachmentCreateServiceTest {
    @Mock private EmailAttachmentJpaRepository repository;
    @InjectMocks private EmailAttachmentCreateService attachmentService;

    private EmailSendMessage message;
    private List<EmailTemplateContentResult> templates;

    @BeforeEach
    void setUp() {
        message = mock(EmailSendMessage.class);
        EmailTemplateContentResult directAttachment = EmailTemplateContentResultBuilder.builder()
                .fileKey("/template/test.pdf")
                .build();
        EmailTemplateContentResult templateAttachment = EmailTemplateContentResultBuilder.builder()
                .fileKeyTemplate("/template/${test}.pdf")
                .build();

        templates = List.of(directAttachment, templateAttachment);
    }

    @Test
    @DisplayName("첨부파일 목록은 입력된 개수만큼 저장된다.")
    void shouldCreateAttachmentsForEachInputAttachment() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        doReturn(mock(List.class)).when(repository).saveAll(any());

        attachmentService.create(message, templates);

        verify(repository).saveAll(captor.capture());
        List<EmailAttachment> captorValue = captor.getValue();
        assertThat(captorValue).hasSize(2);
    }

    @Test
    @DisplayName("file_key가 존재하면 attachment_type이 DIRECT인 첨부파일이 생성된다.")
    void shouldCreateDirectAttachment() {
        doReturn(mock(List.class)).when(repository).saveAll(any());

        List<EmailAttachment> attachments = attachmentService.create(message, templates);

        assertThat(attachments)
                .filteredOn(v -> v.getType() == AttachmentType.DIRECT)
                .extracting(EmailAttachment::getFileKey)
                .contains("/template/test.pdf");
    }

    @Test
    @DisplayName("file_key_template이 존재하면 attachment_type이 TEMPLATE인 첨부파일이 생성된다.")
    void shouldCreateTemplateAttachment() {
        doReturn(mock(List.class)).when(repository).saveAll(any());

        List<EmailAttachment> attachments = attachmentService.create(message, templates);

        assertThat(attachments)
                .filteredOn(v -> v.getType() == AttachmentType.TEMPLATE)
                .extracting(EmailAttachment::getFileKey)
                .contains("/template/${test}.pdf");
    }
}