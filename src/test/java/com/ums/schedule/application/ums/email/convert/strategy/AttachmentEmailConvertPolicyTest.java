package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.handler.EmailConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.HtmlConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.PdfConvertHandler;
import com.ums.schedule.application.ums.email.generator.handler.PdfSecurityHandler;
import com.ums.schedule.application.ums.email.generator.policy.AttachmentEmailConvertPolicy;

import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.template.exception.EmailTemplateNotConfiguredException;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.config.properties.EmailTemplateProperties;
import com.ums.schedule.domain.message.email.exception.EmailContentMissingException;
import com.ums.schedule.domain.target.message.AttachmentPayload;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import com.ums.schedule.fixture.email.EmailTemplateContentBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Mock private EmailTemplateProperties properties;

    private EmailTemplateBuilder templateBuilder;
    private TargetMessageData targetMessageData;


    @BeforeEach
    void setUp() throws IOException {
        handlers.add(securityHandler);
        handlers.add(pdfHandler);
        handlers.add(htmlHandler);
        templateBuilder = EmailTemplateBuilder.builder()
                .convertType(ConvertType.PDF)
                .cover(givenEmailContent("cover.html", "cover_message"))
                .body(givenEmailContent("body.html", "body_message"))
                .attachmentList(givenAttachmentList());
        givenTargetData();
    }
    private EmailTemplateBuilder givenEmailTemplate() throws IOException {
        templateBuilder = EmailTemplateBuilder.builder()
                .convertType(ConvertType.PDF)
                .cover(givenEmailContent("cover.html", "cover_message"))
                .body(givenEmailContent("body.html", "body_message"))
                .attachmentList(givenAttachmentList());
        return templateBuilder;
    }
    private EmailTemplateContent givenEmailContent(String fileKey, String message) throws IOException {
        Template template = new Template("test_template", message, new Configuration(Configuration.VERSION_2_3_32));
        return EmailTemplateContentBuilder.builder()
                .template(template)
                .fileKey(fileKey)
                .attachmentName("${attachmentName}.pdf")
                .downloadName("${targetName}.pdf")
                .build();
    }
    private List<EmailTemplateContent> givenAttachmentList() {
        EmailTemplateContent attachment = EmailTemplateContentBuilder.builder()
                .fileKey("attachment.pdf")
                .attachmentName("attachment.pdf")
                .downloadName("download.pdf")
                .build();
        return List.of(
                attachment, attachment, attachment
        );
    }

    private void givenTargetData() {
        Map<SendTargetColumn, String> targetData = Map.of(
                SendTargetColumn.TARGET_KEY, "hyejin"
        );
        this.targetMessageData = new TargetMessageData(
                "hyejin_company",
                1,
                targetData,
                Map.of()
        );
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
            EmailTemplate template = templateBuilder.cover(null).build();

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailContentMissingException.class);
        }

        @Test
        @DisplayName("바디가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateDoesNotExist() {
            EmailTemplate template = templateBuilder.body(null).build();

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailContentMissingException.class);
        }

        @Test
        @DisplayName("템플릿 업로드 경로가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadPrefixDoesNotExist() throws IOException {
            doReturn(true).when(htmlHandler).supports(any(), any());
            doReturn(null).when(properties).getTemplateKeyPrefix();
            doReturn(mock(File.class)).when(htmlHandler).handle(any());
            assertThatThrownBy(() -> convertPolicy.convert(templateBuilder.build(), targetMessageData))
                    .isInstanceOf(EmailTemplateNotConfiguredException.class);
        }

        @Test
        @DisplayName("첨부파일 업로드 경로가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenAttachmentUploadDirDoesNotExist() throws IOException {
            doReturn("template").when(properties).getTemplateKeyPrefix();
            doReturn(true).when(htmlHandler).supports(any(), any());
            doReturn(mock(File.class)).when(htmlHandler).handle(any());
            doReturn(null).when(properties).getAttachmentKeySuffix();
            assertThatThrownBy(() -> convertPolicy.convert(templateBuilder.build(), targetMessageData))
                    .isInstanceOf(EmailTemplateNotConfiguredException.class);
        }

        @Test
        @DisplayName("핸들러가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenHandlerIsNull() {
            EmailTemplate template = templateBuilder.build();

            doReturn(false).when(pdfHandler).supports(any(), any());
            doReturn(false).when(htmlHandler).supports(any(), any());

            assertThatThrownBy(() -> convertPolicy.convert(template, mock(TargetMessageData.class)))
                    .isInstanceOf(EmailMessageConvertException.class);
        }
    }

    @Nested
    @DisplayName("변환 테스트")
    class WhenConvert {
        private EmailTemplate template;

        @BeforeEach
        void setUp() throws IOException {
            givenTargetData();
            template = givenEmailTemplate().build();
            doReturn(true).when(pdfHandler).supports(any(), any());
            doReturn(mock(File.class)).when(pdfHandler).handle(any());
            doReturn("filekey").when(fileRepository).upload(any(), anyString());
            doReturn("attachment").when(properties).getAttachmentKeySuffix();
            doReturn("template").when(properties).getTemplateKeyPrefix();
        }

        @Test
        @DisplayName("이메일 본문은 커버 템플릿으로 반환된다.")
        void shouldReturnCoverTemplate()  {
            EmailConvertPolicy policy = convertPolicy.convert(template, targetMessageData);

            assertThat(policy.bodyTemplate()).isEqualTo(template.getCover().template());
        }

        @Test
        @DisplayName("바디 템플릿은 첨부 파일에 포함되어 반환된다.")
        void shouldReturnAttachmentContainsBodyTemplate() {
            EmailConvertPolicy policy = convertPolicy.convert(template, targetMessageData);
            String expectedFileKey = "template/template_key/hyejin_company/attachment/hyejin.pdf";

            assertThat(policy.attachments())
                    .extracting(AttachmentPayload::fileKey)
                    .contains(expectedFileKey, "attachment.pdf")
                    .hasSize(4);
        }


        @Test
        @DisplayName("파일 업로드가 실행된다.")
        void shouldExecuteFileUpload()  {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

            convertPolicy.convert(template, targetMessageData);

            verify(fileRepository).upload(any(), captor.capture());
            String fileKey = captor.getValue();

            assertThat(fileKey).isEqualTo("template/template_key/hyejin_company/attachment/hyejin.pdf");
        }

        @Test
        @DisplayName("핸들러가 실행된다.")
        void shouldExecuteHandler() throws IOException {
            convertPolicy.convert(template, targetMessageData);

            verify(pdfHandler).handle(any());
        }
    }
}