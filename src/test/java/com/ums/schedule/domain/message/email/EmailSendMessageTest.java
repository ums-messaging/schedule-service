package com.ums.schedule.domain.message.email;

import com.ums.schedule.application.ums.email.message.model.EmailMessageCreateContext;
import com.ums.schedule.fixture.email.message.EmailMessageContextBuilder;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.message.SendMessage;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.template.code.TemplateTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;


public class EmailSendMessageTest {
    @Nested
    @DisplayName("header template 테스트")
    class WhenHeaderTemplate {
        private EmailMessageContextBuilder command;
        private SendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
        }


        @Test
        @DisplayName("header가 존재하지 않으면, header_template_key는 null이다.")
        void shouldReturnNullHeaderTemplateKey_whenHeaderDoesNotExist() {


//            EmailSendMessage message = EmailSendMessage.of(mock(SendMessage.class), givenCommand);

//            assertThat(message.getHeaderTemplateKey()).isNull();
        }

        @Test
        @DisplayName("header가 존재하면 header의 fileKey를 반환한다")
        void shouldReturnHeaderFileKey_whenHeaderExists() {

//            EmailSendMessage message = EmailSendMessage.of(givenCommand);

//            assertThat(message.getHeaderTemplateKey()).isEqualTo("header.html");
        }

        @Test
        @DisplayName("headerTemplateKey가 없으면 headerTemplate은 null이다.")
        void shouldReturnNullHeaderTemplate_whenHeaderTemplateKeyIsNull() {

        }

        @Test
        @DisplayName("headerTemplateKey는 존재하는데 template이 없으면 예외가 발생한다")
        void shouldThrowException_whenHeaderTemplateIsMissing() {

        }

        @Test
        @DisplayName("headerTemplateKey와 template이 모두 있으면 headerTemplate을 설정한다.")
        void shouldAssignHeaderTemplate_whenTemplateExists() {

        }
    }

    @Nested
    @DisplayName("body template 테스트")
    class WhenBodyTemplate {
        private EmailMessageContextBuilder command;
        private SendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
        }


        @Test
        @DisplayName("body가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyDoesNotExist() {

        }

        @Test
        @DisplayName("body의 fileKey가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyFileKeyDoesNotExist() {

        }

        @Test
        @DisplayName("body가 존재하면 body의 fileKey를 반환한다")
        void shouldReturnBodyFileKey_whenBodyExists() {

        }

        @Test
        @DisplayName("bodyTemplate이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenBodyTemplateDoesNotExist() {

        }

        @Test
        @DisplayName("bodyTemplateKey와 template이 모두 있으면 bodyTemplate을 설정한다.")
        void shouldAssignBodyTemplate_whenTemplateExists() {

        }
    }

    @Nested
    @DisplayName("footer template 테스트")
    class WhenFooterTemplate {
        private EmailMessageContextBuilder command;
        private SendMessage sendMessage;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
        }



        @Test
        @DisplayName("footer가 존재하지 않으면, footer_template_key는 null이다.")
        void shouldReturnNullFooterTemplateKey_whenFooterDoesNotExist() {

        }

        @Test
        @DisplayName("footer가 존재하면 footer의 fileKey를 반환한다")
        void shouldReturnFooterFileKey_whenFooterExists() {

        }

        @Test
        @DisplayName("footerTemplateKey가 없으면 footerTemplate은 null이다.")
        void shouldReturnNullFooterTemplate_whenFooterTemplateKeyIsNull() {
        }

        @Test
        @DisplayName("footerTemplateKey는 존재하는데 template이 없으면 예외가 발생한다")
        void shouldThrowException_whenFooterTemplateIsMissing() {
        }

        @Test
        @DisplayName("footerTemplateKey와 template이 모두 있으면 내용을 설정한다.")
        void shouldAssignFooterTemplate_whenTemplateExists() {
        }
    }

    @Nested
    @DisplayName("attachment list 테스트")
    class WhenAttachmentListTest {
        private SendMessage sendMessage;
        private EmailMessageCreateContext command;

        @BeforeEach
        void setUp() {
            this.sendMessage = givenSendMessage();
        }

        @Test
        @DisplayName("attachment command 개수만큼 attachment가 생성된다")
        void shouldCreateAttachmentsFromCommands() {
        }

        @Test
        @DisplayName("메시지 생성 결과 제목이 존재하지 않으면, 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenTitleDoesNotExist() {
        }

        @Test
        @DisplayName("메시지 생성 결과 본문 키가 존재하지 않으면, 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenBodyFileKeyDoesNotExist() {
        }

        @Test
        @DisplayName("헤더 키가 존재하면, 저장된다.")
        void shouldSaveHeaderKey_whenHeaderKeyExists() {
        }

        @Test
        @DisplayName("헤더 키가 존재하지 않으면, 저장되지 않는다.")
        void shouldSaveHeaderKey_whenHeaderKeyDoesNotExist() {

        }

        @Test
        @DisplayName("본문 키가 존재하면, 저장된다.")
        void shouldSaveBodyKey_whenBodyKeyExists() {

        }

        @Test
        @DisplayName("본문 키가 존재하지 않으면, 이메일 메시지는 저장되지 않는다.")
        void shouldNotSaveEmailSendMessage_whenBodyKeyDoesNotExist() {

        }

        @Test
        @DisplayName("푸터 키가 존재하면, 저장된다.")
        void shouldSaveFooterKey_whenFooterKeyExists() {

        }

        @Test
        @DisplayName("푸터 키가 존재하지 않으면, 저장되지 않는다.")
        void shouldSaveFooterKey_whenFooterKeyDoesNotExist() {

        }
        @Test
        @DisplayName("이미지 경로가 존재하면, 저장된다.")
        void shouldSaveImageDir_whenImageDirExists() {

        }
        @Test
        @DisplayName("이미지 경로가 존재하지 않으면, 저장된다.")
        void shouldNotSaveImageDir_whenImageDirDoesNotExist() {

        }
        @Test
        @DisplayName("생성된 attachment는 EmailSendMessage를 참조한다")
        void shouldSetParentReferenceToAttachments() {
        }
    }

    @Nested
    @DisplayName("subject 테스트")
    class WhenSubject {
        @Test
        @DisplayName("message_type이 ADVERTISE이면 제목 앞에 (광고) 문구가 붙는다.")
        void shouldPrependAdvertisePrefix_whenMessageTypeIsAdvertise() {
        }

        @Test
        @DisplayName("subject가 NULL이면 익셉션이 발생한다.")
        void shouldThrowException_whenTitleIsNull() {
        }
    }

    private SendMessage givenSendMessage() {
        SendRequest sendRequest = mock(SendRequest.class);
        return SendMessage.of(sendRequest, EnumMapperValue.fromEnumMapperType(TemplateTypeEnum.NONE), "(광고)");
    }
}
