package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.exception.email.security.SecurityMailProcessException;
import com.ums.schedule.application.exception.template.TemplateNotFoundException;
import com.ums.schedule.application.sendrequest.email.command.EmailSendCreateRequestBuilder;
import com.ums.schedule.application.ums.common.template.TemplateResult;
import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.convert.resolver.EmailConvertResolver;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.security.SecurityMailAssembler;
import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.application.ums.email.template.query.EmailTemplateQueryService;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.fixture.email.convert.ConvertedAttachmentBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyBuilder;
import com.ums.schedule.fixture.email.security.EmailSecurityPolicyRequestBuilder;
import com.ums.schedule.fixture.template.EmailContentResultBuilder;
import com.ums.schedule.fixture.template.EmailTemplateDetailResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailMessagePolicyProviderTest {
    @Mock private SecurityMailAssembler assembler;
    @Mock private EmailConvertResolver resolver;
    @Mock private EmailTemplateQueryService templateService;

    @InjectMocks private EmailMessagePolicyProvider provider;

    private EmailTemplateDetailQuery query;
    private EmailSendCreateRequestBuilder builder;
    private EmailTemplateDetailResultBuilder templateBuilder;
    private EmailContentResultBuilder bodyBuilder;

    @BeforeEach
    void setUp() {
        this.query = mock(EmailTemplateDetailQuery.class);
        this.builder = EmailSendCreateRequestBuilder.builder();
        this.bodyBuilder = EmailContentResultBuilder.builder().body();
        this.templateBuilder = EmailTemplateDetailResultBuilder.builder()
                .title("메시지 제목")
                .contents(bodyBuilder.build());
    }

    @Nested
    @DisplayName("템플릿 조회")
    class WhenTemplateQuery {

        @BeforeEach
        void setUp() {
            doReturn(mock(EmailConvertPolicy.class)).when(resolver).resolve(any(), any());
        }

        @Test
        @DisplayName("템플릿을 조회한다.")
        void shouldQueryTemplate() {
            EmailTemplateResult template = new EmailTemplateResult(mock(TemplateResult.class), templateBuilder.build());
            doReturn(template).when(templateService).findTemplate(any());

            provider.provide(query, builder.build());

            verify(templateService).findTemplate(any());
        }

        @Nested
        @DisplayName("제목")
        class WhenTitle {

            @Test
            @DisplayName("제목이 존재하면 제목을 반환한다.")
            void shouldReturnTitle_whenTitleExists() {
                givenTemplateWithTitle("메시지 제목");

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.title()).isEqualTo("메시지 제목");
            }

            @Test
            @DisplayName("제목이 존재하면 제목은 반환하지 않는다.")
            void shouldReturnNull_whenTitleDoesNotExists() {
                givenTemplateWithTitle(null);

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.title()).isNull();
            }

            private void givenTemplateWithTitle(String title) {
                templateBuilder = templateBuilder.title(title);
                EmailTemplateResult template = EmailTemplateResult.of(templateBuilder.build());
                doReturn(template).when(templateService).findTemplate(any());
            }
        }

        @Nested
        @DisplayName("이미지 경로")
        class WhenImageDir {
            @Test
            @DisplayName("이미지 경로가 존재하면 이미지 경로을 반환한다.")
            void shouldReturnImageDir_whenImageDirExists() {
                givenTemplateWithImageDir("/template/email/images");

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.imageDir()).isEqualTo("/template/email/images");
            }

            @Test
            @DisplayName("이미지 경로가 존재하면 이미지 경로는 반환하지 않는다.")
            void shouldReturnNull_whenImageDirDoesNotExists() {
                givenTemplateWithImageDir(null);

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.imageDir()).isNull();
            }

            private void givenTemplateWithImageDir(String imageDir) {
                templateBuilder = templateBuilder.imageDir(imageDir);
                EmailTemplateResult template = EmailTemplateResult.of(templateBuilder.build());
                doReturn(template).when(templateService).findTemplate(any());
            }
        }

        @Nested
        @DisplayName("헤더 템플릿")
        class WhenHeaderTemplate {
            @Test
            @DisplayName("헤더 템플릿이 존재하면 헤더 파일 키를 반환한다.")
            void shouldReturnFileKey_whenHeaderTemplateExists() {
                EmailTemplateContentResult header = EmailContentResultBuilder.builder().header().build();
                givenTemplate(header);

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.headerKey()).isEqualTo("header.html");
            }

            @Test
            @DisplayName("헤더 템플릿이 존재하지 않으면, 반환하지 않는다.")
            void shouldReturnNull_whenHeaderTemplateDoesNotExist() {
                givenTemplate(null);

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.headerKey()).isNull();
            }
        }

        @Nested
        @DisplayName("푸터 템플릿")
        class WhenFooterTemplate {
            @Test
            @DisplayName("푸터 템플릿이 존재하면 푸터 파일 키를 반환한다.")
            void shouldReturnFileKey_whenFooterTemplateExists() {
                EmailTemplateContentResult footer = EmailContentResultBuilder.builder().footer().build();
                givenTemplate(footer);

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.footerKey()).isEqualTo("footer.html");
            }

            @Test
            @DisplayName("푸터 템플릿이 존재하지 않으면, 반환하지 않는다.")
            void shouldReturnNull_whenFooterTemplateDoesNotExist() {
                givenTemplate(null);

                EmailMessageContext context = provider.provide(query, builder.build());

                assertThat(context.footerKey()).isNull();
            }
        }


        private void givenTemplate(EmailTemplateContentResult content){
            templateBuilder = templateBuilder.contents(content, bodyBuilder.build());
            EmailTemplateResult template = EmailTemplateResult.of(templateBuilder.build());
            doReturn(template).when(templateService).findTemplate(any());
        }
    }
    @Nested
    @DisplayName("바디 템플릿 조회")
    class WhenBodyTemplate {
        @Test
        @DisplayName("바디 템플릿이 존재하지 않으면, 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateDoesNotExist() {
            givenNullBodyTemplate();

            TemplateNotFoundException expect = TemplateNotFoundException.of(EmailMessageSection.BODY);

            assertThatThrownBy(() -> provider.provide(query, builder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        private void givenNullBodyTemplate() {
            templateBuilder = templateBuilder.contents(null);
            EmailTemplateResult template = EmailTemplateResult.of(templateBuilder.build());
            doReturn(template).when(templateService).findTemplate(any());
        }
    }

    @Test
    @DisplayName("보안 정책 정보가 존재하고, 반환 결과가 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenSecurityPolicyExistsAndReturnNull() {
        EmailSendCreateRequest request = builder
                .securityPolicy(EmailSecurityPolicyRequestBuilder.builder().build())
                .build();

        doReturn(null).when(assembler).assemble(any());

        SecurityMailProcessException expect = SecurityMailProcessException.of();

        assertThatThrownBy(() -> provider.provide(query, request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Nested
    @DisplayName("보안 정책")
    class WhenSecurityMailPolicy {

        @BeforeEach
        void setUp() {
            EmailTemplateResult template = new EmailTemplateResult(mock(TemplateResult.class), templateBuilder.build());
            doReturn(mock(EmailConvertPolicy.class)).when(resolver).resolve(any(), any());
            doReturn(template).when(templateService).findTemplate(any());
        }

        @Test
        @DisplayName("보안 정책 정보가 존재하면, 보안 메일 정보를 생성한다.")
        void shouldCreateSecurityMail_whenSecurityPolicyRequestExists() {
            EmailSendCreateRequest request = builder
                    .securityPolicy(EmailSecurityPolicyRequestBuilder.builder().build())
                    .build();
            doReturn(mock(SecurityMail.class)).when(assembler).assemble(any());
            EmailMessageContext context = provider.provide(query, request);

            verify(assembler).assemble(any(SecurityMailCommand.class));

            assertThat(context.securityMail()).isNotNull();
        }

        @Test
        @DisplayName("보안 정책 정보가 존재하지 않으면, 보안 메일 정보는 생성되지 않는다.")
        void shouldNotCreateSecurityMail_whenSecurityPolicyRequestDoesExists() {
            EmailSendCreateRequest request = builder.securityPolicy(null).build();

            EmailMessageContext context = provider.provide(query, request);

            assertThat(context.securityMail()).isNull();
            verify(assembler, never()).assemble(any());
        }


    }

    @Nested
    @DisplayName("변환 정책")
    class WhenConvertPolicy {
        private EmailConvertPolicyBuilder policyBuilder;

        @BeforeEach
        void setUp() {
            policyBuilder = EmailConvertPolicyBuilder.builder()
                    .bodyKey("body.html")
                    .convertedAttachment(ConvertedAttachmentBuilder.builder().build());
            givenTemplateWithAttachments();
        }

        private void givenTemplateWithAttachments() {
            EmailTemplateContentResult attachment = EmailContentResultBuilder.builder().attachment().build();
            templateBuilder = templateBuilder.contents(
                    bodyBuilder.build(), attachment, attachment, attachment);
            EmailTemplateResult template = new EmailTemplateResult(mock(TemplateResult.class), templateBuilder.build());
            doReturn(template).when(templateService).findTemplate(any());
        }

        @Test
        @DisplayName("입력된 첨부파일은 변환 타입이 NONE인 첨부파일로 변환된다.")
        void shouldCovertAttachments() {
            doReturn(policyBuilder.build()).when(resolver).resolve(any(), any());

            EmailMessageContext context = provider.provide(query, builder.build());

            assertThat(context.attachments())
                    .filteredOn(attachment -> attachment.convertType() == ConvertType.NONE)
                    .hasSize(3);

        }

        @Test
        @DisplayName("변환된 템플릿이 존재하면 입력된 첨부파일에 추가되어 반환된다.")
        void shouldReturnAdditionalAttachments_whenConvertedAttachmentExists() {
            doReturn(policyBuilder.build()).when(resolver).resolve(any(), any());

            EmailMessageContext context = provider.provide(query, builder.build());

            assertThat(context.attachments())
                    .hasSize(4);
        }

        @Test
        @DisplayName("변환된 템플릿이 존재하지 않으면 입력된 첨부파일만 반환된다.")
        void shouldReturnAttachments_whenConvertedAttachmentDoesNotExist() {
            EmailConvertPolicy policy = policyBuilder.convertedAttachment(null).build();
            doReturn(policy).when(resolver).resolve(any(), any());

            EmailMessageContext context = provider.provide(query, builder.build());

            assertThat(context.attachments())
                    .hasSize(3);
        }

        @Test
        @DisplayName("변환된 템플릿이 존재하면, 변환된 템플릿의 변환 타입이 반환된다.")
        void shouldReturnConvertType_whenConvertedAttachmentExists() {
            EmailConvertPolicy policy = givenConvertedAttachment();
            doReturn(policy).when(resolver).resolve(any(), any());

            EmailMessageContext context = provider.provide(query, builder.build());

            assertThat(context.attachments())
                    .filteredOn(attachment -> attachment.convertType() == ConvertType.HTML)
                    .hasSize(1);
        }

        private EmailConvertPolicy givenConvertedAttachment() {
            ConvertedAttachment convertedAttachment = ConvertedAttachmentBuilder.builder().convertType(ConvertType.HTML).build();
            EmailConvertPolicy policy = policyBuilder
                    .convertedAttachment(convertedAttachment)
                    .build();
            return policy;
        }
    }
}