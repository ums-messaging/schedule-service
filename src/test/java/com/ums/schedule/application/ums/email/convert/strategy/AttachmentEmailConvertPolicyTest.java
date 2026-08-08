package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.handler.EmailConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.HtmlConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.PdfConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.PdfSecurityHandler;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.AttachmentEmailConvertPolicy;

import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import com.ums.schedule.fixture.email.EmailTemplateContentBuilder;
import com.ums.schedule.fixture.email.RenderedTemplateBuilder;
import com.ums.schedule.fixture.email.RenderedTemplateContentBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AttachmentEmailConvertPolicyTest {
    @InjectMocks private AttachmentEmailConvertPolicy convertPolicy;

    @Spy private List<EmailConvertHandler> handlers = new ArrayList<>();

    @Mock private AwsS3Repository fileRepository;
    @Mock private PdfSecurityHandler securityHandler;
    @Mock private PdfConvertHandler pdfHandler;
    @Mock private HtmlConvertHandler htmlHandler;

    private RenderedTemplateBuilder templateBuilder;


    @BeforeEach
    void setUp() {
        handlers.add(securityHandler);
        handlers.add(pdfHandler);
        handlers.add(htmlHandler);
        templateBuilder = RenderedTemplateBuilder.builder();
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
    @DisplayName("예외 발생 시")
    class WhenThrowException {
        @Test
        @DisplayName("커버가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenConvertTemplateDoesNotExist() {
            RenderedTemplate template = templateBuilder.cover(null).build();

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailContentMissingException.class);
        }

        @Test
        @DisplayName("바디가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateDoesNotExist() {
            RenderedTemplate template = templateBuilder.body(null).build();

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailContentMissingException.class);
        }

        @Test
        @DisplayName("핸들러가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenHandlerIsNull() {
            RenderedTemplate template = templateBuilder.build();

            doReturn(false).when(pdfHandler).supports(any(), any());
            doReturn(false).when(htmlHandler).supports(any(), any());


            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailMessageConvertException.class);
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class WhenConvert {

        @BeforeEach
        void setUp() throws IOException {
            doReturn(true).when(pdfHandler).supports(any(), any());
            doReturn(mock(File.class)).when(pdfHandler).handle(any());
            doReturn("filekey").when(fileRepository).upload(any(), anyString());
        }


        @Test
        @DisplayName("이메일 본문은 커버 템플릿으로 반환된다.")
        void shouldReturnCoverTemplate() {
            RenderedTemplate template = templateBuilder.build();

            EmailConvertPolicy policy = convertPolicy.convert(template, mock(TargetMessageData.class));

            assertThat(policy.bodyTemplate()).isEqualTo(template.cover().template());
        }

        @Test
        @DisplayName("바디 템플릿은 첨부 파일에 포함되어 반환된다.")
        void shouldReturnAttachmentContainsBodyTemplate() {
            RenderedTemplateContent attachment = RenderedTemplateContentBuilder.builder().build();
            RenderedTemplate template = templateBuilder
                    .attachments(List.of(attachment, attachment, attachment))
                    .build();
            EmailConvertPolicy policy = convertPolicy.convert(template, mock(TargetMessageData.class));

            assertThat(policy.attachments())
                    .extracting(RenderedTemplateContent::fileKey)
                    .contains(template.body().fileKey())
                    .hasSize(4);
        }


        @Test
        @DisplayName("파일 업로드가 실행된다.")
        void shouldExecuteFileUpload()  {
            RenderedTemplate template = templateBuilder.build();

            convertPolicy.convert(template, mock(TargetMessageData.class));

            verify(fileRepository).upload(any(), any());
        }

        @Test
        @DisplayName("핸들러가 실행된다.")
        void shouldExecuteHandler() throws IOException {
            RenderedTemplate template = templateBuilder.build();

            convertPolicy.convert(template, mock(TargetMessageData.class));

            verify(pdfHandler).handle(any());
        }
    }
}