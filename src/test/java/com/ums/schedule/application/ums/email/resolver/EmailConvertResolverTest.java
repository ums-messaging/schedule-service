package com.ums.schedule.application.ums.email.resolver;

import com.ums.schedule.application.ums.email.convert.strategy.AttachmentEmailConvertPolicy;
import com.ums.schedule.application.ums.email.convert.strategy.EmailMessageConvertStrategy;
import com.ums.schedule.application.ums.email.convert.strategy.IdentityEmailConvertPolicy;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.exception.ConvertTypeNotSupportedException;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.fixture.email.attachment.AttachmentContextBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertResultBuilder;
import com.ums.schedule.fixture.message.EmailConvertPolicyCommandBuilder;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.resolver.EmailConvertResolver;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailConvertResolverTest {
    @Mock private EnumMapperFactory mapperFactory;
    @Mock private IdentityEmailConvertPolicy identityPolicy;
    @Mock private AttachmentEmailConvertPolicy attachmentPolicy;

    @Spy private List<EmailMessageConvertStrategy> policyList = new ArrayList<>();
    @InjectMocks private EmailConvertResolver resolver;

    private EmailConvertPolicyCommandBuilder builder;

    @BeforeEach
    void setUp() {
        policyList.add(identityPolicy);
        policyList.add(attachmentPolicy);
    }

    @Nested
    @DisplayName("변환 타입이 입력되지 않았을 때")
    class WhenConvertTypeIsNull {
        @BeforeEach
        void setUp() {
            builder = EmailConvertPolicyCommandBuilder.builder().convertType(null);
            doReturn(true).when(identityPolicy).supports(any());
            doReturn(mock(EmailConvertResult.class)).when(identityPolicy).convert(any(), any());
        }

        @Test
        @DisplayName("보안 정책이 존재하지 않으면 convert_type은 NONE이 반환된다.")
        void shouldReturnNone_whenSecurityPolicyDoesNotExist() {
            EmailConvertResolveCommand givenCommand = builder.build();

            EmailConvertPolicy result = resolver.resolve(givenCommand, null);

            assertThat(result.convertType()).isEqualTo(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
        }

        @Test
        @DisplayName("보안 정책이 존재하면 convert_type은 HTML이 반환된다.")
        void shouldReturnHtml_whenSecurityPolicyExists() {
            EmailConvertResolveCommand givenCommand = builder.build();

            EmailConvertPolicy result = resolver.resolve(givenCommand, mock(SecurityMail.class));

            assertThat(result.convertType()).isEqualTo(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML));
        }
    }

    @Nested
    @DisplayName("변환 타입이 HTML일 때")
    class WhenConvertTypeIsHtml {
        @BeforeEach
        void setUp() {
            builder = EmailConvertPolicyCommandBuilder.builder().convertType("HTML");
            doReturn(true).when(identityPolicy).supports(any());
            doReturn(mock(EmailConvertResult.class)).when(identityPolicy).convert(any(), any());
            doReturn(mock(EnumMapperValue.class)).when(mapperFactory).findEnumMapperValue(any(), any());
        }

        @Test
        @DisplayName("보안 메일이 존재하지 않아도 변환 타입은 HTML을 반환한다.")
        void shouldReturnHtml_whenSecurityMailDoesNotExist() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            EmailConvertResolveCommand givenCommand = builder.build();

            resolver.resolve(givenCommand, null);

            verify(mapperFactory).findEnumMapperValue(any(), captor.capture());
            assertThat(captor.getValue()).isEqualTo("HTML");
        }

        @Test
        @DisplayName("보안 메일이 존재해도 변환 타입은 HTML을 반환한다.")
        void shouldReturnHtml_whenSecurityMailExists() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            EmailConvertResolveCommand givenCommand = builder.build();

            resolver.resolve(givenCommand, mock(SecurityMail.class));

            verify(mapperFactory).findEnumMapperValue(any(), captor.capture());
            assertThat(captor.getValue()).isEqualTo("HTML");
        }
    }

    @Nested
    @DisplayName("변환 타입이 PDF 일 때")
    class WhenConvertTypeIsPDF {
        @BeforeEach
        void setUp() {
            builder = EmailConvertPolicyCommandBuilder.builder().convertType("PDF");
            doReturn(true).when(identityPolicy).supports(any());
            doReturn(mock(EmailConvertResult.class)).when(identityPolicy).convert(any(), any());
            doReturn(mock(EnumMapperValue.class)).when(mapperFactory).findEnumMapperValue(any(), any());
        }

        @Test
        @DisplayName("보안 메일이 존재하지 않아도 변환 타입은 PDF를 반환한다.")
        void shouldReturnHtml_whenSecurityMailDeosNotExist() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            EmailConvertResolveCommand givenCommand = builder.build();

            resolver.resolve(givenCommand, null);

            verify(mapperFactory).findEnumMapperValue(any(), captor.capture());
            assertThat(captor.getValue()).isEqualTo("PDF");
        }

        @Test
        @DisplayName("보안 메일이 존재해도 변환 타입은 PDF를 반환한다.")
        void shouldReturnPdf_whenSecurityMailExists() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            EmailConvertResolveCommand givenCommand = builder.build();

            resolver.resolve(givenCommand, mock(SecurityMail.class));

            verify(mapperFactory).findEnumMapperValue(any(), captor.capture());
            assertThat(captor.getValue()).isEqualTo("PDF");
        }
    }

    @Test
    @DisplayName("본문은 변환 정책에서 반환한 키가 반환된다.")
    void shouldReturnEmailConvertPolicyBodyKey() {
        EmailConvertResolveCommand command = EmailConvertPolicyCommandBuilder.builder()
                .body(givenContext("body.html"))
                .build();
        EmailConvertResult givenResult = EmailConvertResultBuilder.builder().bodyKey("cover.html").build();
        doReturn(true).when(identityPolicy).supports(any());
        doReturn(givenResult).when(identityPolicy).convert(any(), any());

        EmailConvertPolicy result = resolver.resolve(command, mock(SecurityMail.class));

        assertThat(result.bodyKey()).isEqualTo(givenResult.bodyKey());
    }

    private AttachmentContext givenContext(String fileKey) {
        return AttachmentContextBuilder.builder()
                .key(fileKey)
                .build();
    }

    @Test
    @DisplayName("지원되는 변환 타입이 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenNotSupportedConvertType() {
        EmailConvertResolveCommand command = EmailConvertPolicyCommandBuilder.builder()
                .body(givenContext("body.html"))
                .build();

        doReturn(false).when(identityPolicy).supports(any());
        doReturn(false).when(attachmentPolicy).supports(any());

        ConvertTypeNotSupportedException expect = ConvertTypeNotSupportedException.of();

        assertThatThrownBy(() -> resolver.resolve(command, mock(SecurityMail.class)))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }
}