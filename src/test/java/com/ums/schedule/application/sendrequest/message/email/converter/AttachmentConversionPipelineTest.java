package com.ums.schedule.application.sendrequest.message.email.converter;

import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.exception.EmailMessageConvertException;
import com.ums.schedule.application.exception.OwnerPasswordNotConfiguredException;
import com.ums.schedule.application.ums.email.convert.handler.HtmlMessageConverter;
import com.ums.schedule.application.ums.email.convert.handler.PdfMessageConverter;
import com.ums.schedule.application.message.email.handler.PdfSecurityConverter;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.ums.email.template.EmailTemplateLoader;
import com.ums.schedule.config.properties.SecurityPolicyProperties;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.template.exception.TemplateNotFoundException;
import freemarker.template.TemplateException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentConversionPipelineTest {
    private AttachmentPipelineCommandBuilder builder = AttachmentPipelineCommandBuilder.builder();

    @Nested
    @DisplayName("HTML 메시지 컨버터 테스트")
    class WhenHtmlMessageConverter {
        @Mock private EmailTemplateLoader templateLoader;
        @InjectMocks private HtmlMessageConverter pipeline;

        private TemplateConversionResult result;
        private final String templateContent = "${name}님의 ${month}월 청구서입니다.";

        @BeforeEach
        void setUp() {
            builder = builder.convertType(ConvertTypeEnum.HTML);
        }

        @Test
        @DisplayName("convert_type이 HTML이고, securityPolicy 여부가 false이면 지원한다.")
        void shouldSupport_whenConvertTypeIsHtml() {
            boolean expect = pipeline.supports(ConvertTypeEnum.HTML, false);
            assertThat(expect).isTrue();
        }

        @Test
        @DisplayName("임시 파일이 생성된다.")
        void shouldCreateTempFile() throws IOException, TemplateException {
            AttachmentPipelineCommand command = builder.build();
            doReturn(templateContent).when(templateLoader).loadAndCompileTemplate(any(), any());

            result = pipeline.handle(command);

            assertThat(result.tempFile()).exists();
        }

        @Test
        @DisplayName("convert_type이 HTML이면 렌더링 된 템플릿 내용이 파일에 저장된다.")
        void shouldSaveFileTemplateContent_whenConvertTypeIsHtml() throws TemplateException, IOException {
            AttachmentPipelineCommand command = builder.build();
            doReturn(templateContent).when(templateLoader).loadAndCompileTemplate(any(), any());

            result = pipeline.handle(command);

            File file = result.tempFile();
            String expect = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            assertThat(expect).contains(templateContent);
        }

        @Test
        @DisplayName("convert_type이 HTML이 아니면 빈 내용이 저장된다.")
        void shouldSaveEmptyContent_whenConvertTypeIsNotHtml() throws TemplateException, IOException {
            AttachmentPipelineCommand command = builder.convertType(ConvertTypeEnum.PDF).build();
            doReturn(templateContent).when(templateLoader).loadAndCompileTemplate(any(), any());

            result = pipeline.handle(command);

            File file = result.tempFile();
            String expect = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            assertThat(expect).isEmpty();
        }

        @Test
        @DisplayName("템플릿 치환 중 오류 발생 시 예외가 변환된다. ")
        void shouldThrowConvertException_whenTemplateParsingError() throws TemplateException, IOException {
            AttachmentPipelineCommand command = builder.build();
            TemplateException givenException = mock(TemplateException.class);
            doThrow(givenException)
                    .when(templateLoader).loadAndCompileTemplate(any(), any());

            EmailMessageConvertException expect = EmailMessageConvertException.of(givenException);

            assertThatThrownBy(() -> pipeline.handle(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("템플릿 내용이 null이면 예외가 변환된다.")
        void shouldConvertException_whenTemplateContentIsNull() throws TemplateException, IOException {
            AttachmentPipelineCommand command = builder.build();
            doReturn(null).when(templateLoader).loadAndCompileTemplate(any(), any());

            EmailMessageConvertException expect = EmailMessageConvertException.of(TemplateNotFoundException.of());

            assertThatThrownBy(() -> pipeline.handle(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("filePrefix는 임시 파일 명으로 생성된다.")
        void shouldCreateFilename() throws TemplateException, IOException {
            AttachmentPipelineCommand command = builder.filePrefix("test").build();
            doReturn("test").when(templateLoader).loadAndCompileTemplate(any(), any());

            TemplateConversionResult result = pipeline.handle(command);
            File file = result.tempFile();

            assertThat(file.getName()).startsWith("test_");
        }

        @Test
        @DisplayName("fileSuffix는 임시 파일 확장자로 생성된다.")
        void shouldCreateFileExtension() throws TemplateException, IOException {
            AttachmentPipelineCommand command = builder.fileSuffix(".pdf").build();
            doReturn("test").when(templateLoader).loadAndCompileTemplate(any(), any());

            TemplateConversionResult result = pipeline.handle(command);
            File file = result.tempFile();

            assertThat(file.getName()).endsWith(".pdf");
        }
    }

    @Nested
    @DisplayName("PDF 메시지 컨버터")
    class WhenPdfMessageConverter {
        @Mock private HtmlMessageConverter htmlHandler;
        @InjectMocks private PdfMessageConverter handler;

        @TempDir
        private Path tempDir;
        private File tempFile;

        private String templateContent = "<html><body><h1>test</h1></body></html>";

        @BeforeEach
        void setUp() throws IOException {
            builder = builder.convertType(ConvertTypeEnum.PDF);
            tempDir = Files.createTempFile("test", ".pdf");
            tempFile = tempDir.toFile();
        }

        @Test
        @DisplayName("convert_type이 PDF이고, securityPolicy가 NULL이면 지원한다.")
        void shouldSupport_whenConvertTypeIsPdfAndSecurityPolicyIsNull() {
            boolean expect = handler.supports(ConvertTypeEnum.PDF, false);
            assertThat(expect).isTrue();
        }
        @Test
        @DisplayName("임시 파일 경로에 PDF 파일이 생성된다.")
        void shouldCreateFileAtTempFilePath() {
            AttachmentPipelineCommand command = builder.build();
            TemplateConversionResult givenResult = TemplateConversionResult.of(tempFile, "test.pdf", templateContent);
            doReturn(givenResult)
                    .when(htmlHandler).handle(any());

            TemplateConversionResult result = handler.handle(command);

            assertThat(result.tempFile()).exists();
        }

        @Test
        @DisplayName("HtmlRenderPipeline 실행 중 오류 발생 시, 예외를 변환한다.")
        void shouldThrowConvertException_whenHtmlRenderPipelineThrowException() throws IOException {
            AttachmentPipelineCommand command = builder.build();
            doThrow(TemplateNotFoundException.of())
                    .when(htmlHandler).handle(any());

            EmailMessageConvertException expect = EmailMessageConvertException.of(TemplateNotFoundException.of());

            assertThatThrownBy(() -> handler.handle(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("HtmlRendererPipeline을 실행한다.")
        void shouldExecuteHtmlRendererPipeline() {
            AttachmentPipelineCommand command = builder.build();
            TemplateConversionResult givenResult = TemplateConversionResult.of(tempFile, "test.pdf", templateContent);
            doReturn(givenResult)
                    .when(htmlHandler).handle(any());

            handler.handle(command);

            verify(htmlHandler).handle(any());
        }
    }

    @Nested
    @DisplayName("PdfSecurityConverter 테스트")
    class WhenPdfSecurityConverter {

        @Mock private SecurityPolicyProperties properties;
        @Mock private PdfMessageConverter pdfHandler;
        @InjectMocks private PdfSecurityConverter handler;

        private TemplateConversionResult result;
        private EmailAttachment attachment;
        @TempDir
        private Path tempDir;
        private File tempFile;

        private String templateContent = "<html><body><h1>test</h1></body></html>";
        @BeforeEach
        void setUp() throws IOException {
            builder = builder.convertType(ConvertTypeEnum.PDF);

            tempFile = tempDir.resolve("test.pdf").toFile();

            try (PDDocument document = new PDDocument()) {
                document.addPage(new PDPage());
                document.save(tempFile);
            }
            result = TemplateConversionResult.of(tempFile, "test.pdf", templateContent);

        }

        @Test
        @DisplayName("convert_type이 PDF이고, securityPolicy가 NULL이 아니면 지원한다.")
        void shouldSupport_whenConvertTypeIsPdfAndSecurityPolicyIsNotNull() {
            boolean expect = handler.supports(ConvertTypeEnum.PDF, true);

            assertThat(expect).isTrue();
        }
        @Test
        @DisplayName("pdfConvertHandler를 실행한다.")
        void shouldExecutePdfConvertHandler() {
            AttachmentPipelineCommand command = builder.build();
            doReturn(result).when(pdfHandler).handle(any());
            doReturn("test").when(properties).getOwnerPassword();

            handler.handle(command);

            verify(pdfHandler).handle(any());
        }

        @Test
        @DisplayName("pdfConvertHandler 실행 중 오류가 발생하면 예외를 반환한다.")
        void shouldThrowConvertException_whenPdfConvertHandlerError() {
            AttachmentPipelineCommand command = builder.build();
            doThrow(EmailMessageConvertException.of(new IOException())).when(pdfHandler).handle(any());

            EmailMessageConvertException expect = EmailMessageConvertException.of(new IOException());

            assertThatThrownBy(() -> handler.handle(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("인쇄 권한이 비활성화되면 인쇄할 수 없다.")
        void givenPrintPermissionDisabled_whenProtectingPdf_thenCannotPrint() throws IOException {
            AttachmentPipelineCommand command = builder.canPrint(false).build();
            doReturn(result).when(pdfHandler).handle(any());
            doReturn("test").when(properties).getOwnerPassword();

            TemplateConversionResult result = handler.handle(command);

            try (PDDocument document = PDDocument.load(result.tempFile(), "20000314")) {
                AccessPermission permission = document.getCurrentAccessPermission();

                assertThat(permission.canPrint()).isFalse();
            }
        }

        @Test
        @DisplayName("ownerPassword가 설정되지 않으면 예외가 발생한다.")
        void shouldThrowException_whenOwnerPasswordIsEmpty() {
            AttachmentPipelineCommand command = builder.canModify(false).build();
            doReturn(result).when(pdfHandler).handle(any());
            doReturn("").when(properties).getOwnerPassword();

            OwnerPasswordNotConfiguredException expect = OwnerPasswordNotConfiguredException.of();

            assertThatThrownBy(() -> handler.handle(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("수정 권한이 비활성화되면 수정할 수 없다.")
        void givenModifyPermissionDisabled_whenProtectingPdf_thenCannotModify() throws IOException {
            AttachmentPipelineCommand command = builder.canModify(false).build();
            doReturn(result).when(pdfHandler).handle(any());
            doReturn("test").when(properties).getOwnerPassword();

            TemplateConversionResult result = handler.handle(command);

            try (PDDocument document = PDDocument.load(tempFile, "20000314")) {
                AccessPermission permission = document.getCurrentAccessPermission();

                assertThat(permission.isOwnerPermission()).isFalse();
                assertThat(permission.canModify()).isFalse();
            }
        }

        @Test
        @DisplayName("사용자 비밀번호를 설정하면 PDF가 암호화된다.")
        void givenUserPassword_whenProtectingPdf_thenPdfIsEncrypted() throws IOException {
            AttachmentPipelineCommand command = builder.canModify(false).build();
            doReturn(result).when(pdfHandler).handle(any());
            doReturn("test").when(properties).getOwnerPassword();

            TemplateConversionResult result = handler.handle(command);

            try (PDDocument document = PDDocument.load(tempFile, "20000314")) {
                assertThat(document.isEncrypted()).isTrue();
            }

        }

        @Test
        @DisplayName("잘못된 사용자 비밀번호로는 PDF를 열 수 없다.")
        void givenWrongUserPassword_whenOpeningPdf_thenThrowException() {

        }
    }

    private static class AttachmentPipelineCommandBuilder {
        private ConvertTypeEnum convertType;
        private String filePrefix;
        private String fileSuffix;
        private String fileKey;
        private String objectKey;
        private String attachmentName;
        private String downloadName;
        private Map<String, Object> targetDataParam;
        private String userPassword;
        private int encryptionLength;
        private boolean canModify;
        private boolean canPrint;

        AttachmentPipelineCommandBuilder() {
            this.filePrefix = "email";
            this.fileSuffix = "pdf";
            this.fileKey = "email_body.html";
            this.objectKey = "target_id.html";
            this.attachmentName = "jang님의 3월 청구서";
            this.downloadName = "202603월 청구서";
            this.targetDataParam = Map.of();
            this.userPassword = "20000314";
            this.encryptionLength = 128;
            this.canModify = false;
            this.canPrint = false;
        }


        static AttachmentPipelineCommandBuilder builder() {
            return new AttachmentPipelineCommandBuilder();
        }


        AttachmentPipelineCommandBuilder convertType(ConvertTypeEnum convertType) {
            this.convertType = convertType;
            return this;
        }

        AttachmentPipelineCommandBuilder filePrefix(String filePrefix) {
            this.filePrefix = filePrefix;
            return this;
        }

        AttachmentPipelineCommandBuilder fileSuffix(String fileSuffix) {
            this.fileSuffix = fileSuffix;
            return this;
        }

        AttachmentPipelineCommandBuilder fileKey(String fileKey) {
            this.fileKey = fileKey;
            return this;
        }

        AttachmentPipelineCommandBuilder objectKey(String objectKey) {
            this.objectKey = objectKey;
            return this;
        }

        AttachmentPipelineCommandBuilder attachmentName(String attachmentName) {
            this.attachmentName = attachmentName;
            return this;
        }

        AttachmentPipelineCommandBuilder downloadName(String downloadName) {
            this.downloadName = downloadName;
            return this;
        }

        AttachmentPipelineCommandBuilder targetDataParam(Map<String, Object> targetDataParam) {
            this.targetDataParam = targetDataParam;
            return this;
        }

        AttachmentPipelineCommandBuilder userPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }
        AttachmentPipelineCommandBuilder encryptionLength(int encryptionLength) {
            this.encryptionLength = encryptionLength;
            return this;
        }
        AttachmentPipelineCommandBuilder canModify(boolean canModify) {
            this.canModify = canModify;
            return this;
        }

        AttachmentPipelineCommandBuilder canPrint(boolean canPrint) {
            this.canPrint = canPrint;
            return this;
        }

        AttachmentPipelineCommand build() {
            return new AttachmentPipelineCommand(
                    convertType,
                    filePrefix,
                    fileSuffix,
                    fileKey,
                    objectKey,
                    attachmentName,
                    downloadName,
                    targetDataParam,
                    userPassword,
                    encryptionLength,
                    canModify,
                    canPrint
            );
        }
    }
}