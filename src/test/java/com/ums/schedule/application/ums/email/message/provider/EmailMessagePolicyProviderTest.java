package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.adapter.api.request.email.request.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.email.command.EmailSendCreateRequestBuilder;
import com.ums.schedule.application.ums.email.exception.SecurityMailProcessException;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.security.SecurityMailAssembler;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.mapper.exception.EnumMapperNotFoundException;
import com.ums.schedule.fixture.email.security.EmailSecurityPolicyRequestBuilder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailMessagePolicyProviderTest {
    @Spy private EnumMapperFactory factory;
    @Mock private SecurityMailAssembler assembler;
    @InjectMocks private EmailMessagePolicyProvider provider;

    private EmailSendCreateRequestBuilder builder;

    @BeforeEach
    void setUp() {
        factory.register(EmailCode.class);
        this.builder = EmailSendCreateRequestBuilder.builder();
    }

    @Test
    @DisplayName("유효하지 않은 이메일 타입이 입력될 경우 예외가 발생한다.")
    void shouldThrowException_whenInvalidEmailType() {
        EmailSendCreateRequest request = builder.mailType("XXX").convertType(null).build();

        EnumMapperNotFoundException expect = EnumMapperNotFoundException.of(EmailCode.EMAIL_TYPE, "XXX");

        assertThatThrownBy(() -> provider.provide(request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }


    @Test
    @DisplayName("유효하지 않은 변환 타입이 입력될 경우 예외가 변환된다.")
    void shouldConvertException_whenInvalidConvertType() {
        EmailSendCreateRequest request = builder
                .mailType("PLAIN")
                .convertType("XXX").build();

        EnumMapperNotFoundException expect = EnumMapperNotFoundException.of(EmailCode.CONVERT_TYPE, "XXX");

        assertThatThrownBy(() -> provider.provide(request))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }


    @Nested
    @DisplayName("이메일 타입이 SECURITY 일 때")
    class WhenEmailTypeIsSecurity {
        @BeforeEach
        void setUp() {
            EmailSecurityPolicyRequest securityPolicy = EmailSecurityPolicyRequestBuilder.builder().build();
            builder = builder.mailType("SECURITY")
                    .securityPolicy(securityPolicy);
        }

        @Test
        @DisplayName("보안 정책 정보가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenSecurityPolicyRequestDoesNotExist() {
            EmailSendCreateRequest request = builder.securityPolicy(null).build();

            SecurityMailProcessException expect = SecurityMailProcessException.of();

            assertThatThrownBy(() -> provider.provide(request))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());

        }
        @Test
        @DisplayName("보안 정책 정보가 존재하면, 보안 메일 정보를 생성한다.")
        void shouldCreateSecurityMail_whenSecurityPolicyRequestExists() {
            EmailSendCreateRequest request = builder.build();
            SecurityMail securityMail = mock(SecurityMail.class);
            doReturn(securityMail).when(assembler).assemble(any());

            EmailPolicyResult result = provider.provide(request);

            verify(assembler).assemble(any());
            assertThat(result.securityMail()).isNotNull();
        }

        @Test
        @DisplayName("변환 타입이 입력되지 않으면, 변환 타입은 HTML을 반환한다.")
        void shouldReturnHtml() {
            EmailSendCreateRequest request = builder.convertType(null).build();
            SecurityMail securityMail = mock(SecurityMail.class);
            doReturn(securityMail).when(assembler).assemble(any());

            EmailPolicyResult result = provider.provide(request);


            assertThat(result.convertMail().getConvertType()).isEqualTo(ConvertType.HTML);
        }

        @Test
        @DisplayName("변환 타입이 HTML이면 변환 타입을 HTML을 반환한다.")
        void shouldReturnHtml_whenConvertTypeIsHtml() {
            EmailSendCreateRequest request = builder.convertType("HTML").build();
            SecurityMail securityMail = mock(SecurityMail.class);
            doReturn(securityMail).when(assembler).assemble(any());

            EmailPolicyResult result = provider.provide(request);

            assertThat(result.convertMail().getConvertType()).isEqualTo(ConvertType.HTML);
        }

        @Test
        @DisplayName("변환 타입이 PDF이면 변환 타입을 PDF를 반환한다.")
        void shouldReturnPdf_whenConvertTypeIsPdf() {
            EmailSendCreateRequest request = builder.convertType("PDF").build();
            SecurityMail securityMail = mock(SecurityMail.class);
            doReturn(securityMail).when(assembler).assemble(any());

            EmailPolicyResult result = provider.provide(request);

            assertThat(result.convertMail().getConvertType()).isEqualTo(ConvertType.PDF);
        }
    }

    @Nested
    @DisplayName("이메일 타입이 PLAIN 일 때")
    class WhenEmailTypeIsPlain {

        @BeforeEach
        void setUp() {
            builder = builder.mailType("PLAIN");
        }

        @Test
        @DisplayName("보안 메일 정보는 생성되지 않는다.")
        void shouldNotCreateSecurityMail() {
            EmailPolicyResult result = provider.provide(builder.build());

            verify(assembler, never()).assemble(any());
            assertThat(result.securityMail()).isNull();
        }

        @Test
        @DisplayName("변환 타입이 입력되지 않으면, NONE을 반환한다.")
        void shouldReturnNone() {
            EmailSendCreateRequest request = builder.convertType(null).build();

            EmailPolicyResult result = provider.provide(request);

            assertThat(result.convertMail().getConvertType()).isEqualTo(ConvertType.NONE);
        }

        @Test
        @DisplayName("변환 타입이 HTML이면 변환 타입을 HTML을 반환한다.")
        void shouldReturnHtml_whenConvertTypeIsHtml() {
            EmailSendCreateRequest request = builder.convertType("HTML").build();

            EmailPolicyResult result = provider.provide(request);

            assertThat(result.convertMail().getConvertType()).isEqualTo(ConvertType.HTML);
        }

        @Test
        @DisplayName("변환 타입이 PDF이면 변환 타입을 PDF를 반환한다.")
        void shouldReturnPdf_whenConvertTypeIsPdf() {
            EmailSendCreateRequest request = builder.convertType("PDF").build();

            EmailPolicyResult result = provider.provide(request);

            assertThat(result.convertMail().getConvertType()).isEqualTo(ConvertType.PDF);
        }
    }
}