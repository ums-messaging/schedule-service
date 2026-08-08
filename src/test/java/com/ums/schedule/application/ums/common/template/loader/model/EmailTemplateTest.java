package com.ums.schedule.application.ums.common.template.loader.model;

import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailMessageSection;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import com.ums.schedule.domain.message.email.security.SecurityMailPolicy;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.fixture.email.convert.ConvertMailBuilder;
import com.ums.schedule.fixture.email.convert.SecurityMailPolicyBuilder;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class EmailTemplateTest {
    private Map<EmailMessageSection, Template> templateMap ;
    private EmailSendMessageBuilder builder;

    @BeforeEach
    void setUp() {
        templateMap = Map.of(
                EmailMessageSection.BODY, mock(Template.class)
        );
        builder = EmailSendMessageBuilder.builder();
    }

    @Nested
    @DisplayName("제목 테스트")
    class WhenSubject {

        @Test
        @DisplayName("제목이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenSubjectIsNull() {
            EmailSendMessage message = builder.subject(null).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("title");
        }

        @Test
        @DisplayName("제목이 반환된다.")
        void shouldReturnSubject() {
            EmailSendMessage message = builder.subject("mail subject").build();

            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getTitle()).isEqualTo("mail subject");
        }
    }

    @Nested
    @DisplayName("헤더 템플릿 정보 테스트")
    class WhenHeaderTemplateContent {
        private Map<EmailMessageSection, Template> headerTemplateMap;
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            headerTemplateMap = Map.of(
                    EmailMessageSection.HEADER, mock(Template.class),
                    EmailMessageSection.BODY, mock(Template.class)
            );
            message = builder.headerTemplateKey("header.html")
                    .build();
        }

        @Test
        @DisplayName("헤더 키가 존재하는데, 템플릿 정보가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenHeaderKeyExists() {
            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("header_template")
            ;
        }

        @Test
        @DisplayName("헤더 정보가 없으면 헤더는 반환되지 않는다.")
        void shouldReturnNull_whenHeaderTemplateDoesNotExist() {
            message = builder.headerTemplateKey(null).build();
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getHeader()).isNull();
        }

        @Test
        @DisplayName("헤더 정보가 존재하면, 헤더는 반환된다.")
        void shouldReturnHeaderTemplateContent() {
            EmailTemplate template = EmailTemplate.of(message, headerTemplateMap);

            assertThat(template.getHeader()).isNotNull();
        }
    }

    @Nested
    @DisplayName("푸터 템플릿 정보 테스트")
    class WhenFooterExists {
        private Map<EmailMessageSection, Template> footerTemplateMap;
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            footerTemplateMap = Map.of(
                    EmailMessageSection.BODY, mock(Template.class),
                    EmailMessageSection.FOOTER, mock(Template.class)
            );
            message = builder.footerTemplateKey("footer.html")
                    .build();
        }

        @Test
        @DisplayName("푸터 키가 존재하는데, 템플릿 정보가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenHeaderKeyExists() {
            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("footer_template");
        }

        @Test
        @DisplayName("푸터 정보가 존재하지 않으면 푸터는 반환되지 않는다.")
        void shouldReturnNull_whenFooterDoesNotExist() {
            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("푸터 정보가 존재하면, 푸터가 반환된다.")
        void shouldReturnFooter_whenFooterExists() {
            EmailTemplate template = EmailTemplate.of(message, footerTemplateMap);
            assertThat(template.getFooter()).isNotNull();
        }
    }

    @Nested
    @DisplayName("바디 템플릿 정보 테스트")
    class WhenBodyExists {
        private Map<EmailMessageSection, Template> bodyTemplateMap;
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            bodyTemplateMap = Map.of(
                    EmailMessageSection.BODY, mock(Template.class)
            );
            message = builder.bodyTemplateKey("body.html")
                    .build();
        }

        @Test
        @DisplayName("바디 템플릿 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateKeyDoesNotExist() {
            message = builder.bodyTemplateKey(null).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("body_file_key");
            ;
        }

        @Test
        @DisplayName("바디 정보가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyDoesNotExist() {
            assertThatThrownBy(() -> EmailTemplate.of(message, Map.of()))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("body_template");
        }

        @Test
        @DisplayName("바디 정보가 존재하면, 바디가 반환된다.")
        void shouldReturnBodyTemplateContent_whenBodyExists() {
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getBody()).isNotNull();
        }
    }

    @Nested
    @DisplayName("변환 정보가 존재하지 않을 때")
    class WhenConvertMailDoesNotExist {
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            this.message = builder.build();
        }

        @Test
        @DisplayName("변환 정보가 존재하지 않을 때 CONVERT_TYPE은 NONE을 반환한다.")
        void shouldReturnNone_whenConvertMailDoesNotExist() {
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getConvertType()).isEqualTo(ConvertType.NONE);
        }

        @Test
        @DisplayName("변환 타입이 NONE이고 커버가 존재하지 않으면 커버는 반환되지 않는다.")
        void shouldNotReturn_whenConvertTypeIsNoneAndCoverDoesNotExist() {
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getCover()).isNull();
        }
    }

    @Nested
    @DisplayName("변환 타입이 HTML 일 때")
    class WhenConvertTypeIsHtml {
        private ConvertMailBuilder convertMailBuilder;
        private Map<EmailMessageSection, Template> templateMap;
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            convertMailBuilder = ConvertMailBuilder.builder()
                    .convertType(ConvertType.HTML)
                    .attachmentName("첨부파일명")
                    .downloadName("다운로드명");

            templateMap = Map.of(
                    EmailMessageSection.BODY, mock(Template.class),
                    EmailMessageSection.COVER, mock(Template.class)
            );
            builder = builder.bodyTemplateKey("body.html")
                    .coverTemplateKey("cover.html")
                    .convertMail(convertMailBuilder.build());

            message = builder.build();
        }

        @Test
        @DisplayName("커버 템플릿 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenCoverTemplateKeyDoesNotExist() {
            EmailSendMessage message = builder.coverTemplateKey(null).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("cover_file_key")
            ;
        }

        @Test
        @DisplayName("커버가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenAndCoverDoesNotExist() {
            Map<EmailMessageSection, Template> templateMap =
                    Map.of(EmailMessageSection.BODY, mock(Template.class));

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("cover_template")
            ;
        }

        @Test
        @DisplayName("커버를 반환한다.")
        void shouldReturnCoverTemplate() {
            EmailTemplate template = EmailTemplate.of(message, templateMap);
            assertThat(template.getCover()).isNotNull();
        }

        @Test
        @DisplayName("바디 템플릿의 첨부파일 명이 존재하지 않으면 예외가 발생한다. ")
        void shouldThrowException_whenBodyAttachmentNameIsEmpty() {
            ConvertMail convertMail = convertMailBuilder.attachmentName(null).build();
            EmailSendMessage message = builder.convertMail(convertMail).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContainingAll("body_template", "attachment_name")
            ;
        }

        @Test
        @DisplayName("바디 템플릿의 다운로드 명이 존재하지 않으면 예외가 발생한다. ")
        void shouldThrowException_whenBodyDownloadNameIsEmpty() {
            ConvertMail convertMail = convertMailBuilder.downloadName(null).build();
            EmailSendMessage message = builder.convertMail(convertMail).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContainingAll("body_template", "download_name")
            ;
        }
    }

    @Nested
    @DisplayName("변환 타입이 PDF 일 때")
    class WhenConvertTypeIsPdf {
        private ConvertMailBuilder convertMailBuilder;
        private Map<EmailMessageSection, Template> templateMap;
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            convertMailBuilder = ConvertMailBuilder.builder()
                    .convertType(ConvertType.PDF)
                    .attachmentName("첨부파일명")
                    .downloadName("다운로드명");

            templateMap = Map.of(
                    EmailMessageSection.BODY, mock(Template.class),
                    EmailMessageSection.COVER, mock(Template.class)
            );
            builder = builder.bodyTemplateKey("body.html")
                    .coverTemplateKey("cover.html")
                    .convertMail(convertMailBuilder.build());

            message = builder.build();
        }

        @Test
        @DisplayName("커버 템플릿 키가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenCoverTemplateKeyDoesNotExist() {
            EmailSendMessage message = builder.coverTemplateKey(null).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("cover_file_key")
            ;
        }

        @Test
        @DisplayName("커버가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenAndCoverDoesNotExist() {
            Map<EmailMessageSection, Template> templateMap =
                    Map.of(EmailMessageSection.BODY, mock(Template.class));

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("cover_template");
        }
        @Test
        @DisplayName("커버를 반환한다.")
        void shouldReturnCoverTemplate() {
            EmailTemplate template = EmailTemplate.of(message, templateMap);
            assertThat(template.getCover()).isNotNull();
        }
        @Test
        @DisplayName("바디 템플릿의 첨부파일 명이 존재하지 않으면 예외가 발생한다. ")
        void shouldThrowException_whenBodyAttachmentNameIsEmpty() {
            ConvertMail convertMail = convertMailBuilder.attachmentName(null).build();
            EmailSendMessage message = builder.convertMail(convertMail).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContainingAll("body_template", "attachment_name");
        }

        @Test
        @DisplayName("바디 템플릿의 다운로드 명이 존재하지 않으면 예외가 발생한다. ")
        void shouldThrowException_whenBodyDownloadNameIsEmpty() {
            ConvertMail convertMail = convertMailBuilder.downloadName(null).build();
            EmailSendMessage message = builder.convertMail(convertMail).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("body_template", "download_name");
        }
    }

    @Nested
    @DisplayName("이메일 타입이 SECURITY일 때")
    class WhenEmailTypeIsSecurity {
        private Map<EmailMessageSection, Template> templateMap;
        private EmailSendMessage message;

        @BeforeEach
        void setUp() {
            templateMap = Map.of(
                    EmailMessageSection.BODY, mock(Template.class),
                    EmailMessageSection.COVER, mock(Template.class)
            );
            SecurityMailPolicy securityMail = SecurityMailPolicyBuilder.builder().build();
            ConvertMail convertMail = ConvertMailBuilder.builder().convertType(ConvertType.HTML).build();
            builder = builder.emailType(EmailType.SECURITY)
                    .convertMail(convertMail)
                    .securityMailPolicy(securityMail);
            message = builder.coverTemplateKey("cover.html").build();
        }

        @Test
        @DisplayName("보안 정보가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenSecurityMailPolicyDoesNotExist() {
            this.message = builder.securityMailPolicy(null).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("security_mail_policy");
        }

        @Test
        @DisplayName("변환 타입이 NONE이면 예외가 발생한다.")
        void shouldThrowException_whenConvertTypeIsNone(){
            ConvertMail convertMail = ConvertMailBuilder.builder().convertType(ConvertType.NONE).build();
            message = builder.convertMail(convertMail).build();

            assertThatThrownBy(() -> EmailTemplate.of(message, templateMap))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("convert_type")
            ;
        }
    }

    @Nested
    @DisplayName("첨부파일 목록이 존재할 때")
    class WhenAttachmentListIsNotEmpty {
        private EmailSendMessage message;
        private List<EmailAttachment> attachments;
        private EmailAttachment withTemplate;
        private EmailAttachment withDirect;

        @BeforeEach
        void setUp() {
            withTemplate = EmailAttachmentBuilder.builder()
                    .attachmentType(AttachmentType.TEMPLATE)
                    .fileKey("${target_id}.pdf")
                    .attachmentName("첨부파일")
                    .downloadName("다운로드")
                    .build();

            withDirect = EmailAttachmentBuilder.builder()
                    .attachmentType(AttachmentType.DIRECT)
                    .fileKey("attachment.html")
                    .attachmentName("첨부파일")
                    .downloadName("다운로드")
                    .build();

            attachments = List.of(withTemplate, withDirect);
            builder = builder.attachments(attachments);
            message = builder.build();
        }

        @Test
        @DisplayName("첨부파일 타입이 TEMPLATE이면, 파일 키 템플릿이 반환된다.")
        void shouldReturnFileKeyTemplate_whenAttachmentTypeIsTemplate() {
            message = builder.attachments(List.of(withTemplate)).build();
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getAttachments())
                    .extracting(v -> v.fileKeyTemplate())
                    .containsExactly("${target_id}.pdf");

        }

        @Test
        @DisplayName("첨부파일 타입이 DIRECT이면, 파일 키가 반환된다.")
        void shouldReturnFileKey_whenAttachmentTypeIsDirect() {
            message = builder.attachments(List.of(withDirect)).build();
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getAttachments())
                    .extracting(v -> v.fileKey())
                    .containsExactly("attachment.html");
        }

        @Test
        @DisplayName("첨부파일 개수만큼 첨부파일이 생성된다.")
        void shouldCreateAttachmentList() {
            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getAttachments()).hasSize(2);
        }

        @Test
        @DisplayName("첨부파일 목록이 존재하지 않으면 빈 목록을 반환한다.")
        void shouldEmptyList_whenAttachmentListIsEmpty() {
            message = builder.attachments(List.of()).build();

            EmailTemplate template = EmailTemplate.of(message, templateMap);

            assertThat(template.getAttachments()).hasSize(0);
        }
    }
}