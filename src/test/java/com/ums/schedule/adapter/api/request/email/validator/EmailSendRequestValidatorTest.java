package com.ums.schedule.adapter.api.request.email.validator;

import com.ums.schedule.adapter.api.request.email.request.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.email.command.EmailSendCreateRequestBuilder;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EmailSendRequestValidatorTest {
    @InjectMocks private EmailSendRequestValidator validator;
    private EmailSendCreateRequestBuilder builder;

    @BeforeEach
    void setUp() {
        this.builder = EmailSendCreateRequestBuilder.builder();
    }

    @Nested
    @DisplayName("첨부파일 명을 입력하지 않았을 때")
    class WhenAttachmentNameIsEmpty {
        @BeforeEach
        void setUp() {
            builder = builder.attachmentName("")
                    .downloadName("download_name.xlsx");
        }
        @Test
        @DisplayName("변환 타입이 PDF일 때 에러가 반환된다.")
        void shouldReturnFieldError_whenConvertTypeIsPDF() {
            EmailSendCreateRequest request = builder
                    .convertType("PDF")
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            FieldError fieldError = error.getFieldError();
            assertThat(fieldError)
                    .extracting(FieldError::getField, FieldError::getCode)
                    .contains("attachmentName", "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED");
        }

        @Test
        @DisplayName("변환 타입이 HTML일 때 에러가 반환된다.")
        void shouldReturnFieldError_whenConvertTypeIsHTML() {
            EmailSendCreateRequest request = builder
                    .convertType("HTML")
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            FieldError fieldError = error.getFieldError();
            assertThat(fieldError)
                    .extracting(FieldError::getField, FieldError::getCode)
                    .contains("attachmentName", "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED");
        }

        @Test
        @DisplayName("변환 타입이 NONE일 때 에러는 존재하지 않는다.")
        void shouldReturnFalse_whenConvertTypeIsNone() {
            EmailSendCreateRequest request = builder
                    .convertType("NONE")
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

           assertThat(error.hasErrors()).isFalse();
        }

        @Test
        @DisplayName("변환 타입이 NULL일 때 에러는 존재하지 않는다.")
        void shouldReturnFalse_whenConvertTypeIsNull() {
            EmailSendCreateRequest request = builder
                    .convertType(null)
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            assertThat(error.hasErrors()).isFalse();
        }

        @Test
        @DisplayName("이메일 타입이 보안메일이면 변환타입 상관없이 에러를 반환한다.")
        void shouldReturnFieldError_whenSecurityPolicyExists() {
            EmailSendCreateRequest request = builder
                    .convertType(null)
                    .securityPolicy(mock(EmailSecurityPolicyRequest.class))
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            FieldError fieldError = error.getFieldError();
            assertThat(fieldError)
                    .extracting(FieldError::getField, FieldError::getCode)
                    .contains("attachmentName", "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED");
        }
    }
    @Nested
    @DisplayName("다운로드 명을 입력하지 않았을 때")
    class WhenDownloadNameIsEmpty {
        @BeforeEach
        void setUp() {
            builder = builder
                    .attachmentName("attachment_name.xlsx")
                    .downloadName("");
        }
        @Test
        @DisplayName("변환 타입이 PDF일 때 에러가 반환된다.")
        void shouldReturnFieldError_whenConvertTypeIsPDF() {
            EmailSendCreateRequest request = builder
                    .convertType("PDF")
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            FieldError fieldError = error.getFieldError();
            assertThat(fieldError)
                    .extracting(FieldError::getField, FieldError::getCode)
                    .contains("downloadName", "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED");
        }

        @Test
        @DisplayName("변환 타입이 HTML일 때 에러가 반환된다.")
        void shouldReturnFieldError_whenConvertTypeIsHTML() {
            EmailSendCreateRequest request = builder
                    .convertType("HTML")
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            FieldError fieldError = error.getFieldError();
            assertThat(fieldError)
                    .extracting(FieldError::getField, FieldError::getCode)
                    .contains("downloadName", "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED");
        }

        @Test
        @DisplayName("변환 타입이 NONE일 때 에러는 존재하지 않는다.")
        void shouldReturnFalse_whenConvertTypeIsNone() {
            EmailSendCreateRequest request = builder
                    .convertType("NONE")
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            assertThat(error.hasErrors()).isFalse();
        }

        @Test
        @DisplayName("변환 타입이 NULL일 때 에러는 존재하지 않는다.")
        void shouldReturnFalse_whenConvertTypeIsNull() {
            EmailSendCreateRequest request = builder
                    .convertType(null)
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            assertThat(error.hasErrors()).isFalse();
        }

        @Test
        @DisplayName("보안 정책이 존재하면 변환타입 상관없이 에러를 반환한다.")
        void shouldReturnFieldError_whenSecurityPolicyExists() {
            EmailSendCreateRequest request = builder
                    .mailType("SECURITY")
                    .convertType(null)
                    .securityPolicy(mock(EmailSecurityPolicyRequest.class))
                    .build();

            Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

            validator.validate(request, error);

            FieldError fieldError = error.getFieldError();
            assertThat(fieldError)
                    .extracting(FieldError::getField, FieldError::getCode)
                    .contains("downloadName", "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED");
        }
    }

    @Test
    @DisplayName("메일 타입이 SECURITY이고, 보안 정책이 존재하지 않으면 에러를 반환한다.")
    void shouldReturnFieldError_whenMailTypeIsSecurityAndExistSecurityPolicy() {
        EmailSendCreateRequest request = builder
                .mailType("SECURITY")
                .convertType(null)
                .securityPolicy(null)
                .build();

        Errors error = new BeanPropertyBindingResult(request, "emailSendRequest");

        validator.validate(request, error);

        FieldError fieldError = error.getFieldError();
        assertThat(fieldError)
                .extracting(FieldError::getField, FieldError::getCode)
                .contains("securityPolicy", "EMAIL_SEND_REQUEST:SECURITY_POLICY_NOT_NULL");
    }
}