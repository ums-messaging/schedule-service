package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.fixture.email.convert.EmailConvertResolveCommandBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class IdentityEmailConvertPolicyTest {
    @InjectMocks private IdentityEmailConvertPolicy convertPolicy;

    private EnumMapperValue convertTypeValue;
    private EmailConvertResolveCommandBuilder builder;

    @BeforeEach
    void setUp() {
        convertTypeValue = EnumMapperValue.fromEnumMapperType(ConvertType.NONE);
        builder = EmailConvertResolveCommandBuilder.builder()
                .body("body.html");
    }

    @Nested
    @DisplayName("Supports 테스트")
    class WhenSupports {
        @Test
        @DisplayName("변환 타입이 NONE이면 TRUE를 반환한다.")
        void shouldReturnTrue_whenConvertTypeIsNone() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertType.NONE));
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("변환 타입이 PDF이면 FALSE를 반환한다.")
        void shouldReturnFalse_whenConvertTypeIsPdf() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertType.PDF));
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("변환 타입이 HTML이면 FALSE를 반환한다.")
        void shouldReturnFalse_whenConvertTypeIsHtml() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertType.HTML));
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("본문 파일 키는 BODY 의 파일 키가 반환된다.")
    void shouldReturnBodyFileKey() {
        EmailConvertResult result = convertPolicy.convert(convertTypeValue, builder.build());

        assertThat(result.bodyKey()).isEqualTo("body.html");
    }
    @Test
    @DisplayName("변환 타입이 NONE이 아니면 예외가 발생한다.")
    void shouldThrowException_whenConvertTypeIsNone() {
        EmailConvertTypeNotSupportedException expect = EmailConvertTypeNotSupportedException.of(ConvertType.PDF);

        assertThatThrownBy(() ->
                convertPolicy.convert(
                        EnumMapperValue.fromEnumMapperType(ConvertType.PDF),
                        builder.build())
        ).isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }
}