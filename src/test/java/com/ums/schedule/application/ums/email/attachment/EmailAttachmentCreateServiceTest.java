package com.ums.schedule.application.ums.email.attachment;

import com.ums.schedule.application.ums.email.generator.AttachmentMetadata;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.attachment.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.fixture.email.attachment.SecurityMailBuilder;
import com.ums.schedule.fixture.email.convert.AttachmentMetadataBuilder;
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
    private SecurityMail securityMail;
    private AttachmentMetadata attachment;

    @BeforeEach
    void setUp() {
        message = mock(EmailSendMessage.class);
        securityMail = SecurityMailBuilder.builder().build();
        attachment = AttachmentMetadataBuilder.builder().build();
    }

    @Test
    @DisplayName("첨부파일 목록은 입력된 개수만큼 저장된다.")
    void shouldCreateAttachmentsForEachInputAttachment() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        doReturn(mock(List.class)).when(repository).saveAll(any());


        verify(repository).saveAll(captor.capture());
        List<EmailAttachment> captorValue = captor.getValue();
        assertThat(captorValue).hasSize(3);
    }

}