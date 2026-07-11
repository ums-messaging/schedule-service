package com.ums.schedule.application.message.email.resolver;

import com.ums.schedule.application.ums.email.convert.resolver.EmailConvertResolver;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailMessageConvertPolicyResolverTest {
    @Mock private EnumMapperFactory factory;
    @InjectMocks private EmailConvertResolver resolver;

    @Nested
    @DisplayName("변환 타입이 입력되지 않았을 때")
    class WhenConvertTypeIsNull {
        @Test
        @DisplayName("변환 타입이 입력되지 않고, 보안 정책이 존재하지 않으면 변환 타입은 NONE을 반환한다.")
        void shouldReturnNone_whenConvertTypeAndSecurityPolicyDoNotExist(){

        }

        @Test
        @DisplayName("변환 타입이 입력되지 않고, 보안 정책이 존재하면 변환 타입은 HTML을 반환한다.")
        void shouldReturnHtml_whenConvertTypeDoesNotExistAndSecurityPolicyExists() {

        }

    }

    @Nested
    @DisplayName("변환 타입이 PDF 일 때")
    class WhenConvertTypeIsPdf {

    }

    @Nested
    @DisplayName("변환 타입이 HTML 일 때")
    class WhenConvertTypeIsHtml {
        @Test
        @DisplayName("본문 파일 키로 cover의 fileKey가 반환된다.")
        void shouldReturnCoverFileKey() {

        }

        @Test
        @DisplayName("첨부파일로 변환된다.")
        void shouldConvertAttachment() {

        }

        @Test
        @DisplayName("보안 정책이 존재하면 변환된 첨부파일에 보안정보가 생성된다.")
        void shouldCreateSecurityPolicy_whenSecurityPolicyExists() {

        }

        @Test
        @DisplayName("첨부파일 목록이 존재하면 입력된 첨부파일 개수에 변환된 첨부파일이 추가되어 생성된다.")
        void shouldAddAttachment() {

        }
    }

    @Nested
    @DisplayName("변환 타입이 NONE일 때")
    class WhenConvertTypeIsNone {
        @Test
        @DisplayName("본문 파일 키로 body의 fileKey가 반환된다.")
        void shouldReturnBodyFileKey() {

        }
        @Test
        @DisplayName("첨부파일 목록이 존재하면 첨부파일 목록은 입력된 개수만큼 생성된다.")
        void shouldCreateAttachmentList() {

        }
        @Test
        @DisplayName("입력된 첨부파일의 변환 타입은 NONE이다.")
        void shouldReturnNone_whenAttachmentList() {

        }
    }

}