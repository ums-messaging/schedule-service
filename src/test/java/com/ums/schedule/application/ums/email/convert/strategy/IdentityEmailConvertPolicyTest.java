package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.IdentityEmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import com.ums.schedule.fixture.email.EmailTemplateContentBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class IdentityEmailConvertPolicyTest {
    @InjectMocks private IdentityEmailConvertPolicy convertPolicy;

    private EmailTemplateBuilder templateBuilder;
    private EnumMapperValue convertTypeValue;

    @BeforeEach
    void setUp() throws IOException {
        convertTypeValue = EnumMapperValue.fromEnumMapperType(ConvertType.NONE);
        templateBuilder = generateEmailTemplate();
    }
    private EmailTemplateBuilder generateEmailTemplate() throws IOException {
        return EmailTemplateBuilder.builder()
                .body(createTemplate("body_template"));
    }

    private EmailTemplateContent createTemplate(String template) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        return EmailTemplateContentBuilder.builder()
                .template(new Template("test", template, configuration))
                .fileKeyTemplate("${targetKey}.pdf")
                .fileKey("body.pdf")
                .build();
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
            EmailTemplate template = templateBuilder.build();

            EmailConvertPolicy policy = convertPolicy.convert(template, mock(TargetMessageData.class));

            assertThat(policy.bodyTemplate().toString()).isEqualTo("body_template");
        }

        @Test
        @DisplayName("첨부파일은 입력된 개수만큼 반환된다.")
        void shouldReturnAttachmentList() {
            EmailTemplateContent attachment = EmailTemplateContentBuilder.builder()
                    .fileKey("attachment.pdf")
                    .build();
            EmailTemplate template = templateBuilder.attachmentList(List.of(attachment, attachment, attachment)).build();

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
            EmailTemplate template = templateBuilder.body(null).build();

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailContentMissingException.class);
        }
    }
}