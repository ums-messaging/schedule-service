package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import com.ums.schedule.fixture.email.convert.ConvertedAttachmentBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class IdentityEmailConvertPolicyTest {
    @InjectMocks private IdentityEmailConvertPolicy convertPolicy;

    private EmailConvertPolicyContextBuilder contextBuilder;

    @BeforeEach
    void setUp() {
        this.contextBuilder = EmailConvertPolicyContextBuilder.builder();
    }

    @Nested
    @DisplayName("Supports 테스트")
    class WhenSupports {
        @Test
        @DisplayName("변환 타입이 NONE이면 TRUE를 반환한다.")
        void shouldReturnTrue_whenConvertTypeIsNone() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.NONE));
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("변환 타입이 PDF이면 FALSE를 반환한다.")
        void shouldReturnFalse_whenConvertTypeIsPdf() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF));
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("변환 타입이 HTML이면 FALSE를 반환한다.")
        void shouldReturnFalse_whenConvertTypeIsHtml() {
            boolean result = convertPolicy.supports(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.HTML));
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("본문 파일 키는 BODY 의 파일 키가 반환된다.")
    void shouldReturnBodyFileKey() {
        EmailConvertPolicyContext context = contextBuilder
                .bodyKey("body.html")
                .coverKey("cover.html").build();

        EmailConvertResult result = convertPolicy.convert(context);

        assertThat(result.bodyKey()).isEqualTo("body.html");
    }

    @Test
    @DisplayName("첨부파일 목록이 존재하면 첨부파일 목록은 입력된 개수만큼 생성된다.")
    void shouldCreateAttachmentsForEachInputAttachment() {
        ConvertedAttachment attachment = ConvertedAttachmentBuilder.builder().build();
        EmailConvertPolicyContext context = contextBuilder
                .attachments(List.of(attachment, attachment, attachment))
                .build();

        EmailConvertResult result = convertPolicy.convert(context);

        assertThat(result.convertedAttachments()).hasSize(3);
    }
}