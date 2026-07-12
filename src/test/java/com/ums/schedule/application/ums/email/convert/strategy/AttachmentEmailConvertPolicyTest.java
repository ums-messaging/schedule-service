package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.exception.ConvertMessageNotConfiguredException;
import com.ums.schedule.application.ums.email.config.EmailMessageProperties;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.fixture.email.attachment.AttachmentContextBuilder;
import com.ums.schedule.fixture.email.attachment.SecurityMailBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyContextBuilder;
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

    private EmailConvertPolicyContextBuilder contextBuilder;

    @BeforeEach
    void setUp() {
        contextBuilder = EmailConvertPolicyContextBuilder.builder();
    }

    @Nested
    @DisplayName("Supports 테스트")
    class WhenSupports {
        @Test
        @DisplayName("변환 타입이 NONE이면 FALSE를 반환한다.")
        void shouldReturnFalse() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("변환 타입이 PDF이면 TRUE를 반환한다.")
        void shouldReturnTrue_whenConvertTypeIsPdf() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF));
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("변환 타입이 HTML이면 TRUE를 반환한다.")
        void shouldReturnFalse_whenConvertTypeIsHtml() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML));
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class WhenConvert {
        @BeforeEach
        void setUp() {
            contextBuilder = contextBuilder
                    .convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                    .body(givenContext("body.html"))
                    .coverKey("cover.html");
        }
        @Test
        @DisplayName("설정 파일에서 파일 템플릿 키가 조회된다.")
        void shouldGetConfiguredFileTemplateKey() {
            doReturn("${template}").when(properties).getConvertFileKeyTemplate();
            EmailConvertPolicyContext context = contextBuilder.build();

            convertPolicy.convert(context);

            verify(properties).getConvertFileKeyTemplate();
        }

        @Test
        @DisplayName("본문 파일 키는 COVER의 키가 반환된다.")
        void shouldReturnBodyFileKey() {
            doReturn("${template}").when(properties).getConvertFileKeyTemplate();
            EmailConvertPolicyContext context = contextBuilder
                    .body(givenContext("body.html"))
                    .coverKey("cover.html")
                    .build();

            EmailConvertResult result = convertPolicy.convert(context);

            assertThat(result.bodyKey()).isEqualTo("cover.html");
        }

        @Test
        @DisplayName("변환된 첨부파일의 키는 BODY 키 정보로 반환된다.")
        void shouldConvertBodyFileKeyToAttachment() {
            doReturn("${template}").when(properties).getConvertFileKeyTemplate();
            EmailConvertPolicyContext context = contextBuilder
                    .convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                    .build();

            EmailConvertResult result = convertPolicy.convert(context);

            assertThat(result.convertedAttachment().fileKey())
                    .isEqualTo("body.html");
        }

        @Test
        @DisplayName("파일 템플릿 키는 설정 파일에서 조회한 값과 변환 타입으로 조립되어 반환된다.")
        void shouldReturnStartWithConfiguredValue() {
            doReturn("${template}").when(properties).getConvertFileKeyTemplate();

            EmailConvertPolicyContext context = contextBuilder.convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                    .build();

            EmailConvertResult result = convertPolicy.convert(context);

            assertThat(result.convertedAttachment().fileKeyTemplate()).isEqualTo("${template}.pdf");
        }


        @Test
        @DisplayName("설정 파일에서 조회한 값이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenConfiguredValueIsEmpty() {
            doReturn("").when(properties).getConvertFileKeyTemplate();
            EmailConvertPolicyContext context = contextBuilder.convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                    .build();

            ConvertMessageNotConfiguredException expect = ConvertMessageNotConfiguredException.of("convert.file_key_template");

            assertThatThrownBy(() -> convertPolicy.convert(context))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("보안 정책이 존재하면, 변환된 첨부파일에 적용되어 반환된다.")
        void shouldReturnSecurityMailPolicy_whenSecurityMailExists() {
            doReturn("${template}").when(properties).getConvertFileKeyTemplate();
            EmailConvertPolicyContext context = contextBuilder
                    .securityMail(givenSecurityMail())
                    .build();

            EmailConvertResult result = convertPolicy.convert(context);

            assertThat(result.securityMail()).isNotNull();
        }

        private SecurityMail givenSecurityMail() {
            return SecurityMailBuilder.builder().build();
        }

        private AttachmentContext givenContext(String fileKey) {
            return AttachmentContextBuilder.builder()
                    .key(fileKey)
                    .build();
        }
    }


}