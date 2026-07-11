package com.ums.schedule.application.sendrequest.target.email;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.message.email.EmailResourceCommand;
import com.ums.schedule.application.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.convert.handler.HtmlMessageConverter;
import com.ums.schedule.application.ums.email.convert.handler.PdfMessageConverter;
import com.ums.schedule.application.message.email.handler.PdfSecurityConverter;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.target.code.TargetColumnEnum;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.template.email.EmailTemplate;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSendTargetGeneratorTest {
    private static final String templateContent = "${name}님의 ${month}월 청구서입니다.";

    @Mock private AwsS3Repository repository;
    private TargetUploadReport targetUploadReport;
    private TargetMessageData targetData;
    private Map<TargetColumnEnum, String> targetDataParam;
    private EmailSendTargetGenerator generator;

    @Mock private HtmlMessageConverter htmlPipeline;
    @Mock private PdfMessageConverter pdfPipeline;
    @Mock private PdfSecurityConverter securityPipeline;


    @BeforeEach
    void setUp() {
        generator = new EmailSendTargetGenerator(repository,
                List.of(htmlPipeline, pdfPipeline, securityPipeline)
        );

        targetDataParam =
                Map.of(
                        TargetColumnEnum.TARGET_KEY, UUID.randomUUID().toString(),
                        TargetColumnEnum.TARGET_EMAIL, "jang314@test.com",
                        TargetColumnEnum.TARGET_NAME, "jang",
                        TargetColumnEnum.TARGET_PHONE, "01012345678",
                        TargetColumnEnum.TARGET_BIRTHDAY, "20000314"
                );

        Map<String, Object> targetMessageParam =
                Map.of(
                        "name", "jang",
                        "month", 3
                );

        this.targetData = new TargetMessageData(targetDataParam, targetMessageParam);
        this.targetUploadReport = mock(TargetUploadReport.class);
    }

    @Nested
    @DisplayName("제목 템플릿")
    class WhenSubjectTemplate {
        private EmailTemplate template;

        @BeforeEach
        void setUp() {
            Template template = mock(Template.class);
            this.template = EmailTemplate.of(templateContent, template, List.of());
        }

        @Test
        @DisplayName("제목 템플릿이 정상적으로 치환된다.")
        void shouldParseSubjectTemplate() {
            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            assertThat(target.getTitle()).isEqualTo("jang님의 3월 청구서입니다.");
        }

        @Test
        @DisplayName("제목 템플릿 치환에 실패하면 대상자는 ERROR 상태가 된다.")
        void shouldChangeStateToError_whenSubjectTemplateParsingFails() {
            Map<String, Object> targetMessageParam =
                    Map.of(
                            "name", "jang"
                    );
            TargetMessageData targetData = new TargetMessageData(targetDataParam, targetMessageParam);
            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            assertThat(target.getState().currentStatusCode()).isEqualTo(SendTargetStatusEnum.FAIL);
        }
    }

    @Nested
    @DisplayName("본문 템플릿")
    class WhenContentTemplate {
        private EmailTemplate template;

        @BeforeEach
        void setUp() throws IOException {
            Template template = new Template("email_body", new StringReader(templateContent));
            this.template = EmailTemplate.of(templateContent, template, List.of());
        }

        @Test
        @DisplayName("본문 템플릿이 정상적으로 치환된다.")
        void shouldParseContentTemplate() throws IOException  {
            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            assertThat(target.getContent()).isEqualTo("jang님의 3월 청구서입니다.");
        }
        @Test
        @DisplayName("본문 템플릿 치환에 실패하면 대상자는 ERROR 상태가 된다.")
        void shouldReturnFailedTarget_whenSubjectTemplateParsingFails()  {
            Map<String, Object> targetMessageParam =
                    Map.of(
                            "name", "jang"
                    );
            TargetMessageData targetData = new TargetMessageData(targetDataParam, targetMessageParam);

            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            assertThat(target.getState().currentStatusCode()).isEqualTo(SendTargetStatusEnum.FAIL);
        }
    }

    @Nested
    @DisplayName("Attachment 테스트")
    class WhenAttachmentTest {
        private EmailTemplate template;
        private List<EmailAttachment> attachmentList;

        @BeforeEach
        void setUp() {
            EmailAttachment pdfDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.PDF)
                    .fileKeyTemplate("${targetKey}.pdf")
                    .build();

            EmailAttachment htmlDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.HTML)
                    .fileKeyTemplate("${targetKey}.html")
                    .build();

            EmailAttachment noneDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.NONE)
                    .fileKeyTemplate("${targetKey}.html")
                    .build();

            Template body = mock(Template.class);

            this.attachmentList = List.of(pdfDocument, htmlDocument, noneDocument);
            this.template = EmailTemplate.of(templateContent, body, this.attachmentList);
        }

        @Test
        @DisplayName("첨부파일 처리에 실패하면 실패 대상자를 반환한다.")
        void shouldReturnFailedTarget_whenAttachmentProcessingFails() throws IOException {
            doReturn(true).when(htmlPipeline).supports(any(), any(Boolean.class));
            doThrow(EmailMessageConvertException.of(new IOException()))
                    .when(htmlPipeline).handle(any());

            EmailMessageConvertException expect = EmailMessageConvertException.of(new IOException());

            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            assertThat(target.getState().currentStatusCode()).isEqualTo(SendTargetStatusEnum.FAIL);
            assertThat(target.getResultMessage()).isEqualTo(expect.getMessage());
        }

        @Test
        @DisplayName("첨부파일 명이 정상적으로 치환된다.")
        void shouldParseAttachmentName() throws IOException {
            doReturn(true).when(pdfPipeline).supports(any(), any(Boolean.class));
            doReturn(mock(TemplateConversionResult.class)).when(pdfPipeline).handle(any());

            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            List<EmailResourceCommand> expect = JsonUtil.toList(target.getResourceJson(), EmailResourceCommand.class);

            assertThat(expect).extracting(list -> list.attachmentName())
                    .containsExactly("jang.pdf", "jang.html", "jang.pdf");
        }

        @Test
        @DisplayName("fileKeyTemplate이 정상적으로 치환되어 objectKey로 반환한다.")
        void shouldReturnObjectKeyAndParseFileKeyTemplate() throws IOException {
            doReturn(true).when(pdfPipeline).supports(any(), any(Boolean.class));
            doReturn(mock(TemplateConversionResult.class)).when(pdfPipeline).handle(any());

            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            List<EmailResourceCommand> expect = JsonUtil.toList(target.getResourceJson(), EmailResourceCommand.class);

            assertThat(expect).extracting(list -> list.objectKey())
                    .containsExactly(
                            "%s.%s".formatted(target.getTargetKey(), "pdf"),
                            "%s.%s".formatted(target.getTargetKey(), "html"),
                            "%s.%s".formatted(target.getTargetKey(), "html"));
        }

        @Test
        @DisplayName("다운로드 명이 정상적으로 치환된다.")
        void shouldParseDownloadName() throws IOException {
            doReturn(true).when(pdfPipeline).supports(any(), any(Boolean.class));
            doReturn(mock(TemplateConversionResult.class)).when(pdfPipeline).handle(any());

            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            List<EmailResourceCommand> expect = JsonUtil.toList(target.getResourceJson(), EmailResourceCommand.class);

            assertThat(expect).extracting(list -> list.downloadName())
                    .containsExactly("jang.pdf", "jang.html", "jang.pdf");
        }

        @Test
        @DisplayName("보안 정책이 존재할 때 보안 정책 비밀번호가 정상적으로 치환된다.")
        void shouldParseSecurityPassword() {
            SecurityMailPolicy policy = mock(SecurityMailPolicy.class);
            EmailAttachment attachment = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.PDF)
                    .fileKeyTemplate("${targetKey}.html")
                    .securityPolicy(policy)
                    .build();

            EmailTemplate template = EmailTemplate.of("subject", mock(Template.class), List.of(attachment));
            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            List<EmailResourceCommand> list = JsonUtil.toList(target.getResourceJson(), EmailResourceCommand.class);
            EmailResourceCommand expect = list.get(0);

            assertThat(expect.password()).isEqualTo("20000314");
        }

        @Test
        @DisplayName("attachment 개수 만큼 대상자 첨부파일이 생성된다.")
        void shouldCreateAttachmentList() throws IOException {
            doReturn(true).when(htmlPipeline).supports(any(), anyBoolean());
            doReturn(mock(TemplateConversionResult.class)).when(htmlPipeline).handle(any());

            SendTarget target = generator.generate(targetUploadReport, template, targetData);

            List<EmailResourceCommand> expect = JsonUtil.toList(target.getResourceJson(), EmailResourceCommand.class);
            assertThat(expect).hasSize(3);
        }
    }

    @Nested
    @DisplayName("handler가 null일 때")
    class WhenAttachmentHandlerIsNull {
        private EmailTemplate template;
        private List<EmailAttachment> attachmentList;

        @BeforeEach
        void setUp() {
            EmailAttachment pdfDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.PDF)
                    .fileKeyTemplate("${targetKey}.pdf")
                    .build();

            EmailAttachment htmlDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.HTML)
                    .fileKeyTemplate("${targetKey}.html")
                    .build();

            EmailAttachment noneDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.NONE)
                    .fileKeyTemplate("${targetKey}.html")
                    .build();

            Template body = mock(Template.class);

            this.attachmentList = List.of(pdfDocument, htmlDocument, noneDocument);
            this.template = EmailTemplate.of(templateContent, body, this.attachmentList);
        }

        @Test
        @DisplayName("파일 업로드가 실행된다.")
        void shouldExecuteFileUpload() throws IOException {
            doReturn(true).when(htmlPipeline).supports(any(), anyBoolean());
            doReturn(mock(TemplateConversionResult.class)).when(htmlPipeline).handle(any());

            generator.generate(targetUploadReport, template, targetData);

            verify(repository, times(3)).upload(any(), any());
        }

        @Test
        @DisplayName("핸들러가 실행된다.")
        void shouldExecuteHandler() throws IOException {
            doReturn(true).when(htmlPipeline).supports(any(), anyBoolean());
            doReturn(mock(TemplateConversionResult.class)).when(htmlPipeline).handle(any());

            generator.generate(targetUploadReport, template, targetData);

            verify(htmlPipeline, times(3)).handle(any());
        }
    }

    @Nested
    @DisplayName("handler가 null이 아닐 때")
    class WhenAttachmentHandlerIsNotNull {
        private EmailTemplate template;
        private List<EmailAttachment> attachmentList;

        @BeforeEach
        void setUp() {
            EmailAttachment pdfDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.PDF)
                    .fileKeyTemplate("${targetKey}.pdf")
                    .build();

            EmailAttachment htmlDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.HTML)
                    .fileKeyTemplate("${targetKey}.html")
                    .build();

            EmailAttachment noneDocument = EmailAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.NONE)
                    .fileKeyTemplate("${targetKey}.html")
                    .build();

            Template body = mock(Template.class);

            this.attachmentList = List.of(pdfDocument, htmlDocument, noneDocument);
            this.template = EmailTemplate.of(templateContent, body, this.attachmentList);
        }

        @Test
        @DisplayName("파일 업로드가 실행되지 않는다.")
        void shouldNotExecuteFileUpload() throws IOException {
            doReturn(false).when(htmlPipeline).supports(any(), anyBoolean());
            doReturn(false).when(pdfPipeline).supports(any(), anyBoolean());
            doReturn(false).when(securityPipeline).supports(any(), anyBoolean());

            generator.generate(targetUploadReport, template, targetData);

            verify(repository, never()).upload(any(), any());
        }

        @Test
        @DisplayName("핸들러가 실행되지 않는다.")
        void shouldNotExecuteHandler() throws IOException {
            doReturn(false).when(htmlPipeline).supports(any(), anyBoolean());
            doReturn(false).when(pdfPipeline).supports(any(), anyBoolean());
            doReturn(false).when(securityPipeline).supports(any(), anyBoolean());

            generator.generate(targetUploadReport, template, targetData);

            verify(htmlPipeline, never()).handle(any());
            verify(pdfPipeline, never()).handle(any());
            verify(securityPipeline, never()).handle(any());
        }
    }
}