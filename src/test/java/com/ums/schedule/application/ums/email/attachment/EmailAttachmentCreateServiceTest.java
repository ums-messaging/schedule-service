package com.ums.schedule.application.ums.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentListCreateCommand;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.code.AttachmentType;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.exception.EmailSendMessage;
import com.ums.schedule.fixture.email.attachment.AttachmentContextBuilder;
import com.ums.schedule.fixture.email.attachment.AttachmentListCreateCommandBuilder;
import com.ums.schedule.fixture.email.convert.ConvertedAttachmentBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyBuilder;
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

    private AttachmentListCreateCommandBuilder commandBuilder;

    @BeforeEach
    void setUp() {
        commandBuilder = AttachmentListCreateCommandBuilder.builder()
                .sendMessage(mock(EmailSendMessage.class));
    }

    @Test
    @DisplayName("변환된 첨부파일이 존재하지 않으면 첨부파일 목록은 입력된 개수만큼 저장된다.")
    void shouldCreateAttachmentsForEachInputAttachment() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        EmailConvertPolicy policy = EmailConvertPolicyBuilder.builder().convertedAttachment(null).build();
        AttachmentListCreateCommand command = commandBuilder.convertedPolicy(policy)
                .attachmentList(List.of(givenAttachmentContext(), givenAttachmentContext(), givenAttachmentContext()))
                .build();

        attachmentService.create(command);

        verify(repository).saveAll(captor.capture());
        List<EmailAttachment> expect = captor.getValue();

        assertThat(expect)
                .extracting(EmailAttachment::getConvertType)
                .filteredOn(type -> type == ConvertTypeEnum.NONE)
                .hasSize(3);
    }

    @Test
    @DisplayName("변환된 첨부파일이 존재하면 입력된 첨부파일 개수에 변환된 첨부파일이 추가되어 저장된다.")
    void shouldCreateConvertedAttachmentInAdditionToInputAttachments() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        EmailConvertPolicy policy = EmailConvertPolicyBuilder.builder()
                .convertedAttachment(ConvertedAttachmentBuilder.builder().build()).build();
        AttachmentListCreateCommand command = commandBuilder.convertedPolicy(policy)
                .attachmentList(List.of(givenAttachmentContext(), givenAttachmentContext(), givenAttachmentContext()))
                .build();

        attachmentService.create(command);

        verify(repository).saveAll(captor.capture());

        assertThat(captor.getValue())
                .hasSize(4);
    }

    @Test
    @DisplayName("변환된 첨부파일만 존재하면, 변환된 첨부파일만 저장된다.")
    void shouldCreateConvertedAttachment_whenConvertedAttachmentExistsOnly() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        EmailConvertPolicy policy = EmailConvertPolicyBuilder.builder()
                .convertedAttachment(ConvertedAttachmentBuilder.builder().build()).build();
        AttachmentListCreateCommand command = commandBuilder.convertedPolicy(policy)
                .attachmentList(List.of())
                .build();

        attachmentService.create(command);

        verify(repository).saveAll(captor.capture());
        List<EmailAttachment> expect = captor.getValue();

        assertThat(expect)
                .extracting(EmailAttachment::getConvertType)
                .filteredOn(type -> type != ConvertTypeEnum.NONE)
                .hasSize(1);
    }

    private AttachmentContext givenAttachmentContext() {
        return AttachmentContextBuilder.builder()
                .type(AttachmentType.TEMPLATE)
                .key("attachment.html")
                .build();
    }
}