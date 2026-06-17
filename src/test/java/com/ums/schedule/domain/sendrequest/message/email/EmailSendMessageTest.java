package com.ums.schedule.domain.sendrequest.message.email;

import com.ums.schedule.application.resource.email.command.EmailAttachmentCreateCommandBuilder;
import com.ums.schedule.application.sendrequest.message.email.command.EmailAttachmentCreateCommand;
import com.ums.schedule.application.sendrequest.message.email.command.EmailSendMessageCreateCommand;
import com.ums.schedule.application.sendrequest.template.email.command.EmailTemplateContentCommand;
import com.ums.schedule.application.message.email.command.EmailSendMessageCreateCommandBuilder;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.validation.RequiredException;
import com.ums.schedule.domain.sendrequest.template.email.code.EmailTemplateSectionEnum;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.message.email.exception.EmailMessageFileKeyMissingException;
import com.ums.schedule.domain.sendrequest.message.email.exception.EmailMessageMissingException;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;


public class EmailSendMessageTest {
    @Nested
    @DisplayName("header template 테스트")
    class WhenHeaderTemplate {
        private EmailSendMessageCreateCommandBuilder command;
        private SendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
            this.command = givenHeader();
        }

        private EmailSendMessageCreateCommandBuilder givenHeader() {
            return EmailSendMessageCreateCommandBuilder.builder()
                    .header(UUID.randomUUID().toString(), "<div>header</div");
        }

        @Test
        @DisplayName("header가 존재하지 않으면, header_template_key는 null이다.")
        void shouldReturnNullHeaderTemplateKey_whenHeaderDoesNotExist() {
            EmailSendMessageCreateCommand givenCommand = this.command.header(null, null).build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            assertThat(message.getHeaderTemplateKey()).isNull();
        }

        @Test
        @DisplayName("header가 존재하면 header의 fileKey를 반환한다")
        void shouldReturnHeaderFileKey_whenHeaderExists() {
            EmailSendMessageCreateCommand givenCommand = this.command.build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());
            EmailTemplateContentCommand expect = givenCommand.templateMap().get(EmailTemplateSectionEnum.HEADER);

            assertThat(message.getHeaderTemplateKey()).isEqualTo(expect.fileKey());
        }

        @Test
        @DisplayName("headerTemplateKey가 없으면 headerTemplate은 null이다.")
        void shouldReturnNullHeaderTemplate_whenHeaderTemplateKeyIsNull() {
            EmailSendMessageCreateCommand givenCommand = this.command.header(null, "header template").build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            assertThat(message.getHeaderTemplate()).isNull();
        }

        @Test
        @DisplayName("headerTemplateKey는 존재하는데 template이 없으면 예외가 발생한다")
        void shouldThrowException_whenHeaderTemplateIsMissing() {
            EmailSendMessageCreateCommand givenCommand = this.command.header("header key",null).build();

            EmailMessageMissingException expect = EmailMessageMissingException.headerOf();

            assertThatThrownBy(() -> EmailSendMessage.of(sendMessage, givenCommand, List.of()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("headerTemplateKey와 template이 모두 있으면 headerTemplate을 설정한다.")
        void shouldAssignHeaderTemplate_whenTemplateExists() {
            EmailSendMessageCreateCommand givenCommand = this.command.build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            EmailTemplateContentCommand expect = givenCommand.templateMap().get(EmailTemplateSectionEnum.HEADER);

            assertThat(message.getHeaderTemplate()).isNotNull().isEqualTo(expect.template());
        }
    }

    @Nested
    @DisplayName("body template 테스트")
    class WhenBodyTemplate {
        private EmailSendMessageCreateCommandBuilder command;
        private SendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
            this.command = givenBody();
        }

        private EmailSendMessageCreateCommandBuilder givenBody() {
            return EmailSendMessageCreateCommandBuilder.builder()
                    .body(UUID.randomUUID().toString(), "<div>body</div");
        }

        @Test
        @DisplayName("body가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyDoesNotExist() {
            EmailSendMessageCreateCommand givenCommand = this.command.body(null, null).build();

            EmailMessageMissingException expect = EmailMessageMissingException.bodyOf();

            assertThatThrownBy(() -> EmailSendMessage.of(sendMessage, givenCommand, List.of()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("body의 fileKey가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyFileKeyDoesNotExist() {
            EmailSendMessageCreateCommand givenCommand = this.command.body(null, "<div>body</div>").build();

            EmailMessageFileKeyMissingException expect = EmailMessageFileKeyMissingException.bodyOf();

            assertThatThrownBy(() -> EmailSendMessage.of(sendMessage, givenCommand, List.of()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("body가 존재하면 body의 fileKey를 반환한다")
        void shouldReturnBodyFileKey_whenBodyExists() {
            EmailSendMessageCreateCommand givenCommand = this.command.body("body.html", "<div>body</div>").build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            EmailTemplateContentCommand expect = givenCommand.templateMap().get(EmailTemplateSectionEnum.BODY);
            assertThat(message.getBodyTemplateKey()).isNotNull().isEqualTo(expect.fileKey());
        }

        @Test
        @DisplayName("bodyTemplate이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateDoesNotExist() {
            EmailSendMessageCreateCommand givenCommand = this.command.body("body.html", null).build();

            EmailMessageMissingException expect = EmailMessageMissingException.bodyOf();

            assertThatThrownBy(() -> EmailSendMessage.of(sendMessage, givenCommand, List.of()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("bodyTemplateKey와 template이 모두 있으면 bodyTemplate을 설정한다.")
        void shouldAssignBodyTemplate_whenTemplateExists() {
            EmailSendMessageCreateCommand givenCommand = this.command.body("body.html", "<div>body</div>").build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            EmailTemplateContentCommand expect = givenCommand.templateMap().get(EmailTemplateSectionEnum.BODY);
            assertThat(message.getBodyTemplate()).isNotNull().isEqualTo(expect.template());
        }
    }

    @Nested
    @DisplayName("footer template 테스트")
    class WhenFooterTemplate {
        private EmailSendMessageCreateCommandBuilder command;
        private SendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
            this.command = givenFooter();
        }

        private EmailSendMessageCreateCommandBuilder givenFooter() {
            return EmailSendMessageCreateCommandBuilder.builder()
                    .footer(UUID.randomUUID().toString(), "<div>footer</div");
        }

        @Test
        @DisplayName("footer가 존재하지 않으면, footer_template_key는 null이다.")
        void shouldReturnNullFooterTemplateKey_whenFooterDoesNotExist() {
            EmailSendMessageCreateCommand givenCommand = this.command.footer(null, null).build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            assertThat(message.getFooterTemplateKey()).isNull();
        }

        @Test
        @DisplayName("footer가 존재하면 footer의 fileKey를 반환한다")
        void shouldReturnFooterFileKey_whenFooterExists() {
            EmailSendMessageCreateCommand givenCommand = this.command.footer("footer.html", "footer").build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            EmailTemplateContentCommand expect = givenCommand.templateMap().get(EmailTemplateSectionEnum.FOOTER);
            assertThat(message.getFooterTemplateKey()).isNotNull().isEqualTo(expect.fileKey());
        }

        @Test
        @DisplayName("footerTemplateKey가 없으면 footerTemplate은 null이다.")
        void shouldReturnNullFooterTemplate_whenFooterTemplateKeyIsNull() {
            EmailSendMessageCreateCommand givenCommand = this.command.footer(null, "footer").build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            assertThat(message.getFooterTemplate()).isNull();
        }

        @Test
        @DisplayName("footerTemplateKey는 존재하는데 template이 없으면 예외가 발생한다")
        void shouldThrowException_whenFooterTemplateIsMissing() {
            EmailSendMessageCreateCommand givenCommand = this.command.footer("footer.html", null).build();

            EmailMessageMissingException expect = EmailMessageMissingException.footerOf();

            assertThatThrownBy(() -> EmailSendMessage.of(sendMessage, givenCommand, List.of()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("footerTemplateKey와 template이 모두 있으면 내용을 설정한다.")
        void shouldAssignFooterTemplate_whenTemplateExists() {
            EmailSendMessageCreateCommand givenCommand = this.command.build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, givenCommand, List.of());

            EmailTemplateContentCommand expect = givenCommand.templateMap().get(EmailTemplateSectionEnum.FOOTER);
            assertThat(message.getFooterTemplate()).isNotNull().isEqualTo(expect.template());
        }
    }

    @Nested
    @DisplayName("attachment list 테스트")
    class WhenAttachmentListTest {
        private SendMessage sendMessage;
        private EmailSendMessageCreateCommand command;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
            this.command = EmailSendMessageCreateCommandBuilder.builder().build();
        }

        @Test
        @DisplayName("attachment command 개수만큼 attachment가 생성된다")
        void shouldCreateAttachmentsFromCommands() {
            EmailAttachmentCreateCommand givenCommandA = EmailAttachmentCreateCommandBuilder.builder().build();
            EmailAttachmentCreateCommand givenCommandB = EmailAttachmentCreateCommandBuilder.builder().build();
            EmailAttachmentCreateCommand givenCommandC = EmailAttachmentCreateCommandBuilder.builder().build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, command, List.of(givenCommandA, givenCommandB, givenCommandC));

            assertThat(message.getAttachmentList()).hasSize(3);
        }


        @Test
        @DisplayName("생성된 attachment는 EmailSendMessage를 참조한다")
        void shouldSetParentReferenceToAttachments() {
            EmailAttachmentCreateCommand givenCommand = EmailAttachmentCreateCommandBuilder.builder().build();

            EmailSendMessage message = EmailSendMessage.of(sendMessage, command, List.of(givenCommand));

            assertThat(message.getAttachmentList())
                    .allSatisfy(attachment -> assertThat(attachment.getSendMessage()).isSameAs(message));
        }
    }

    @Nested
    @DisplayName("subject 테스트")
    class WhenSubject {
        @Test
        @DisplayName("message_type이 ADVERTISE이면 제목 앞에 (광고) 문구가 붙는다.")
        void shouldPrependAdvertisePrefix_whenMessageTypeIsAdvertise() {
            SendRequest sendRequest = mock(SendRequest.class);
            SendMessage givenMessage = SendMessage.of(sendRequest, EnumMapperValue.fromEnumMapperType(TemplateTypeEnum.ADVERTISE), "(광고)");
            EmailSendMessageCreateCommand command = EmailSendMessageCreateCommandBuilder.builder()
                    .title("title")
                    .build();

            EmailSendMessage message = EmailSendMessage.of(givenMessage, command, List.of());

            assertThat(message.getSubject()).isEqualTo("(광고) " + command.title());
        }

        @Test
        @DisplayName("subject가 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenTitleIsNull() {
            SendRequest sendRequest = mock(SendRequest.class);
            SendMessage givenMessage = SendMessage.of(sendRequest, EnumMapperValue.fromEnumMapperType(TemplateTypeEnum.ADVERTISE), "(광고)");
            EmailSendMessageCreateCommand command = EmailSendMessageCreateCommandBuilder.builder()
                    .title(null)
                    .build();


            RequiredException expect = RequiredException.fieldOf("title");

            assertThatThrownBy(() -> EmailSendMessage.of(givenMessage, command, List.of()))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    private SendMessage givenSendMessage() {
        SendRequest sendRequest = mock(SendRequest.class);
        return SendMessage.of(sendRequest, EnumMapperValue.fromEnumMapperType(TemplateTypeEnum.NONE), "(광고)");
    }
}
