package com.ums.schedule.application.ums.email.message;

import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;
import com.ums.schedule.application.sendrequest.email.command.EmailSendCreateRequestBuilder;
import com.ums.schedule.application.ums.common.exception.TemplateLoadFailException;
import com.ums.schedule.application.ums.common.message.SendMessageFactory;
import com.ums.schedule.application.ums.common.template.model.TemplateResult;
import com.ums.schedule.application.ums.email.attachment.EmailAttachmentCreateService;
import com.ums.schedule.application.ums.email.message.provider.EmailPolicyResult;
import com.ums.schedule.application.ums.email.message.provider.EmailMessagePolicyProvider;
import com.ums.schedule.application.ums.email.exception.SecurityMailProcessException;
import com.ums.schedule.application.ums.email.template.query.EmailTemplateQueryService;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.exception.DbNotFoundException;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessageJpaRepository;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.fixture.email.convert.EmailPolicyResultBuilder;
import com.ums.schedule.fixture.template.EmailTemplateContentResultBuilder;
import com.ums.schedule.fixture.template.EmailTemplateDetailResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailMessageCreateServiceTest {
    @Spy private EnumMapperFactory mapperFactory;
    @Mock private EmailTemplateQueryService templateQueryService;
    @Mock private SendMessageFactory factory;
    @Mock private EmailMessagePolicyProvider provider;
    @Mock private EmailSendMessageJpaRepository repository;
    @Mock private EmailAttachmentCreateService attachmentService;
    @InjectMocks private EmailMessageCreateService messageService;

    private EmailSendCreateRequestBuilder builder;

    @BeforeEach
    void setUp() {
        mapperFactory.register(EmailCode.class);
        givenEmailSendCreateRequest();
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
        @DisplayName("템플릿 조회 실패 시 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenTemplateFindFails() {
            TemplateLoadFailException exception = mock(TemplateLoadFailException.class);
            doThrow(exception).when(templateQueryService).findTemplate(any());

            assertThatThrownBy(() -> messageService.create("hyejin_company", builder.build()))
                    ;
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("EmailMessageProvider 실행 중 오류 발생 시 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenEmailMessageProviderFails() {
            doThrow(mock(SecurityMailProcessException.class)).when(provider).provide(any());
            assertThatThrownBy(() -> messageService.create("hyejin_company", builder.build()));

            verify(repository, never()).save(any(EmailSendMessage.class));
        }

        @Test
        @DisplayName("SendMessage 생성 중 오류 발생 시 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenSendMessageCreateFails() {
            givenTemplate();
            givenEmailPolicyResult();
            doThrow(mock(DbNotFoundException.class)).when(factory).createSendMessage(any());

            assertThatThrownBy(() -> messageService.create("hyejin_company", builder.build()));
            verify(repository, never()).save(any(EmailSendMessage.class));
        }
    }

    @Nested
    @DisplayName("이메일 메시지가 저장될 때")
    class WhenEmailSendMessageSave {

        @BeforeEach
        void setUp() {
            givenTemplate();
            givenEmailPolicyResult();
            doReturn(SendMessageBuilder.builder().build()).when(factory).createSendMessage(any());
            doReturn(mock(EmailSendMessage.class)).when(repository).save(any());
        }

        @Test
        @DisplayName("템플릿이 조회된다.")
        void shouldFindTemplate() {
            messageService.create("hyejin_company", builder.build());
            verify(templateQueryService).findTemplate(any());
        }

        @Test
        @DisplayName("EmailMessageProvider가 실행된다.")
        void shouldExecuteEmailMessageProvider() {
            messageService.create("hyejin_company", builder.build());

            verify(provider).provide(any());
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
        @BeforeEach
        void setUp() {
            givenEmailPolicyResult();
            doReturn(mock(SendMessage.class)).when(factory).createSendMessage(any());
            doReturn(mock(EmailSendMessage.class)).when(repository).save(any());
        }

        @Test
        @DisplayName("첨부파일이 존재하면, 개수 만큼 첨부파일이 저장된다.")
        void shouldSaveAttachments() {
            ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
            givenTemplateWithAttachments();

            messageService.create("hyejin_company", builder.build());

            verify(attachmentService).create(any(), captor.capture());
            List<EmailAttachment> captorList = captor.getValue();

            assertThat(captorList).hasSize(3);
        }

        @Test
        @DisplayName("첨부파일이 존재하지 않으면, 첨부파일이 저장되지 않는다.")
        void shouldNotSaveAttachments_whenAttachmentsDoNotExist() {
            givenTemplate();

            messageService.create("hyejin_company", builder.build());

            verify(attachmentService, never()).create(any(EmailSendMessage.class), any(List.class));
        }
    }

    private void givenTemplate() {
        EmailTemplateDetailResult template = EmailTemplateDetailResultBuilder.builder()
                .contents(EmailTemplateContentResultBuilder.builder().body().build())
                .build();
        EmailTemplateResult result = new EmailTemplateResult(mock(TemplateResult.class), template);
        doReturn(result).when(templateQueryService).findTemplate(any());
    }

    private void givenTemplateWithAttachments() {
        EmailTemplateDetailResult template = EmailTemplateDetailResultBuilder.builder()
                .contents(
                        EmailTemplateContentResultBuilder.builder().body().build(),
                        EmailTemplateContentResultBuilder.builder().attachment().build(),
                        EmailTemplateContentResultBuilder.builder().attachment().build(),
                        EmailTemplateContentResultBuilder.builder().attachment().build()

                )
                .build();
        EmailTemplateResult result = new EmailTemplateResult(mock(TemplateResult.class), template);
        doReturn(result).when(templateQueryService).findTemplate(any());
    }

    private void givenEmailPolicyResult() {
        EmailPolicyResult policyResult = EmailPolicyResultBuilder.builder()
                .emailType(EmailType.PLAIN)
                .convertMail(mock(ConvertMail.class))
                .securityMail(null)
                .build();
        doReturn(policyResult).when(provider).provide(any());
    }
}