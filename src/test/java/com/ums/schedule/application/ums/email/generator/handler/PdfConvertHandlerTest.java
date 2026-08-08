package com.ums.schedule.application.ums.email.generator.handler;

import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.fixture.email.convert.EmailConvertContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PdfConvertHandlerTest {
    @InjectMocks private PdfConvertHandler handler;

    private EmailConvertContextBuilder builder;

    @TempDir
    private Path path;

    @BeforeEach
    void setUp() throws IOException {
        this.path = Files.createTempFile("test", ".pdf");
        builder = EmailConvertContextBuilder.builder()
                .path(path)
                .template("<html><body>my template</body></html>");
    }
    @Test
    @DisplayName("파일 확장자가 PDF가 아니면 예외가 발생한다.")
    void shouldThrowException_whenFileExtensionIsNotPdf() throws IOException {
        this.path = Files.createTempFile("test", ".html");
        EmailConvertContext context = builder.path(this.path).build();

        assertThatThrownBy(() -> handler.handle(context))
                .isExactlyInstanceOf(EmailMessageConvertException.class)
                .extracting(v -> ((EmailMessageConvertException) v).getErrorCode())
                .isEqualTo(EmailMessageErrorCode.INVALID_CONVERT_TYPE_FILE);
    }

    @Test
    @DisplayName("템플릿 내용이 빈 값이면 예외가 발생한다.")
    void shouldThrowException_whenTemplateIsEmpty() {
        EmailConvertContext context = builder.template("").build();

        assertThatThrownBy(() -> handler.handle(context))
                .isExactlyInstanceOf(EmailMessageConvertException.class)
                .extracting(v -> ((EmailMessageConvertException) v).getErrorCode())
                .isEqualTo(EmailMessageErrorCode.NOT_FOUND_CONVERTED_CONTENT);
    }

    @Test
    @DisplayName("CONVERT_TYPE이 PDF이고, EMAIL_TYPE이 PLAIN이면 지원한다.")
    void shouldSupport_whenConvertTypeIsPdfAndSecurityPolicyIsNull() {
        boolean expect = handler.supports(ConvertType.PDF, EmailType.PLAIN);
        assertThat(expect).isTrue();
    }
    @Test
    @DisplayName("임시 파일 경로에 PDF 파일이 생성된다.")
    void shouldCreateFileAtTempFilePath() throws IOException {
        File file = handler.handle(builder.build());

        assertThat(file).exists();
    }
}