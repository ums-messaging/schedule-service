package com.ums.schedule.domain.target.message;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.email.generator.model.EmailTargetMessageCreateContext;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplateContent;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.fixture.email.EmailTargetMessageCreateContextBuilder;
import com.ums.schedule.fixture.email.RenderedTemplateContentBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class EmailTargetMessageTest {
    private EmailSendMessage sendMessage;
    private EmailTargetMessageCreateContextBuilder contextBuilder;
    private TargetMessageData targetMessageData;

    @BeforeEach
    void setUp() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        this.sendMessage = mock(EmailSendMessage.class);
        this.contextBuilder = EmailTargetMessageCreateContextBuilder.builder()
                .subject("${month}월 청구서 입니다.")
                .header(new Template("header.html", "${header}", configuration))
                .header(new Template("header.html", "${header}", configuration))
                .bodyTemplate("body template")
                .sendMessage(sendMessage)
                .attachmentList(List.of());

        this.targetMessageData = new TargetMessageData(
                createTargetData(),
                createDataParam()
        );
    }

    private Map<String, Object> createDataParam() {
        return Map.of(
                "month", 8,
                "header", "header template",
                "footer", "footer template"
        );
    }

    private Map<SendTargetColumn, String> createTargetData() {
        return Map.of(
                SendTargetColumn.TARGET_KEY, UUID.randomUUID().toString(),
                SendTargetColumn.TARGET_NAME, "hyejin",
                SendTargetColumn.TARGET_EMAIL, "jang@test.com"
        );
    }

    @Test
    @DisplayName("제목 템플릿이 정상적으로 치환된다.")
    void shouldParseSubjectTemplate() {
        EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetMessageData);

        assertThat(targetMessage.getSubject()).isEqualTo("8월 청구서 입니다.");
    }

    @Test
    @DisplayName("헤더 템플릿이 정상적으로 치환된다.")
    void shouldParseHeaderTemplate() {
        EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetMessageData);

        assertThat(targetMessage.getHeaderMessage()).isEqualTo("header template");
    }

    @Test
    @DisplayName("푸터 템플릿이 정상적으로 치환된다.")
    void shouldParseFooterTemplate() {
        EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetMessageData);

        assertThat(targetMessage.getHeaderMessage()).isEqualTo("header template");
    }

    @Test
    @DisplayName("바디 정보 반환된다.")
    void shouldReturnBodyTemplate() {
        EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetMessageData);

        assertThat(targetMessage.getBodyMessage()).isEqualTo("body template");
    }

    @Test
    @DisplayName("이메일 메시지가 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenEmailSendMessageIsNull() {
        EmailTargetMessageCreateContext context = contextBuilder.sendMessage(null).build();

        assertThatThrownBy(()->EmailTargetMessage.of(context, targetMessageData))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("email_send_message");
    }

    @Test
    @DisplayName("첨부파일 목록은 JSON으로 정상 치환된다.")
    void shouldCreateJsonArrayForEachAttachment() {
        RenderedTemplateContent attachment = RenderedTemplateContentBuilder.builder().build();
        List<RenderedTemplateContent> attachments = List.of(
                attachment, attachment, attachment
        );
        EmailTargetMessageCreateContext context = contextBuilder
                .attachmentList(attachments)
                .build();

        EmailTargetMessage targetMessage = EmailTargetMessage.of(context, targetMessageData);

        String expect = JsonUtil.toJson(attachments);

        assertThat(targetMessage.getAttachments()).isEqualTo(expect);
    }

    @Test
    @DisplayName("첨부파일이 존재하지 않으면, '[]'를 반환한다.")
    void shouldReturnEmptyList_whenAttachmentListDoNotExist() {
        EmailTargetMessageCreateContext context = contextBuilder
                .attachmentList(List.of())
                .build();

        EmailTargetMessage targetMessage = EmailTargetMessage.of(context, targetMessageData);


        assertThat(targetMessage.getAttachments()).isEqualTo("[]");
    }
}