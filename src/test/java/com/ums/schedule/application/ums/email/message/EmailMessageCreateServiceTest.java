package com.ums.schedule.application.ums.email.message;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;
import com.ums.schedule.application.exception.email.security.SecurityMailProcessException;
import com.ums.schedule.application.sendrequest.email.command.EmailSendCreateRequestBuilder;
import com.ums.schedule.application.ums.common.message.SendMessageFactory;
import com.ums.schedule.application.ums.common.template.model.TemplateResult;
import com.ums.schedule.application.ums.email.attachment.EmailAttachmentCreateService;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.message.provider.EmailMessageContext;
import com.ums.schedule.application.ums.email.message.provider.EmailMessagePolicyProvider;
import com.ums.schedule.domain.exception.validation.RequiredException;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.fixture.email.message.EmailMessageContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailMessageCreateServiceTest {
    @Mock private SendMessageFactory factory;
    @Mock private EmailMessagePolicyProvider provider;
    @Mock private EmailSendMessageJpaRepository repository;
    @Mock private EmailAttachmentCreateService attachmentService;
    @InjectMocks private EmailMessageCreateService messageService;

    private EmailSendCreateRequestBuilder builder;
    private EmailMessageContextBuilder contextBuilder;

    @BeforeEach
    void setUp() {
        givenDefaultValueObjects();
        givenEmailSendCreateRequest();

    }

    private void givenDefaultValueObjects() {
        contextBuilder = EmailMessageContextBuilder.builder()
                .template(mock(TemplateResult.class));
    }

    private void givenEmailSendCreateRequest() {
        SendRequestCreateRequest request = SendRequestCreateRequestBuilder.builder().build();
        builder = EmailSendCreateRequestBuilder.builder()
                .request(request);
    }

    @Nested
    @DisplayName("이메일 메시지가 저장되지 않을 때")
    class WhenEmailSendMessageDoesNotSave {
        @Test
        @DisplayName("EmailMessageProvider 실행 중 오류 발생 시 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenEmailMessageProviderFails() {
            doThrow(mock(SecurityMailProcessException.class)).when(provider).provide(any(), any());

            assertThatThrownBy(() -> messageService.create("hyejin_company", builder.build()));

            verify(repository, never()).save(any(EmailSendMessage.class));
        }

        @Test
        @DisplayName("SendMessage 생성 중 오류 발생 시 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenSendMessageCreateFails() {
            doReturn(contextBuilder.build()).when(provider).provide(any(), any());
            doThrow(mock(RequiredException.class)).when(factory).createSendMessage(any());

            assertThatThrownBy(() -> messageService.create("hyejin_company", builder.build()));
            verify(repository, never()).save(any(EmailSendMessage.class));
        }
    }

    @Nested
    @DisplayName("이메일 메시지가 저장될 때")
    class WhenEmailSendMessageSave {

        @BeforeEach
        void setUp() {
            doReturn(mock(SendMessage.class)).when(factory).createSendMessage(any());
            doReturn(mock(EmailSendMessage.class)).when(repository).save(any());
            doReturn(contextBuilder.build()).when(provider).provide(any(), any());
        }

        @Test
        @DisplayName("EmailMessageProvider가 실행된다.")
        void shouldExecuteEmailMessageProvider() {
            messageService.create("hyejin_company", builder.build());

            verify(provider).provide(any(), any());
        }
        @Test
        @DisplayName("SendMessage가 생성된다.")
        void shouldCreateSendMessage() {
            EmailSendMessage message = messageService.create("hyejin_company", builder.build());

            assertThat(message.getSendMessage()).isNotNull();
        }


        @Test
        @DisplayName("이메일 메시지가 저장된다.")
        void shouldSaveEmailSendMessage() {
            messageService.create("hyejin_company", builder.build());

            verify(repository).save(any(EmailSendMessage.class));
        }
    }

    @Nested
    @DisplayName("첨부파일")
    class WhenAttachmentCreate {
        private ConvertedAttachment attachment;
        @BeforeEach
        void setUp() {
            attachment = mock(ConvertedAttachment.class);
            doReturn(mock(SendMessage.class)).when(factory).createSendMessage(any());
            doReturn(mock(EmailSendMessage.class)).when(repository).save(any());
        }

        @Test
        @DisplayName("첨부파일이 존재하면, 개수 만큼 첨부파일이 저장된다.")
        void shouldSaveAttachments() {
            ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
            EmailMessageContext context = contextBuilder.attachmentList(attachment, attachment, attachment).build();
            doReturn(context).when(provider).provide(any(), any());

            messageService.create("hyejin_company", builder.build());

            verify(attachmentService).create(any(), any(), captor.capture());
            List<ConvertedAttachment> captorList = captor.getValue();

            assertThat(captorList).hasSize(3);
        }

        @Test
        @DisplayName("첨부파일이 존재하지 않으면, 첨부파일이 저장되지 않는다.")
        void shouldNotSaveAttachments_whenAttachmentsDoNotExist() {
            EmailMessageContext context = contextBuilder.attachmentList().build();
            doReturn(context).when(provider).provide(any(), any());

            messageService.create("hyejin_company", builder.build());

            verify(attachmentService, never()).create(any(), any(), any());
        }
    }
}