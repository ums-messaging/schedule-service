package com.ums.schedule.application.ums.email.generator.handler;

import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.exception.SecurityMailNotConfiguredException;
import com.ums.schedule.common.code.api.SecurityMailErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.config.properties.SecurityPolicyProperties;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.fixture.email.convert.EmailConvertContextBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PdfSecurityHandlerTest {
    @Mock
    private SecurityPolicyProperties properties;
    @Mock private PdfConvertHandler pdfHandler;
    @InjectMocks
    private PdfSecurityHandler handler;

    @TempDir
    private Path path;
    private File tempFile;

    private EmailConvertContextBuilder builder;
    @BeforeEach
    void setUp() throws IOException {
        path = Files.createTempFile("test", ".pdf");
        tempFile = path.resolve("test.pdf").toFile();
        builder = EmailConvertContextBuilder.builder()
                .path(path)
                .userPassword("19940314")
                .permissionMask(PermissionMaskEnum.NONE)
                .encryptionType(EncryptionTypeEnum.ASE256);

        try (PDDocument document = new PDDocument()) {
            document.addPage(new PDPage());
            document.save(tempFile);
        }
    }

    @Test
    @DisplayName("convert_type이 PDF이고, MAIL_TYPE이 SECURITY이면 지원한다.")
    void shouldSupport_whenConvertTypeIsPdfAndSecurityPolicyIsNotNull() {
        boolean expect = handler.supports(ConvertType.PDF, EmailType.SECURITY);
        assertThat(expect).isTrue();
    }

    @Test
    @DisplayName("pdfConvertHandler를 실행한다.")
    void shouldExecutePdfConvertHandler() throws IOException {
        doReturn(tempFile).when(pdfHandler).handle(any());
        doReturn("test").when(properties).getOwnerPassword();

        handler.handle(builder.build());

        verify(pdfHandler).handle(any());
    }

    @Test
    @DisplayName("pdfConvertHandler 실행 중 오류가 발생하면 예외를 반환한다.")
    void shouldThrowConvertException_whenPdfConvertHandlerError() throws IOException {
        EmailMessageConvertException exception = mock(EmailMessageConvertException.class);
        doThrow(exception).when(pdfHandler).handle(any());

        EmailMessageConvertException expect = EmailMessageConvertException.of(exception);

        assertThatThrownBy(() ->  handler.handle(builder.build()))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("인쇄 권한이 비활성화되면 인쇄할 수 없다.")
    void givenPrintPermissionDisabled_whenProtectingPdf_thenCannotPrint() throws IOException {
        doReturn(tempFile).when(pdfHandler).handle(any());
        doReturn("test").when(properties).getOwnerPassword();


        try (PDDocument document = PDDocument.load(tempFile, "20000314")) {
            AccessPermission permission = document.getCurrentAccessPermission();

            assertThat(permission.canPrint()).isFalse();
        }
    }

    @Test
    @DisplayName("ownerPassword가 설정되지 않으면 예외가 발생한다.")
    void shouldThrowException_whenOwnerPasswordIsEmpty() throws IOException {
        doReturn(tempFile).when(pdfHandler).handle(any());
        doReturn("").when(properties).getOwnerPassword();

        SecurityMailNotConfiguredException expect = SecurityMailNotConfiguredException.of(SecurityMailErrorCode.OWNER_PW_CONFIGURED_LOAD_FAILS);


        assertThatThrownBy(() ->  handler.handle(builder.build()))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("수정 권한이 비활성화되면 수정할 수 없다.")
    void givenModifyPermissionDisabled_whenProtectingPdf_thenCannotModify() throws IOException {
        doReturn(tempFile).when(pdfHandler).handle(any());
        doReturn("test").when(properties).getOwnerPassword();

        File file = handler.handle(builder.build());

        try (PDDocument document = PDDocument.load(tempFile, "20000314")) {
            AccessPermission permission = document.getCurrentAccessPermission();

            assertThat(permission.isOwnerPermission()).isFalse();
            assertThat(permission.canModify()).isFalse();
        }
    }

    @Test
    @DisplayName("사용자 비밀번호를 설정하면 PDF가 암호화된다.")
    void givenUserPassword_whenProtectingPdf_thenPdfIsEncrypted() throws IOException {
        doReturn(tempFile).when(pdfHandler).handle(any());
        doReturn("test").when(properties).getOwnerPassword();

        handler.handle(builder.build());

        try (PDDocument document = PDDocument.load(tempFile, "20000314")) {
            assertThat(document.isEncrypted()).isTrue();
        }

    }

    @Test
    @DisplayName("잘못된 사용자 비밀번호로는 PDF를 열 수 없다.")
    void givenWrongUserPassword_whenOpeningPdf_thenThrowException() {

    }
}