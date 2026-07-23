package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.config.EmailMessageProperties;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.exception.EmailPolicyViolationException;
import com.ums.schedule.application.ums.email.template.exception.EmailTemplateNotConfiguredException;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import com.ums.schedule.fixture.email.attachment.AttachmentContextBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertResolveCommandBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AttachmentEmailConvertPolicyTest {
    @Mock private EmailMessageProperties properties;
    @InjectMocks private AttachmentEmailConvertPolicy convertPolicy;

    private EnumMapperValue convertTypeValue;
    private EmailConvertResolveCommandBuilder builder;

    @BeforeEach
    void setUp() {
        convertTypeValue = EnumMapperValue.fromEnumMapperType(ConvertType.PDF);
        builder = EmailConvertResolveCommandBuilder.builder()
                .body("body.html")
                .cover("cover.html");
    }

    private AttachmentContext givenContext(String fileKey) {
        return AttachmentContextBuilder.builder()
                .key(fileKey)
                .build();
    }

    @Nested
    @DisplayName("Supports 테스트")
    class WhenSupports {
        @Test
        @DisplayName("변환 타입이 NONE이면 FALSE를 반환한다.")
        void shouldReturnFalse() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertType.NONE));
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("변환 타입이 PDF이면 TRUE를 반환한다.")
        void shouldReturnTrue_whenConvertTypeIsPdf() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertType.PDF));
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("변환 타입이 HTML이면 TRUE를 반환한다.")
        void shouldReturnFalse_whenConvertTypeIsHtml() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertType.HTML));
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class WhenConvert {
        @BeforeEach
        void setUp() {
            doReturn("${template}").when(properties).getConvertFileKeyTemplate();
        }

        @Test
        @DisplayName("설정 파일에서 파일 템플릿 키가 조회된다.")
        void shouldGetConfiguredFileTemplateKey() {
            convertPolicy.convert(convertTypeValue, builder.build());

            verify(properties).getConvertFileKeyTemplate();
        }

        @Test
        @DisplayName("커버 정보가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenConvertTemplateDoesNotExist() {
            EmailConvertResolveCommand command = builder
                    .cover(null).build();

            EmailContentMissingException expect =
                    EmailContentMissingException.of(EmailMessageSection.COVER);

            assertThatThrownBy(() -> convertPolicy.convert(convertTypeValue, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("본문 파일 키는 COVER의 키가 반환된다.")
        void shouldReturnBodyFileKey() {
            EmailConvertResult result = convertPolicy.convert(convertTypeValue, builder.build());

            assertThat(result.bodyKey()).isEqualTo("cover.html");
        }

        @Test
        @DisplayName("변환된 첨부파일의 키는 BODY 키 정보로 반환된다.")
        void shouldConvertBodyFileKeyToAttachment() {
            EmailConvertResult result = convertPolicy.convert(convertTypeValue, builder.build());

            assertThat(result.convertedAttachment().fileKey())
                    .isEqualTo("body.html");
        }

        @Test
        @DisplayName("파일 템플릿 키는 설정 파일에서 조회한 값과 변환 타입으로 조립되어 반환된다.")
        void shouldReturnStartWithConfiguredValue() {
            EmailConvertResult result = convertPolicy.convert(convertTypeValue, builder.build());

            assertThat(result.convertedAttachment().fileKeyTemplate()).isEqualTo("${template}.pdf");
        }
    }

    @Nested
    @DisplayName("설정 파일이 조회한 값이 존재하지 않을 때")
    class WhenConfiguredValueIsEmpty {
        @Test
        @DisplayName("설정 파일에서 조회한 값이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenConfiguredValueIsEmpty() {
            doReturn("").when(properties).getConvertFileKeyTemplate();

            EmailTemplateNotConfiguredException expect = EmailTemplateNotConfiguredException.of(EmailMessageErrorCode.NOT_CONFIGURED_FILE_KEY_TEMPLATE);

            assertThatThrownBy(() -> convertPolicy.convert(convertTypeValue, builder.build()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("변환 타입이 NONE이면 예외가 발생한다.")
        void shouldThrowException_whenConvertTypeIsNone() {
            EmailConvertTypeNotSupportedException expect = EmailConvertTypeNotSupportedException.of(ConvertType.NONE);

            assertThatThrownBy(() ->
                        convertPolicy.convert(
                                EnumMapperValue.fromEnumMapperType(ConvertType.NONE),
                                builder.build())
                ).isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }
}