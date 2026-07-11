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
class AttachmentEmailConvertPolicyTest {
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

    @Test
    @DisplayName("본문 파일 키는 COVER의 키가 반환된다.")
    void shouldReturnBodyFileKey() {
        EmailConvertPolicyContext context = contextBuilder
                .bodyKey("body.html")
                .coverKey("cover.html")
                .build();

        EmailConvertResult result = convertPolicy.convert(context);

        assertThat(result.bodyKey()).isEqualTo("cover.html");
    }


    @Test
    @DisplayName("BODY 키 정보는 첨부 파일로 변환된다.")
    void shouldConvertBodyFileKeyToAttachment() {
        EmailConvertPolicyContext context = contextBuilder
                .convertType(EnumMapperValue.fromEnumMapperType(ConvertTypeEnum.PDF))
                .bodyKey("body.html")
                .coverKey("cover.html")
                .build();

        EmailConvertResult result = convertPolicy.convert(context);

        assertThat(result.convertedAttachments())
                .filteredOn(attachment -> attachment.convertType() != ConvertTypeEnum.NONE)
                .extracting(ConvertedAttachment::key)
                .contains("body.html")
                .hasSize(1);
    }

    @Test
    @DisplayName("첨부파일 목록이 존재하면 입력된 첨부파일 개수에 변환된 첨부파일이 추가되어 생성된다.")
    void shouldCreateConvertedAttachmentInAdditionToInputAttachments() {
        ConvertedAttachment attachment = ConvertedAttachmentBuilder.builder().build();
        EmailConvertPolicyContext context = contextBuilder
                .bodyKey("body.html")
                .coverKey("cover.html")
                .attachments(List.of(attachment, attachment, attachment))
                .build();

        EmailConvertResult result = convertPolicy.convert(context);

        assertThat(result.convertedAttachments()).hasSize(4);
    }
}