package com.ums.schedule.application.target.generator;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.TargetRowResultBuilder;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.uploader.model.TargetUploadContext;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.resolver.EmailConvertResolver;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.api.SendTargetErrorCode;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import com.ums.schedule.fixture.email.EmailTemplateContentBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyBuilder;
import com.ums.schedule.fixture.target_upload.TargetUploadContextBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmailTargetMessageGeneratorTest {
    @Mock private TargetUploadReportJpaRepository targetUploadRepository;
    @Mock private EmailConvertResolver resolver;
    @InjectMocks private EmailTargetMessageGenerator generator;

    private TargetUploadContextBuilder contextBuilder;
    private TargetRowResultBuilder rowBuilder;

    @BeforeEach
    void setUp() {
        contextBuilder = TargetUploadContextBuilder.builder()
                .emailSendMessage(mock(EmailSendMessage.class))
                .template(EmailTemplateBuilder.builder().build())
        ;
        rowBuilder = TargetRowResultBuilder.builder()
                .rowNo(1)
                .targetRowStatus(SendTargetRowStatus.SUCCESS)
                .targetMessage(createTargetMessage());
    }


    private TargetMessageData createTargetMessage() {
        TargetMessageData targetMessage = new TargetMessageData(
                createTargetData(),
                createDataParam()
        );
        return targetMessage;
    }

    private Map<String, Object> createDataParam() {
        return Map.of(
                "month", 8,
                "fruit", "apple",
                "header_template", "header",
                "cover_template", "cover",
                "body_template", "body",
                "footer_template", "footer"
        );
    }

    private Map<SendTargetColumn, String> createTargetData() {
        return Map.of(
                SendTargetColumn.TARGET_KEY, UUID.randomUUID().toString(),
                SendTargetColumn.TARGET_NAME, "hyejin",
                SendTargetColumn.TARGET_EMAIL, "jang@test.com"
        );
    }

    @Nested
    @DisplayName("대상자 메시지 생성 성공 시")
    class WhenSendTargetCreate {
        @BeforeEach
        void setUp() {
            EmailConvertPolicy policy = EmailConvertPolicyBuilder.builder().build();
            doReturn(policy).when(resolver).resolve(any(), any());
            doReturn(Optional.ofNullable(mock(TargetUploadReport.class)))
                    .when(targetUploadRepository).findById(any());
        }

        @Test
        @DisplayName("대상자 업로드 리포트를 조회한다.")
        void shouldFindTargetUploadReport() {
            SendTarget target = generator.generate(contextBuilder.build(), rowBuilder.build());

            verify(targetUploadRepository).findById(any());
        }
        @Test
        @DisplayName("상태가 CREATE인 대상자를 생성한다.")
        void shouldCreateSendTarget() {
            SendTarget target = generator.generate(contextBuilder.build(), rowBuilder.build());
            assertThat(target.getState().getCurrentCode()).isEqualTo(SendTargetStatus.CREATE);
        }

        @Nested
        @DisplayName("템플릿 치환 테스트")
        class WhenTemplateParse {
            private ArgumentCaptor<RenderedTemplate> captor;

            private EmailTemplateBuilder templateBuilder;
            private EmailTemplateContentBuilder contentBuilder;

            @BeforeEach
            void setUp() throws IOException {
                captor = ArgumentCaptor.forClass(RenderedTemplate.class);
                templateBuilder = EmailTemplateBuilder.builder()
                        .body(createTemplate("body.html", "${body_template}"))
                ;
                contentBuilder = EmailTemplateContentBuilder.builder();
            }

            private EmailTemplateContent createTemplate(String templateName, String sourceCode) throws IOException {
                Template template = new Template(templateName, sourceCode, new Configuration(Configuration.VERSION_2_3_21));
                return EmailTemplateContentBuilder.builder()
                        .template(template)
                        .attachmentName("${fruit}.pdf")
                        .downloadName("${fruit}.pdf")
                        .build();
            }

            @Test
            @DisplayName("헤더 템플릿이 존재할 경우, 정상적으로 치환된다.")
            void shouldParseHeaderTemplate() throws IOException {
                EmailTemplateContent headerContent = createTemplate("header.html", "${header_template}");
                EmailTemplate template = templateBuilder.header(headerContent).build();

                TargetUploadContext context = contextBuilder.template(template).build();
                generator.generate(context, rowBuilder.build());

                verify(resolver).resolve(captor.capture(), any());
                RenderedTemplate result = captor.getValue();

                assertThat(result.header().template()).isEqualTo("header");
            }

            @Test
            @DisplayName("푸터 템플릿이 존재할 경우, 정상적으로 치환된다.")
            void shouldParseFooterTemplate() throws IOException {
                EmailTemplateContent footerTemplate = createTemplate("footer.html", "${footer_template}");
                EmailTemplate template = templateBuilder.footer(footerTemplate).build();

                TargetUploadContext context = contextBuilder.template(template).build();
                generator.generate(context, rowBuilder.build());

                verify(resolver).resolve(captor.capture(), any());
                RenderedTemplate result = captor.getValue();

                assertThat(result.footer().template()).isEqualTo("footer");
            }

            @Test
            @DisplayName("커버 템플릿이 존재할 경우, 정상적으로 치환된다.")
            void shouldParseCoverTemplate() throws IOException {
                EmailTemplateContent coverTemplate = createTemplate("cover.html", "${cover_template}");
                EmailTemplate template = templateBuilder.cover(coverTemplate).build();

                TargetUploadContext context = contextBuilder.template(template).build();
                generator.generate(context, rowBuilder.build());

                verify(resolver).resolve(captor.capture(), any());
                RenderedTemplate result = captor.getValue();

                assertThat(result.cover().template()).isEqualTo("cover");
            }

            @Test
            @DisplayName("바디 템플릿은 정상적으로 치환된다.")
            void shouldParseBodyTemplate() throws IOException {
                EmailTemplateContent bodyTemplate = createTemplate("body.html", "${body_template}");
                EmailTemplate template = templateBuilder.body(bodyTemplate).build();

                TargetUploadContext context = contextBuilder.template(template).build();
                generator.generate(context, rowBuilder.build());

                verify(resolver).resolve(captor.capture(), any());
                RenderedTemplate result = captor.getValue();

                assertThat(result.body())
                        .extracting(
                                RenderedTemplateContent::template,
                                RenderedTemplateContent::attachmentName,
                                RenderedTemplateContent::downloadName
                                )
                        .contains("body", "apple.pdf", "apple.pdf");
            }

            @Test
            @DisplayName("첨부파일 파일 키 템플릿이 존재할 경우 정상적으로 치환된다. ")
            void shouldParseFileKeyTemplate() {
                EmailTemplateContent attachment = EmailTemplateContentBuilder.builder()
                        .fileKeyTemplate("${fruit}.pdf")
                        .attachmentName("${fruit}.pdf")
                        .downloadName("${fruit}.pdf")
                        .build();
                EmailTemplate template = templateBuilder.attachmentList(List.of(attachment)).build();

                TargetUploadContext context = contextBuilder.template(template).build();
                generator.generate(context, rowBuilder.build());

                verify(resolver).resolve(captor.capture(), any());
                RenderedTemplate result = captor.getValue();

                assertThat(result.attachments())
                        .extracting(
                                RenderedTemplateContent::fileKey,
                                RenderedTemplateContent::attachmentName,
                                RenderedTemplateContent::downloadName
                        )
                        .contains(
                                tuple("apple.pdf", "apple.pdf", "apple.pdf")
                        );

            }

            @Test
            @DisplayName("첨부파일 파일 키가 존재할 경우 파일 키는 치환되지 않는다. ")
            void shouldParseFileTemplate() {
                EmailTemplateContent attachment = EmailTemplateContentBuilder.builder()
                        .fileKey("${fruit}.pdf")
                        .attachmentName("${fruit}.pdf")
                        .downloadName("${fruit}.pdf")
                        .build();
                EmailTemplate template = templateBuilder.attachmentList(List.of(attachment)).build();

                TargetUploadContext context = contextBuilder.template(template).build();

                // when
                generator.generate(context, rowBuilder.build());

                verify(resolver).resolve(captor.capture(), any());
                RenderedTemplate result = captor.getValue();

                assertThat(result.attachments())
                        .extracting(
                                RenderedTemplateContent::fileKey,
                                RenderedTemplateContent::attachmentName,
                                RenderedTemplateContent::downloadName
                        )
                        .contains(
                                tuple("${fruit}.pdf", "apple.pdf", "apple.pdf")
                        );
            }
        }
    }

    @Test
    @DisplayName("대상자 업로드 리포트 조회 실패 시 예외가 발생한다.")
    void shouldThrowException_whenTargetUploadReportFindFails() {
        doReturn(Optional.empty())
                .when(targetUploadRepository).findById(any());

        assertThatThrownBy(() -> generator.generate(contextBuilder.build(), rowBuilder.build()))
                .isInstanceOf(TargetUploadReportNotFoundException.class);
    }

    @Test
    @DisplayName("대상자 로우 결과가 FAIL인 경우 실패 대상자를 생성한다.")
    void shouldCreateFailureSendTarget_whenTargetRowResultStateIsFail() {
        doReturn(Optional.ofNullable(mock(TargetUploadReport.class)))
                .when(targetUploadRepository).findById(any());

        SendTarget target = generator.generate(contextBuilder.build(), rowBuilder.targetRowStatus(SendTargetRowStatus.FAIL).build());

        assertThat(target)
                .extracting(t -> t.getState().getCurrentCode(), t-> t.getResultMessage())
                .contains(SendTargetStatus.FAIL, SendTargetErrorCode.TARGET_ROW_READ_FAILS.description());
    }

    @Test
    @DisplayName("템플릿 파싱 중 오류 발생 시 실패 대상자를 생성한다.")
    void shouldCreateFailureSendTarget_whenTemplateRenderFails() {
        EmailTemplate template = EmailTemplateBuilder.builder()
                .title("${year}년 ${month}월 청구서입니다.")
                .build();
        TargetUploadContext context = contextBuilder.template(template).build();
        EmailConvertPolicy policy = EmailConvertPolicyBuilder.builder().build();

        doReturn(policy).when(resolver).resolve(any(), any());
        doReturn(Optional.ofNullable(mock(TargetUploadReport.class)))
                .when(targetUploadRepository).findById(any());

        SendTarget target = generator.generate(context, rowBuilder.build());

        assertThat(target)
                .extracting(t -> t.getState().getCurrentCode(), t-> t.getResultMessage())
                .contains(SendTargetStatus.FAIL, SendTargetErrorCode.TARGET_VARIABLE_REQUIRED.description())
        ;
    }

    @Test
    @DisplayName("템플릿 변환 중 오류 발생 시 실패 대상자를 생성한다.")
    void shouldCreateFailureSendTarget_whenTemplateConvertFails() {
        doReturn(Optional.ofNullable(mock(TargetUploadReport.class)))
                .when(targetUploadRepository).findById(any());
        doThrow(EmailMessageConvertException.of(EmailMessageErrorCode.NOT_CONVERT_MESSAGE))
                .when(resolver).resolve(any(), any());
        SendTarget target = generator.generate(contextBuilder.build(), rowBuilder.build());

        assertThat(target)
                .extracting(t -> t.getState().getCurrentCode(), t-> t.getResultMessage())
                .contains(SendTargetStatus.FAIL, EmailMessageErrorCode.NOT_CONVERT_MESSAGE.description());
    }
}