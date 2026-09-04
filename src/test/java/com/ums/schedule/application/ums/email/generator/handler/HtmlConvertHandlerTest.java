package com.ums.schedule.application.ums.email.generator.handler;

import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.exception.EmailPolicyViolationException;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class HtmlConvertHandlerTest {
    @InjectMocks private HtmlConvertHandler handler;

    private EmailConvertContextBuilder builder;

    @TempDir
    private Path path;

    @BeforeEach
    void setUp() throws IOException {
        this.path = Files.createTempFile("test", ".html");
        builder = EmailConvertContextBuilder.builder()
                .path(path)
                .template("my template.");
    }

    @Test
    @DisplayName("파일 확장자가 html이 아니면 예외가 발생한다.")
    void shouldThrowException_whenFileExtensionIsNotHtml() throws IOException {
        this.path = Files.createTempFile("test", "pdf");
        EmailConvertContext context = builder.path(this.path).build();

        assertThatThrownBy(() -> handler.handle(context))
                .isInstanceOf(EmailMessageConvertException.class)
                .extracting(v -> ((EmailMessageConvertException) v).getErrorCode())
                .isEqualTo(EmailMessageErrorCode.INVALID_CONVERT_TYPE_FILE);
    }

    @Test
    @DisplayName("템플릿 내용이 빈 값이면 예외가 발생한다.")
    void shouldThrowException_whenTemplateIsEmpty() {
        EmailConvertContext context = builder.template("").build();

        assertThatThrownBy(() -> handler.handle(context))
                .isInstanceOf(EmailPolicyViolationException.class)
                .extracting(v -> ((EmailPolicyViolationException) v).getErrorCode())
                .isEqualTo(EmailMessageErrorCode.NOT_FOUND_CONVERTED_CONTENT);
    }

    @Test
    @DisplayName("convert_type이 HTML이고, 이메일 타입이 PLAIN이면 지원한다.")
    void shouldSupport_whenConvertTypeIsHtml() {
        boolean expect = handler.supports(ConvertType.HTML, EmailType.PLAIN);
        assertThat(expect).isTrue();
    }

    @Test
    @DisplayName("임시 파일이 생성된다.")
    void shouldCreateTempFile() throws IOException {
        File file = handler.handle(builder.build());

        assertThat(file).exists();
    }

    @Test
    @DisplayName("템플릿 내용이 파일에 저장된다.")
    void shouldSaveFileTemplateContent_whenConvertTypeIsHtml() throws  IOException {
        File file = handler.handle(builder.build());
        String expect = Files.readString(file.toPath(), StandardCharsets.UTF_8);

        assertThat(expect).contains("my template.");
    }
}