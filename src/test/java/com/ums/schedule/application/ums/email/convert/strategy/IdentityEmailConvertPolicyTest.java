package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.IdentityEmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import com.ums.schedule.fixture.email.RenderedTemplateBuilder;
import com.ums.schedule.fixture.email.RenderedTemplateContentBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IdentityEmailConvertPolicyTest {
    @InjectMocks private IdentityEmailConvertPolicy convertPolicy;

    private EnumMapperValue convertTypeValue;
    private RenderedTemplateBuilder templateBuilder;

    @BeforeEach
    void setUp() {
        convertTypeValue = EnumMapperValue.fromEnumMapperType(ConvertType.NONE);
        templateBuilder = RenderedTemplateBuilder.builder()
                .body(RenderedTemplateContentBuilder
                        .builder()
                        .template("body_template")
                        .build()
                )
        ;
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

    @Nested
    @DisplayName("변환 테스트")
    class WhenConvert {

        @Test
        @DisplayName("본문 파일 키는 BODY 의 파일 키가 반환된다.")
        void shouldReturnBodyFileKey() {
            RenderedTemplate template = templateBuilder.build();

            EmailConvertPolicy policy = convertPolicy.convert(template, mock(TargetMessageData.class));

            assertThat(policy.bodyTemplate()).isEqualTo("body_template");
        }

        @Test
        @DisplayName("첨부파일은 입력된 개수만큼 반환된다.")
        void shouldReturnAttachmentList() {
            RenderedTemplateContent attachment = RenderedTemplateContentBuilder.builder().build();
            RenderedTemplate template = templateBuilder.attachments(List.of(attachment, attachment, attachment)).build();

            EmailConvertPolicy policy = convertPolicy.convert(template, mock(TargetMessageData.class));

            assertThat(policy.attachments())
                    .hasSize(3);
        }
    }

    @Nested
    @DisplayName("예외 발생 시 ")
    class WhenThrowException {
        @Test
        @DisplayName("바디가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateDoesNotExist() {
            RenderedTemplate template = templateBuilder.body(null).build();

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailContentMissingException.class);
        }
    }
}