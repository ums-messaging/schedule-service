package com.ums.schedule.domain.target.message;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplateContent;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.common.code.target.SendTargetColumn;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.util.JsonUtil;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import com.ums.schedule.fixture.email.EmailTemplateContentBuilder;
import com.ums.schedule.fixture.email.attachment.AttachmentPayloadBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyBuilder;
import com.ums.schedule.fixture.target.SendTargetResultBuilder;
import com.ums.schedule.fixture.target.TargetMessageDataBuilder;
import com.ums.schedule.fixture.target_upload.EmailGeneratorContextBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;

class EmailTargetMessageTest {
    private EmailTemplateBuilder templateBuilder;
    private EmailSendMessage sendMessage;
    private TargetUploadReport targetUploadReport;

    private EmailGeneratorContextBuilder contextBuilder;
    private SendTargetResultBuilder targetBuilder;

    @BeforeEach
    void setUp() throws IOException {
        this.sendMessage = mock(EmailSendMessage.class);
        this.targetUploadReport = mock(TargetUploadReport.class);
        this.templateBuilder = generateEmailTemplate();
        this.contextBuilder = givenGenerateContext();
        this.targetBuilder = givenSendTargetResult();
    }

    private SendTargetResultBuilder givenSendTargetResult() {
        return SendTargetResultBuilder.builder()
                .resultCode(SendTargetResultCode.SUCCESS)
                .targetData(createTargetMessage());
    }

    private EmailGeneratorContextBuilder givenGenerateContext() {
        return EmailGeneratorContextBuilder.builder()
                .template(templateBuilder.build())
                .emailSendMessage(sendMessage)
                .targetUploadReport(targetUploadReport);
    }

    private EmailTemplateBuilder generateEmailTemplate() throws IOException {
        return EmailTemplateBuilder.builder()
                .body(createTemplate("${body_template}"));
    }

    private EmailTemplateContent createTemplate(String template) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        return EmailTemplateContentBuilder.builder()
                .template(new Template("test", template, configuration))
                .build();
    }
    private TargetMessageData createTargetMessage() {
        Map<SendTargetColumn, String> targetData = Map.of(
                SendTargetColumn.TARGET_KEY, "its_me",
                SendTargetColumn.TARGET_EMAIL, "jang314@test.com",
                SendTargetColumn.TARGET_NAME, "jang"
        );
        Map<String, Object> dataParam = new HashMap<>();
        dataParam.put("header_template", "header_message");
        dataParam.put("title", "hello world");
        dataParam.put("body_template", "body_message");
        dataParam.put("footer_template", "footer_message");
        dataParam.put("attachment_name", "my_attachment_name");
        dataParam.put("download_name", "my_download_name");
        return new TargetMessageData(
                "hyejin_company",
                3,
                targetData,
                dataParam
        );
    }

    @Nested
    @DisplayName("생성 테스트")
    class WhenCreateEmailTargetMessage {
        @Test
        @DisplayName("target_upload_report가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadReportDoesNotExist() {
            EmailGeneratorContext context = contextBuilder.targetUploadReport(null).build();

            assertThatThrownBy(() -> EmailTargetMessage.of(context, targetBuilder.build()))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("target_upload_report is not null.");
        }

        @Test
        @DisplayName("email_send_message가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenEmailSendMessageDoesNotExist() {
            EmailGeneratorContext context = contextBuilder.emailSendMessage(null).build();

            assertThatThrownBy(() -> EmailTargetMessage.of(context, targetBuilder.build()))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("email_send_message is not null.");
        }

        @Test
        @DisplayName("대상자 결과 코드가 성공이 아니면, 상태는 FAIL이다.")
        void shouldReturnFail_whenSendResultCodeIsNotSuccess() {
            SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.MESSAGE_PARSING_ERROR)
                    .build();
            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);

            assertThat(targetMessage.getState().getCurrentCode())
                    .isEqualTo(SendTargetStatus.FAIL);
        }

        @Test
        @DisplayName("대상자 결과 코드가 성공이면, 상태는 CREATE이다.")
        void shouldReturnCreate_whenSendResultCodeIsSuccess() {
            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetBuilder.build());

            assertThat(targetMessage.getState().getCurrentCode())
                    .isEqualTo(SendTargetStatus.CREATE);
        }

        @Test
        @DisplayName("그룹 결과가 대상자 치환 변수에 추가된다.")
        void shouldPutTargetData() {
            SendTargetResult target = targetBuilder
                    .rowNo(3)
                    .partitionNo(3)
                    .build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
            Map<String, Object> dataParam = targetMessage.getDataParamMap();

            assertThat(dataParam)
                    .containsEntry("rowNo", 3)
                    .containsEntry("partitionNo", 3);
        }

        @Test
        @DisplayName("대상자 치환 변수가 정상적으로 생성된다.")
        void shouldCreateTargetMessageVariable() {
            SendTargetResult target = targetBuilder
                    .build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
            Map<String, Object> dataParam = targetMessage.getDataParamMap();

            assertThat(dataParam)
                    .containsEntry("title", "hello world")
                    .containsEntry("header_template", "header_message")
                    .containsEntry("body_template", "body_message")
                    .containsEntry("footer_template", "footer_message")
                    .containsEntry("attachment_name", "my_attachment_name")
                    .containsEntry("download_name", "my_download_name");
        }

        @Test
        @DisplayName("대상자 치환 변수는 JSON으로 치환된다.")
        void shouldParseTargetMessageVariable() {
            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetBuilder.build());
            Map<String, Object> dataParam = targetMessage.getDataParamMap();
            String expect = JsonUtil.toJson(dataParam);

            assertThat(targetMessage.getMessageVariable()).isEqualTo(expect);
        }

        @Test
        @Disabled
        @DisplayName("대상자 치환 변수가 JSON으로 변환 실패 시 Map정보가 반환된다.")
        void shouldReturnMap_whenJsonParsingFails() {
            String value = """
                    "
                    """;
            Map<String, Object> dataParam = Map.of(
                    "Test", value
            );
            SendTargetResult target = targetBuilder.targetData(
                    TargetMessageDataBuilder.builder()
                            .dataParam(dataParam)
                            .build()
            ).build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);

            assertThat(targetMessage)
                    .extracting(v -> v.getState().getCurrentCode(), EmailTargetMessage::getMessageVariable)
                    .contains(SendTargetStatus.FAIL, dataParam.toString());
        }

        @Test
        @DisplayName("target_key가 존재하지 않으면 상태는 FAIL로 변경된다.")
        void shouldChangeStateToFail_whenTargetKeyDoesNotExist() {
            TargetMessageData targetData = TargetMessageDataBuilder.builder()
                    .targetData(Map.of(
                            SendTargetColumn.TARGET_NAME, "hyejin",
                            SendTargetColumn.TARGET_EMAIL, "jang314@test.com"
                            ))
                    .build();
            SendTargetResult target = targetBuilder.targetData(targetData).build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);

            assertThat(targetMessage)
                    .extracting(v -> v.getState().getCurrentCode(), v -> v.getResultMessage())
                    .contains(SendTargetStatus.FAIL, "대상자 KEY은(는) 필수 값 입니다.");
        }

        @Test
        @DisplayName("target_name이 존재하지 않으면 상태는 FAIL로 변경된다.")
        void shouldChangeStateToFail_whenTargetNameDoesNotExist() {
            TargetMessageData targetData = TargetMessageDataBuilder.builder()
                    .targetData(Map.of(
                            SendTargetColumn.TARGET_KEY, "jang",
                            SendTargetColumn.TARGET_EMAIL, "jang314@test.com"
                    ))
                    .build();
            SendTargetResult target = targetBuilder.targetData(targetData).build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);

            assertThat(targetMessage)
                    .extracting(v -> v.getState().getCurrentCode(), v -> v.getResultMessage())
                    .contains(SendTargetStatus.FAIL, "이름은(는) 필수 값 입니다.");
        }

        @Test
        @DisplayName("target_email이 존재하지 않으면 상태는 FAIL로 변경된다.")
        void shouldChangeStateFail_whenTargetEmailDoesNotExist() {
            TargetMessageData targetData = TargetMessageDataBuilder.builder()
                    .targetData(Map.of(
                            SendTargetColumn.TARGET_KEY, "jang",
                            SendTargetColumn.TARGET_NAME, "hyejin"
                    ))
                    .build();
            SendTargetResult target = targetBuilder.targetData(targetData).build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);

            assertThat(targetMessage)
                    .extracting(v -> v.getState().getCurrentCode(), v -> v.getResultMessage())
                    .contains(SendTargetStatus.FAIL, "이메일은(는) 필수 값 입니다.");
        }

        @Test
        @DisplayName("대상자 치환 변수 대상자 기본정보가 생성된다.")
        void shouldCreateTargetColumnInfo() {
            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetBuilder.build());
            Map<String, Object> dataParam = targetMessage.getDataParamMap();

            assertThat(dataParam)
                    .containsEntry("key", "its_me")
                    .containsEntry("name", "jang")
                    .containsEntry("email", "jang314@test.com");
        }
    }

    @Nested
    @DisplayName("템플릿 치환 테스트")
    class WhenParseTemplate {
        private EmailTargetMessage targetMessage;
        private EmailTemplateBuilder templateBuilder;
        private EmailConvertPolicyBuilder policyBuilder;

        @BeforeEach
        void setUp() throws IOException {
            targetMessage = EmailTargetMessage.of(contextBuilder.build(), targetBuilder.build());

            policyBuilder = EmailConvertPolicyBuilder.builder()
                    .body(createTemplate("${body_template}").template());

            templateBuilder = EmailTemplateBuilder.builder()
                    .header(createTemplate("${header_template}"))
                    .footer(createTemplate("${footer_template}"))
                    .title("${title}");
        }

        private EmailTemplateContent createTemplate(String message) throws IOException {
            return EmailTemplateContentBuilder.builder()
                    .template(new Template("my_template", message, new Configuration(Configuration.VERSION_2_3_32)))
                    .build();
        }

        @Test
        @DisplayName("상태가 CREATE가 아니면, 템플릿은 치환되지 않는다.")
        void shouldNotParse_whenStateIsNotCreate() {
            SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.TARGET_ROW_READ_FAIL).build();

            EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
            targetMessage.renderTemplate(templateBuilder.build(), policyBuilder.build());

            assertThat(targetMessage)
                    .extracting(
                            EmailTargetMessage::getSubject,
                            EmailTargetMessage::getHeaderMessage,
                            EmailTargetMessage::getBodyMessage,
                            EmailTargetMessage::getFooterMessage,
                            EmailTargetMessage::getAttachments
                            )
                    .contains(null, null,null,null, null);

        }

        @Nested
        @DisplayName("제목 치환 테스트")
        class WhenParsingSubject {
            @Test
            @DisplayName("제목이 존재하지 않으면 상태는 FAIL로 변경된다.")
            void shouldChangeStateToFail_whenSubjectDoesNotExist() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                                .title(null)
                        .build(), policyBuilder.build());

                String resultMessage = SendTargetResultCode.TEMPLATE_EMPTY.description()
                        .formatted("subject");

                assertThat(targetMessage)
                        .extracting(v -> v.getState().getCurrentCode(), v -> v.getResultMessage())
                        .contains(SendTargetStatus.FAIL, resultMessage);
            }

            @Test
            @DisplayName("치환 실패 시상태는 FAIL로 변경된다.")
            void shouldChangeStateToFail_whenSubjectParsingFails() {
                SendTargetResult target = targetBuilder
                        .resultCode(SendTargetResultCode.SUCCESS)
                        .build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .title("${fruit}")
                        .build(), policyBuilder.build());

                String resultMessage = SendTargetResultCode.TARGET_VARIABLE_REQUIRED.description()
                        .formatted("subject", "fruit");

                assertThat(targetMessage)
                        .extracting(v -> v.getState().getCurrentCode(), v -> v.getResultMessage())
                        .contains(SendTargetStatus.FAIL, resultMessage);
            }

            @Test
            @DisplayName("제목은 정상적으로 치환된다.")
            void shouldParseSubject() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .build(), policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getSubject(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage());
            }
        }

        @Nested
        @DisplayName("헤더 템플릿 치환 테스트")
        class WhenParsingHeaderTemplate {
            @Test
            @DisplayName("헤더 템플릿이 존재하지 않을 시 헤더 템플릿은 치환되지 않는다.")
            void shouldNotParse_whenHeaderTemplateDoesNotExist() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .header(null)
                        .build(), policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getHeaderMessage(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage())
                        .contains(null, SendTargetStatus.CREATE, null);
            }

            @Test
            @DisplayName("헤더 템플릿이 존재할 시 헤더 템플릿은 치환된다.")
            void shouldParse_whenHeaderTemplateExists() throws IOException {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                                .header(createTemplate("${header_template}"))
                        .build(), policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getHeaderMessage(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage())
                        .contains("header_message", SendTargetStatus.CREATE, null);
            }

            @Test
            @DisplayName("헤더 템플릿 치환 실패 시 상태는 FAIL로 변경된다.")
            void shouldChangeStateToFail_whenHeaderTemplateParsingFails() throws IOException {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(
                        templateBuilder
                                .header(createTemplate("${header_template_1}"))
                                .build(),
                        policyBuilder.build());


                assertThat(targetMessage)
                        .extracting(
                                v -> v.getHeaderMessage(),
                                v -> v.getState().getCurrentCode())
                        .contains(null, SendTargetStatus.FAIL);
            }
        }

        @Nested
        @DisplayName("푸터 템플릿 치환 테스트")
        class WhenParsingFooterTemplate {
            @Test
            @DisplayName("푸터 템플릿이 존재하지 않을 시 푸터 템플릿은 치환되지 않는다.")
            void shouldNotParse_whenFooterTemplateDoesNotExist() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .footer(null)
                        .build(), policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getFooterMessage(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage())
                        .contains(null, SendTargetStatus.CREATE, null);
            }

            @Test
            @DisplayName("푸터 템플릿이 존재할 시 푸터 템플릿은 치환된다.")
            void shouldParse_whenFooterTemplateExists() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .build(), policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getFooterMessage(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage())
                        .contains("footer_message", SendTargetStatus.CREATE, null);
            }

            @Test
            @DisplayName("푸터 템플릿 치환 실패 시 상태는 FAIL로 변경된다.")
            void shouldChangeStateToFail_whenFooterTemplateParsingFails() throws IOException {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(
                        templateBuilder
                                .footer(createTemplate("${footer_template_1}"))
                                .build(),
                        policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getFooterMessage(),
                                v -> v.getState().getCurrentCode())
                        .contains(null, SendTargetStatus.FAIL);
            }
        }

        @Nested
        @DisplayName("바디 템플릿 치환 테스트")
        class WhenParsingBodyTemplate {
            @Test
            @DisplayName("바디 템플릿이 존재하지 않을 시 상태는 실패로 변경된다.")
            void shouldChangeStateToFail_whenBodyTemplateDoesNotExist() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .build(), policyBuilder
                                .body(null)
                        .build());

                String resultMessage = SendTargetResultCode.TEMPLATE_EMPTY.description()
                                .formatted("body");

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getBodyMessage(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage())
                        .contains(null, SendTargetStatus.FAIL, resultMessage);
            }

            @Test
            @DisplayName("바디 템플릿이 존재할 시 바디 템플릿은 치환된다.")
            void shouldParse_whenBodyTemplateExists() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(templateBuilder
                        .build(), policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getBodyMessage(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage())
                        .contains("body_message", SendTargetStatus.CREATE, null);
            }

            @Test
            @DisplayName("바디 템플릿 치환 실패 시 상태는 FAIL로 변경된다.")
            void shouldChangeStateToFail_whenFooterTemplateParsingFails() throws IOException {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(
                        templateBuilder
                                .footer(createTemplate("${footer_template_1}"))
                                .build(),
                        policyBuilder.build());

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getFooterMessage(),
                                v -> v.getState().getCurrentCode())
                        .contains(null, SendTargetStatus.FAIL);
            }
        }

        @Nested
        @DisplayName("첨부파일 목록 테스트")
        class WhenParsingAttachmentList {
            private EmailTargetMessage targetMessage;
            private EmailConvertPolicy policy;
            @BeforeEach
            void setUp() {
                targetMessage= EmailTargetMessage.of(contextBuilder.build(), targetBuilder.build());
                AttachmentPayload payload = givenAttachmentPayload();
                policy = policyBuilder.attachments(payload, payload, payload).build();
            }

            private AttachmentPayload givenAttachmentPayload() {
                return AttachmentPayloadBuilder.builder()
                        .fileKey("attachment.html")
                        .attachmentName("${attachment_name}")
                        .downloadName("${download_name}")
                        .build();
            }

            @Test
            @DisplayName("첨부파일 목록이 존재하면 치환된다.")
            void shouldParseJson_whenAttachmentListDoExist() {
                targetMessage.renderTemplate(templateBuilder.build(), policy);
                String json = targetMessage.getAttachments();

                List<AttachmentPayload> expect = JsonUtil.toList(json, AttachmentPayload.class);

                assertThat(expect)
                        .extracting(
                                AttachmentPayload::fileKey,
                                AttachmentPayload::attachmentName,
                                AttachmentPayload::downloadName
                        )
                        .contains(
                                Tuple.tuple("attachment.html", "my_attachment_name", "my_download_name"),
                                Tuple.tuple("attachment.html", "my_attachment_name", "my_download_name"),
                                Tuple.tuple("attachment.html", "my_attachment_name", "my_download_name")
                        );
            }

            @Test
            @DisplayName("첨부파일 치환이 실패하면 상태는 FAIL로 변경된다.")
            void shouldChangeStateToFail_whenParsingFails() {
                SendTargetResult target = targetBuilder.resultCode(SendTargetResultCode.SUCCESS).build();

                EmailTargetMessage targetMessage = EmailTargetMessage.of(contextBuilder.build(), target);
                targetMessage.renderTemplate(
                        templateBuilder.build(),
                        policyBuilder
                                .attachments(AttachmentPayloadBuilder.builder()
                                        .fileKey("${file_key}.html")
                                        .attachmentName("${attachment_name}.pdf")
                                        .downloadName("${download_name}.pdf")
                                        .build())
                                .build());

                String resultMessage = SendTargetResultCode.TARGET_VARIABLE_REQUIRED.description()
                                .formatted("attachment", "file_key");

                assertThat(targetMessage)
                        .extracting(
                                v -> v.getAttachments(),
                                v -> v.getState().getCurrentCode(),
                                v -> v.getResultMessage()
                        )
                        .contains(null, SendTargetStatus.FAIL, resultMessage);
            }
        }
    }
}