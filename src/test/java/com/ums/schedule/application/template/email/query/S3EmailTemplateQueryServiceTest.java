package com.ums.schedule.application.template.email.query;

import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.exception.email.attachment.EmailAttachmentFileNotFoundException;
import com.ums.schedule.application.exception.template.TemplateNotConfiguredException;
import com.ums.schedule.application.ums.email.template.query.S3EmailTemplateQueryService;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateContentResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.application.ums.email.template.query.model.EmailAttachmentDetailQuery;
import com.ums.schedule.config.properties.EmailTemplateProperties;
import com.ums.schedule.common.code.email.EmailUploadPrefixType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.fixture.template.EmailAttachmentCreateCommandBuilder;
import com.ums.schedule.fixture.template.EmailTemplateCreateCommandBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3EmailTemplateQueryServiceTest {
    @Mock private AwsS3Repository fileRepository;
    @Mock private EmailTemplateProperties properties;
    @InjectMocks private S3EmailTemplateQueryService templateService;

    private EmailTemplateCreateCommandBuilder templateBuilder;
    private EmailAttachmentCreateCommandBuilder attachmentBuilder;

    @BeforeEach
    void setUp() {
        templateBuilder = EmailTemplateCreateCommandBuilder.builder();
        attachmentBuilder = EmailAttachmentCreateCommandBuilder.builder();
    }

    @Test
    @DisplayName("title을 입력하면 제목이 반환된다.")
    void shouldReturnTitle() {
        EmailTemplateDetailQuery command = templateBuilder.title("hello world!").build();
        doReturn("image").when(properties).imageKeySuffix();
        doReturn("attachment").when(properties).attachmentKeySuffix();
        doReturn("/template/email").when(properties).templateKeyPrefix();

        EmailTemplateResult result = templateService.findTemplate(command);

        assertThat(result.emailTemplate().msgTitle()).isEqualTo("hello world!");
    }

    @Nested
    @DisplayName("템플릿 섹션 테스트")
    class WhenTemplateSection {
        private EmailTemplateDetailQuery command;

        @BeforeEach
        void setUp() {
            command = templateBuilder
                    .customerId("jang314")
                    .templateKey("my_template")
                    .build();
            doReturn("/template/email").when(properties).templateKeyPrefix();
            doReturn("image").when(properties).imageKeySuffix();
            doReturn("attachment").when(properties).attachmentKeySuffix();
        }

        @Nested
        @DisplayName("header 템플릿")
        class WhenHeaderTemplate {
            private static final String headerKey = "/template/email/jang314/my_template/header.html";;

            @Test
            @DisplayName("header 템플릿을 조회한다.")
            void shouldGetHeaderTemplate() {
                doReturn(null).when(fileRepository).getFileMetadata(anyString());
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

                templateService.findTemplate(command);

                verify(fileRepository, times(4)).getFileMetadata(captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(key -> key.equals(headerKey));
            }

            @Test
            @DisplayName("header 템플릿이 존재하지 않으면 section이 header인 템플릿은 생성되지 않는다.")
            void shouldNotGenerate_whenHeaderTemplateDoesNotExist() {
                doAnswer(invocation -> {
                    String key = invocation.getArgument(0);
                    if(key.equals(headerKey)) {
                        return null;
                    }
                    return mock(AwsS3FileMetadataResponse.class);
                }).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section)
                        .doesNotContain(EmailMessageSection.HEADER.code());
            }
            @Test
            @DisplayName("header 템플릿이 존재하면 header의 file_key는 정해진 규칙에 의해 생성된다.")
            void shouldGenerateHeaderFileKey_AccordingToRule(){
                doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section, EmailTemplateContentResult::fileKey)
                        .contains(
                                tuple(EmailMessageSection.HEADER.code(), headerKey)
                        );
            }
        }

        @Nested
        @DisplayName("body 템플릿")
        class WhenBodyTemplate {
            private static final String bodyKey = "/template/email/jang314/my_template/body.html";

            @Test
            @DisplayName("body 템플릿을 조회한다.")
            void shouldGetBodyTemplate() {
                doReturn(null).when(fileRepository).getFileMetadata(anyString());
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

                templateService.findTemplate(command);

                verify(fileRepository, times(4)).getFileMetadata(captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(key -> key.equals(bodyKey));
            }
            @Test
            @DisplayName("body 템플릿이 존재하지 않으면 section이 body 템플릿은 생성되지 않는다.")
            void shouldNotGenerate_whenBodyTemplateDoesNotExist() {
                doAnswer(invocation -> {
                    String key = invocation.getArgument(0);
                    if(key.equals(bodyKey)) {
                        return null;
                    }
                    return mock(AwsS3FileMetadataResponse.class);
                }).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section)
                        .doesNotContain(EmailMessageSection.BODY.code());
            }

            @Test
            @DisplayName("body 템플릿이 존재하면 body의 file_key는 정해진 규칙에 의해 생성된다.")
            void shouldGenerateBodyFileKey_AccordingToRule(){
                doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section, EmailTemplateContentResult::fileKey)
                        .contains(
                                tuple(EmailMessageSection.BODY.code(), bodyKey)
                        );
            }
        }

        @Nested
        @DisplayName("footer 템플릿")
        class WhenFooterTemplate {
            private static final String footerKey = "/template/email/jang314/my_template/footer.html";

            @Test
            @DisplayName("footer 템플릿을 조회한다.")
            void shouldGetFooterTemplate() {
                doReturn(null).when(fileRepository).getFileMetadata(anyString());
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

                templateService.findTemplate(command);

                verify(fileRepository, times(4)).getFileMetadata(captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(key -> key.equals(footerKey));
            }
            @Test
            @DisplayName("footer 템플릿이 존재하지 않으면 section이 footer 템플릿은 생성되지 않는다.")
            void shouldNotGenerate_whenFooterTemplateDoesNotExist() {
                doAnswer(invocation -> {
                    String key = invocation.getArgument(0);
                    if(key.equals(footerKey)) {
                        return null;
                    }
                    return mock(AwsS3FileMetadataResponse.class);
                }).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section)
                        .doesNotContain(EmailMessageSection.FOOTER.code());
            }
            @Test
            @DisplayName("footer 템플릿이 존재하면 footer의 file_key는 정해진 규칙에 의해 생성된다.")
            void shouldGenerateFooterFileKey_AccordingToRule(){
                doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section, EmailTemplateContentResult::fileKey)
                        .contains(
                                tuple(EmailMessageSection.FOOTER.code(), footerKey)
                        );
            }
        }

        @Nested
        @DisplayName("cover 템플릿")
        class WhenCoverTemplate {
            private static final String coverKey = "/template/email/jang314/my_template/cover.html";

            @Test
            @DisplayName("cover 템플릿을 조회한다.")
            void shouldGetCoverTemplate() {
                doReturn(null).when(fileRepository).getFileMetadata(anyString());
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

                templateService.findTemplate(command);

                verify(fileRepository, times(4)).getFileMetadata(captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(key -> key.equals(coverKey));
            }
            @Test
            @DisplayName("cover 템플릿이 존재하지 않으면 section이 cover 템플릿은 생성되지 않는다.")
            void shouldNotGenerate_whenCoverTemplateDoesNotExist() {
                doAnswer(invocation -> {
                    String key = invocation.getArgument(0);
                    if(key.equals(coverKey)) {
                        return null;
                    }
                    return mock(AwsS3FileMetadataResponse.class);
                }).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section)
                        .doesNotContain(EmailMessageSection.COVER.code());
            }

            @Test
            @DisplayName("cover 템플릿이 존재하면 cover의 file_key는 정해진 규칙에 의해 생성된다.")
            void shouldGenerateCoverFileKey_AccordingToRule(){
                doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

                EmailTemplateResult result = templateService.findTemplate(command);
                List<EmailTemplateContentResult> expect = result.emailTemplate().contents();

                assertThat(expect)
                        .extracting(EmailTemplateContentResult::section, EmailTemplateContentResult::fileKey)
                        .contains(
                                tuple(EmailMessageSection.COVER.code(), coverKey)
                        );
            }
        }
    }

    @Nested
    @DisplayName("첨부파일 템플릿")
    class WhenAttachmentTemplate {
        private static final String attachmentKey = "/template/email/jang314/my_template/attachment/attachment.pdf";
        private static final String fileKeyTemplate = "/template/email/jang314/my_template/attachment/${target_name}.pdf";

        @BeforeEach
        void setUp() {
            EmailAttachmentDetailQuery fileKeyAttachment = createAttachmentWithFileKey();
            EmailAttachmentDetailQuery fileKeyTemplateAttachment = createAttahmentWithFileKeyTemplate();
            templateBuilder = templateBuilder
                    .customerId("jang314")
                    .templateKey("my_template")
                    .attachmentList(List.of(fileKeyAttachment, fileKeyTemplateAttachment));

            doReturn("/template/email").when(properties).templateKeyPrefix();
            doReturn("image").when(properties).imageKeySuffix();
            doReturn("attachment").when(properties).attachmentKeySuffix();
        }

        private EmailAttachmentDetailQuery createAttahmentWithFileKeyTemplate() {
            return attachmentBuilder
                    .fileKey(null)
                    .fileKeyTemplate("${target_name}.pdf")
                    .build();
        }

        private EmailAttachmentDetailQuery createAttachmentWithFileKey() {
            return attachmentBuilder
                    .fileKey("attachment.pdf")
                    .fileKeyTemplate(null)
                    .build();
        }

        @Test
        @DisplayName("attachment 개수 만큼 section이 attachment인 템플릿이 생성된다.")
        void shouldCreateAttachmentList() {
            EmailTemplateDetailQuery command = templateBuilder.build();
            doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

            EmailTemplateResult result = templateService.findTemplate(command);

            assertThat(result.emailTemplate().contents())
                    .filteredOn(content -> content.section().equals(EmailMessageSection.ATTACHMENT.code()))
                    .hasSize(2);
        }

        @Test
        @DisplayName("attachment의 file_key가 존재하면, 파일 정보를 조회한다.")
        void shouldGetFileMetadata_whenAttachmentFileKeyDoesExist() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            EmailTemplateDetailQuery command = templateBuilder.build();
            doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

            templateService.findTemplate(command);

            verify(fileRepository, times(5)).getFileMetadata(captor.capture());
            assertThat(captor.getAllValues())
                    .anyMatch(k -> k.equals(attachmentKey));
        }

        @Test
        @DisplayName("attachment의 file_key가 존재하면 file_key는 정해진 규칙에 의해 생성된다.")
        void shouldGenerateAttachmentFileKey_whenAttachmentFileKeyDoesExist() {
            EmailTemplateDetailQuery command = templateBuilder.build();
            doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

            EmailTemplateResult result = templateService.findTemplate(command);

            assertThat(result.emailTemplate().contents())
                    .filteredOn(content -> content.section().equals(EmailMessageSection.ATTACHMENT.code()))
                    .anyMatch(content -> content.fileKey().equals(attachmentKey));
        }

        @Test
        @DisplayName("attachment의 file_key가 존재하지 않으면 파일 정보를 조회하지 않는다.")
        void shouldNotGenerateAttachmentFileKey_whenAttachmentFileKeyDoesNotExist() {
            EmailTemplateDetailQuery command = templateBuilder.build();
            doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

            templateService.findTemplate(command);

            verify(fileRepository, times(5)).getFileMetadata(anyString());
        }

        @Test
        @DisplayName("attachment의 file_key_template이 존재하면 file_key_template를 반환한다.")
        void shouldGenerateFileKeyTemplate_whenAttachmentFileKeyTemplateDoesExist() {
            EmailTemplateDetailQuery command = templateBuilder.build();
            doReturn(mock(AwsS3FileMetadataResponse.class)).when(fileRepository).getFileMetadata(anyString());

            EmailTemplateResult result = templateService.findTemplate(command);

            assertThat(result.emailTemplate().contents())
                    .filteredOn(content -> content.section().equals(EmailMessageSection.ATTACHMENT.code()))
                    .anyMatch(content -> fileKeyTemplate.equals(content.fileKeyTemplate()));
        }

        @Test
        @DisplayName("첨부 파일이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenAttachmentDoesNotExist() {
            EmailTemplateDetailQuery command = templateBuilder.build();
            doReturn(null).when(fileRepository).getFileMetadata(anyString());

            EmailAttachmentFileNotFoundException expect =
                    EmailAttachmentFileNotFoundException.of(attachmentKey);

            assertThatThrownBy(() -> templateService.findTemplate(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("설정 파일 테스트")
    class WhenTemplateProperties {
        private EmailTemplateDetailQuery command;
        @BeforeEach
        void setUp() {
            command = templateBuilder
                    .customerId("hyejin_company")
                    .templateKey("my_template")
                    .attachmentList(List.of())
                    .build();
        }
        @Nested
        @DisplayName("템플릿 기본 경로")
        class WhenTemplateFileKeyPrefix {

            @Test
            @DisplayName("템플릿 기본 경로는 설정 파일에서 조회한다.")
            void shouldGetConfiguredTemplateFileKeyPrefix() {
                doReturn("/template/email").when(properties).templateKeyPrefix();
                doReturn("image").when(properties).imageKeySuffix();
                doReturn("attachment").when(properties).attachmentKeySuffix();

                templateService.findTemplate(command);

                verify(properties).templateKeyPrefix();
            }

            @Test
            @DisplayName("템플릿 기본 경로가 존재하지 않을 경우, 예외가 발생한다.")
            void shouldThrowException_whenTemplateKeyPrefixIsEmpty() {
                doReturn("").when(properties).templateKeyPrefix();

                TemplateNotConfiguredException expect = TemplateNotConfiguredException.of("my_template", EmailUploadPrefixType.TEMPLATE_PREFIX);

                assertThatThrownBy(() -> templateService.findTemplate(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }
        }

        @Nested
        @DisplayName("첨부파일 기본 경로")
        class WhenAttachmentKeySuffix {
            @BeforeEach
            void setUp() {
                doReturn("/template/email").when(properties).templateKeyPrefix();
                doReturn("image").when(properties).imageKeySuffix();
            }

            @Test
            @DisplayName("첨부파일 기본 경로는 설정 파일에서 조회한다.")
            void shouldGetConfiguredAttachmentFileKeySuffix() {
                doReturn("/template/email").when(properties).attachmentKeySuffix();

                templateService.findTemplate(command);

                verify(properties).attachmentKeySuffix();
            }

            @Test
            @DisplayName("첨부파일 기본 경로가 존재하지 않을 경우, 예외가 발생한다.")
            void shouldThrowException_whenAttachmentKeySuffixIsEmpty() {
                doReturn("").when(properties).attachmentKeySuffix();

                TemplateNotConfiguredException expect = TemplateNotConfiguredException.of("my_template", EmailUploadPrefixType.ATTACHMENT_SUFFIX);

                assertThatThrownBy(() -> templateService.findTemplate(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }
        }

        @Nested
        @DisplayName("이미지 파일 기본 경로")
        class WhenImageFileKeySuffix {
            @BeforeEach
            void setUp() {
                doReturn("/template/email").when(properties).templateKeyPrefix();
            }

            @Test
            @DisplayName("템플릿 이미지 기본 경로는 설정 파일에서 조회한다.")
            void shouldGetConfiguredImageKeySuffix() {
                doReturn("image").when(properties).imageKeySuffix();
                doReturn("attachment").when(properties).attachmentKeySuffix();

                templateService.findTemplate(command);

                verify(properties).imageKeySuffix();
            }

            @Test
            @DisplayName("템플릿 이미지 기본 경로가 존재하지 않을 경우 예외가 발생한다.")
            void shouldThrowException_whenImageKeySuffixIsEmpty() {
                doReturn("").when(properties).imageKeySuffix();

                TemplateNotConfiguredException expect = TemplateNotConfiguredException.of("my_template", EmailUploadPrefixType.IMAGE_SUFFIX);

                assertThatThrownBy(() -> templateService.findTemplate(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("템플릿 이미지 기본 경로는 정해진 규칙에 의해 생성된다.")
            void shouldGenerateImageDir_whenAccordingToRule() {
                doReturn("image").when(properties).imageKeySuffix();
                doReturn("attachment").when(properties).attachmentKeySuffix();

                EmailTemplateResult template = templateService.findTemplate(command);

                assertThat(template.emailTemplate().imageDir()).isEqualTo("/template/email/hyejin_company/my_template/image");
            }
        }
    }

}