package com.ums.schedule.application.ums.email.attachment;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.message.email.EmailAttachmentJpaRepository;
import com.ums.schedule.domain.message.email.code.ConvertTypeEnum;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.fixture.email.attachment.SecurityMailBuilder;
import com.ums.schedule.fixture.email.convert.ConvertedAttachmentBuilder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailAttachmentCreateServiceTest {
    @Mock private EmailAttachmentJpaRepository repository;
    @InjectMocks private EmailAttachmentCreateService attachmentService;

    private EmailSendMessage message;
    private SecurityMail securityMail;
    private ConvertedAttachment attachment;

    @BeforeEach
    void setUp() {
        message = mock(EmailSendMessage.class);
        securityMail = SecurityMailBuilder.builder().build();
        attachment = ConvertedAttachmentBuilder.builder().build();
    }

    @Test
    @DisplayName("첨부파일 목록은 입력된 개수만큼 저장된다.")
    void shouldCreateAttachmentsForEachInputAttachment() {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        doReturn(mock(List.class)).when(repository).saveAll(any());

        List<EmailAttachment> attachmentList =
                attachmentService.create(message, securityMail,
                        List.of(attachment, attachment, attachment));

        verify(repository).saveAll(captor.capture());
        List<EmailAttachment> captorValue = captor.getValue();
        assertThat(captorValue).hasSize(3);
    }

    @Nested
    @DisplayName("변환 타입이 NONE이 아닌 첨부 파일이 존재할 경우")
    class WhenConvertTypeIsNotNone {
        private ConvertedAttachment attachment;

        @BeforeEach
        void setUp() {
            attachment = ConvertedAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.HTML)
                    .fileKey("attachment.html")
                    .fileKeyTemplate("attachment.html")
                    .build();
        }

        @Test
        @DisplayName("보안 메일이 존재하면, 보안 정보가 저장된다.")
        void shouldSaveSecurityMailPolicy_whenSecurityMailPolicyExists() {
            List<EmailAttachment> attachmentList = attachmentService.create(message, securityMail, List.of(attachment));

            assertThat(attachmentList)
                    .filteredOn(attachment -> filterOnConvertTypeIsNotNone(attachment))
                    .extracting(EmailAttachment::getSecurityPolicy)
                    .isNotNull();
        }

        @Test
        @DisplayName("보안 메일이 존재하지 않으면, 보안 정보는 저장되지 않는다.")
        void shouldNotSaveSecurityMailPolicy_whenSecurityMailPolicyDoesNotExist() {
            List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

            assertThat(attachmentList)
                    .filteredOn(attachment -> filterOnConvertTypeIsNotNone(attachment))
                    .singleElement()
                    .extracting(EmailAttachment::getSecurityPolicy)
                    .isNull();
        }

        @Test
        @DisplayName("file_key가 반환된다.")
        void shouldReturnFileKey() {
            List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

            assertThat(attachmentList)
                    .filteredOn(attachment -> filterOnConvertTypeIsNotNone(attachment))
                    .singleElement()
                    .extracting(EmailAttachment::getFileKey)
                    .isEqualTo("attachment.html");
        }

        @Test
        @DisplayName("file_key_template이 반환된다.")
        void shouldReturnFileKeyTemplate() {
            List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

            assertThat(attachmentList)
                    .filteredOn(attachment -> filterOnConvertTypeIsNotNone(attachment))
                    .singleElement()
                    .extracting(EmailAttachment::getFileKeyTemplate)
                    .isEqualTo("attachment.html");
        }

        @Test
        @DisplayName("변환 타입은 NONE이 아니다.")
        void shouldReturnNotNone() {
            List<EmailAttachment> attachmentList =
                    attachmentService.create(message, null, List.of(attachment));

            assertThat(attachmentList)
                    .extracting(EmailAttachment::getConvertType)
                    .contains(ConvertTypeEnum.HTML);
        }
    }

    @Nested
    @DisplayName("변환 타입이 NONE인 첨부파일이 존재할 경우")
    class WhenConvertTypeIsNone {
        private ConvertedAttachment attachment;

        @BeforeEach
        void setUp() {
            attachment = ConvertedAttachmentBuilder.builder()
                    .convertType(ConvertTypeEnum.NONE)
                    .fileKey("attachment.html")
                    .fileKeyTemplate("attachment.html")
                    .build();
        }

        @Test
        @DisplayName("변환 타입은 NONE이 반환된다.")
        void shouldReturnNone() {
            List<EmailAttachment> attachmentList = attachmentService.create(message, securityMail, List.of(attachment));

            assertThat(attachmentList)
                    .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                    .hasSize(1);
        }

        @Test
        @DisplayName("보안 정책은 반환되지 않는다.")
        void shouldNotReturnSecurityMailPolicy() {
            List<EmailAttachment> attachmentList =
                    attachmentService.create(message, securityMail, List.of(attachment));

            assertThat(attachmentList)
                    .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                    .singleElement()
                    .extracting(EmailAttachment::getSecurityPolicy)
                    .isNull();
        }


        @Nested
        @DisplayName("파일 키가 존재하는 경우")
        class WhenFileKey {
            @BeforeEach
            void setUp() {
                attachment = ConvertedAttachmentBuilder.builder()
                        .convertType(ConvertTypeEnum.NONE)
                        .fileKey("attachment.html")
                        .build();
            }

            @Test
            @DisplayName("file_key가 반환된다.")
            void shouldReturnFileKey() {
                List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

                assertThat(attachmentList)
                        .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                        .singleElement()
                        .extracting(EmailAttachment::getFileKey)
                        .isEqualTo("attachment.html");
            }

            @Test
            @DisplayName("file_key_template은 반환되지 않는다.")
            void shouldNotFileKeyTemplate() {
                List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

                assertThat(attachmentList)
                        .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                        .singleElement()
                        .extracting(EmailAttachment::getFileKeyTemplate)
                        .isNull();
            }
        }
        @Nested
        @DisplayName("파일 키 템플릿이 존재하는 경우")
        class WhenFileKeyTemplate {
            @BeforeEach
            void setUp() {
                attachment = ConvertedAttachmentBuilder.builder()
                        .convertType(ConvertTypeEnum.NONE)
                        .fileKey(null)
                        .fileKeyTemplate("attachment.html")
                        .build();
            }

            @Test
            @DisplayName("file_key_template이 반환된다.")
            void shouldReturnFileKeyTemplate() {
                List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

                assertThat(attachmentList)
                        .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                        .singleElement()
                        .extracting(EmailAttachment::getFileKeyTemplate)
                        .isEqualTo("attachment.html");
            }

            @Test
            @DisplayName("file_key는 반환되지 않는다.")
            void shouldNotReturnFileKey() {
                List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

                assertThat(attachmentList)
                        .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                        .singleElement()
                        .extracting(EmailAttachment::getFileKey)
                        .isNull();
            }

            @Test
            @DisplayName("파일 크기는 반환되지 않는다.")
            void shouldNotReturnFileSize() {
                List<EmailAttachment> attachmentList = attachmentService.create(message, null, List.of(attachment));

                assertThat(attachmentList)
                        .filteredOn(attachment -> filterOnConvertTypeIsNone(attachment))
                        .singleElement()
                        .extracting(EmailAttachment::getFileSize)
                        .isNull();
            }
        }
    }

    private boolean filterOnConvertTypeIsNone(EmailAttachment attachment) {
        return attachment.getConvertType() == ConvertTypeEnum.NONE;
    }

    private boolean filterOnConvertTypeIsNotNone(EmailAttachment attachment) {
        return attachment.getConvertType() != ConvertTypeEnum.NONE;
    }
}