package com.ums.schedule.adapter.api.request.email;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.adapter.api.config.ObjectMapperConfig;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.common.exception.handler.GlobalExceptionHandler;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.request.email.validator.EmailSendRequestValidator;
import com.ums.schedule.application.ums.common.request.model.TargetUploadCreateSummary;
import com.ums.schedule.application.ums.email.request.EmailSendRequestCreateService;
import com.ums.schedule.application.ums.email.request.model.EmailSendRequestCreateSummary;
import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.common.code.api.EmailSendRequestErrorCode;
import com.ums.schedule.common.code.api.SendRequestErrorCode;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.mapper.EnumMapperType;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.fixture.email.EmailSendRequestCreateSummaryBuilder;
import com.ums.schedule.fixture.target_upload.TargetUploadCreateSummaryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FileEmailSendRequestControllerTest {
    @Mock private EmailSendRequestValidator validator;
    @Mock private EmailSendRequestCreateService requestService;
    @InjectMocks  private FileEmailSendRequestController controller;

    private final Map<String, Object> jsonMap = new HashMap<>();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(
                        new MappingJackson2HttpMessageConverter(
                                new ObjectMapperConfig().objectMapper()))
                .build();
        givenEmailSendRequestCreateValidator();
    }

    private void givenEmailSendRequestCreateValidator() {
        when(validator.supports(EmailSendCreateRequest.class)).thenReturn(true);
        doNothing().when(validator).validate(any(), any());
    }

    @Nested
    @DisplayName("발송 요청 : 파일 URL 발급")
    class WhenSendRequestWithFileUpload {
        private final String URL = "/api/v1/send-requests/email/upload-url";

        @BeforeEach
        void setUp() {
            jsonMap.put("mail_type", "PLAIN");
            jsonMap.put("schedule_id", 1);
            jsonMap.put("template_key", "my_template");
            jsonMap.put("sender_key", "test@test.com");
            jsonMap.put("customer_request_id", "request1111");
            jsonMap.put("title", "hello world!");
        }

        @Nested
        @DisplayName("유효성 검증")
        class WhenValidation {

            @Test
            @DisplayName("스케쥴 ID가 존재하지 않으면, 400 에러를 반환한다.")
            void shouldReturn400Error_whenScheduleIdIsNull() throws Exception {
                String json = givenValueAndGetJson("schedule_id", null);

                willReturnBadRequestMockMvcResult(json, "schedule_id", SendRequestErrorCode.SCHEDULE_REQUIRED);
            }

            @Test
            @DisplayName("제목이 빈 값이면, 400 에러를 반환한다.")
            void shouldReturn400_whenTitleIsBlank() throws Exception{
                String json = givenValueAndGetJson("title", " ");

                willReturnBadRequestMockMvcResult(json, "title", EmailSendRequestErrorCode.TITLE_REQUIRED);
            }

            @Test
            @DisplayName("고객 요청 키가 존재하지 않으면, 400 에러를 반환한다.")
            void shouldReturn400Error_whenCustomerRequestIdIsNull() throws Exception {
                String json = givenValueAndGetJson("customer_request_key", " ");

                willReturnBadRequestMockMvcResult(json, "customer_request_key", SendRequestErrorCode.CUSTOMER_KEY_REQUIRED);
            }

            @Test
            @DisplayName("템플릿 키가 존재하지 않으면, 400에러를 반환한다.")
            void shouldReturn400Error_whenTemplateKeyIsNull() throws Exception {
                String json = givenValueAndGetJson("template_key", " ");

                willReturnBadRequestMockMvcResult(json, "template_key",
                        SendRequestErrorCode.TEMPLATE_KEY_REQUIRED);
            }

            @Test
            @DisplayName("발신자 키가 존재하지 않으면, 400 에러를 반환한다.")
            void shouldReturn400Error_whenSenderKeyIsNull() throws Exception {
                String json = givenValueAndGetJson("sender_key", " ");

                willReturnBadRequestMockMvcResult(json, "sender_key",
                        SendRequestErrorCode.SENDER_KEY_REQUIRED);
            }

            @Test
            @DisplayName("발신자 키가 이메일 형식이 아니면, 400에러를 반환한다.")
            void shouldReturn400Error_whenSenderKeyIsInvalid() throws Exception {
                String json = givenValueAndGetJson("sender_key", "01012345678");

                willReturnBadRequestMockMvcResult(json, "sender_key",
                        EmailSendRequestErrorCode.INVALID_SENDER_EMAIL);
            }

            @Test
            @DisplayName("보안 정책이 존재하고, 비밀번호 정책이 빈 값이면 400 에러를 반환한다.")
            void shouldReturn400Error_whenSecurityPolicyExistsAndPwPolicyIsEmpty() throws Exception {
                String json =  """
                    {
                        %s,
                        "security_policy" : { "password_policy" : " "}
                    }
                    """.formatted(givenRequestJson());

                willReturnBadRequestMockMvcResult(json, "security_policy.password_policy",
                        EmailSendRequestErrorCode.PASSWORD_POLICY_REQUIRED);
            }

            @Test
            @DisplayName("첨부파일 목록이 존재하고, 첨부파일 명이 빈 값이면 400 에러를 반환한다.")
            void shouldReturn400Error_whenAttachmentListExistsAndAttachmentNameIsEmpty() throws Exception {
                String json =  """
                    {
                        %s,
                        "attachment_list" : [
                            {
                                "attachment_name" : "",
                                "download_name" : "world.xlsx"
                            },
                            {
                                "attachment_name" : "hello.xlsx",
                                "download_name" : "world.xlsx"
                            }
                        ]
                    }
                    """.formatted(givenRequestJson());

                willReturnBadRequestMockMvcResult(json, "attachment_list[0].attachment_name",
                        EmailSendRequestErrorCode.ATTACHMENT_NAME_REQUIRED);
            }

            @Test
            @DisplayName("첨부파일 목록이 존재하고, 첨부파일 명이 빈 값이면 400 에러를 반환한다.")
            void shouldReturn400Error_whenAttachmentListExistsAndDownloadNameIsEmpty() throws Exception {
                String json =  """
                    {
                        %s,
                        "attachment_list" : [
                            {
                                "attachment_name" : "hello.xlsx",
                                "download_name" : "world.xlsx"
                            },
                            {
                                "attachment_name" : "hello.xlsx",
                                "download_name" : " "
                            }
                        ]
                    }
                    """.formatted(givenRequestJson());

                willReturnBadRequestMockMvcResult(json, "attachment_list[1].download_name",
                        EmailSendRequestErrorCode.DOWNLOAD_NAME_REQUIRED);
            }

            private String givenValueAndGetJson(String field, String v) {
                jsonMap.put(field, v);
                return """
                        {
                            %s
                        }
                        """.formatted(givenRequestJson());
            }

            private void willReturnBadRequestMockMvcResult(String json, String field, EnumMapperType errorCode) throws Exception {
                givenMockMvcResult(json)
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(ApiResponseCode.BAD_REQUEST.code()))
                        .andExpect(jsonPath("$.message").value(ApiResponseCode.BAD_REQUEST.description()))
                        .andExpect(jsonPath("$.status").value(ApiResponseCode.BAD_REQUEST.value()))
                        .andExpect(jsonPath("$.details[*].field", hasItem(field)))
                        .andExpect(jsonPath("$.details[*].reason", hasItem(errorCode.description())));
            }
        }

        @Test
        @DisplayName("이메일 발송 요청이 성공적으로 생성되어, 201 성공 코드를 반환한다.")
        void shouldReturn200SuccessCode() throws Exception {
            EmailSendRequestCreateSummary summary = givenSummary();
            String requestJson = """
                    {
                        %s,
                        "security_policy" : { 
                            "password_policy" : "birthday" 
                        },
                        "attachment_list" : [
                            {
                                "attachment_name" : "hello.csv",
                                "download_name" : "world.csv"
                            },
                            {
                                "attachment_name" : "hello.csv",
                                "download_name" : "world.csv"
                            }
                        ]
                    }
                    """.formatted(givenRequestJson());

            ResultActions result = givenMockMvcResult(requestJson);

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(ApiResponseCode.CREATED.code()))
                    .andExpect(jsonPath("$.status").value(ApiResponseCode.CREATED.value()))
                    .andExpect(jsonPath("$.message").value(ApiResponseCode.CREATED.description()))
                    .andExpect(jsonPath("$.details.request_id").value(summary.requestId()))
                    .andExpect(jsonPath("$.details.retry_count").value(summary.retryCount()))
                    .andExpect(jsonPath("$.details.message_id").value(summary.messageId()))
                    .andExpect(jsonPath("$.details.convert_type").value(summary.convertType().code()))
                    .andExpect(jsonPath("$.details.attachment_count").value(summary.attachmentCount()))
                    .andExpect(jsonPath("$.details.current_target_upload.upload_id").value(summary.targetUploadSummary().uploadId()))
                    .andExpect(jsonPath("$.details.current_target_upload.upload_type").value(summary.targetUploadSummary().uploadType().code()))
                    .andExpect(jsonPath("$.details.current_target_upload.status").value(summary.targetUploadSummary().status().code()))
                    .andExpect(jsonPath("$.details.current_target_upload.object_key").value(summary.targetUploadSummary().objectKey()))
                    .andExpect(jsonPath("$.details.current_target_upload.presigned_url").value(summary.targetUploadSummary().presignedUrl()))
            ;
        }

        private String givenRequestJson() {
            return """
                        "mail_type" : "%s",
                        "schedule_id" : %d,
                        "template_key" : "%s",
                        "sender_key" : "%s",
                        "customer_request_key": "%s",
                        "title" : "%s"
                    """.formatted(
                            jsonMap.get("mail_type"),
                        jsonMap.get("schedule_id"),
                        jsonMap.get("template_key"),
                        jsonMap.get("sender_key"),
                        jsonMap.get("customer_request_key"),
                        jsonMap.get("title")
            );
        }

        private EmailSendRequestCreateSummary givenSummary() {
            TargetUploadCreateSummary targetUpload = givenTargetUploadSummary();
            EmailSendRequestCreateSummary summary = givenSendRequestSummary(targetUpload);
            doReturn(summary)
                    .when(requestService).create(anyString(), any(),
                            any(EmailSendCreateRequest.class));
            return summary;
        }

        private EmailSendRequestCreateSummary givenSendRequestSummary(TargetUploadCreateSummary targetUpload) {
            return EmailSendRequestCreateSummaryBuilder.builder()
                    .requestId(1L)
                    .retryCount(3)
                    .attachmentCount(1)
                    .messageId(UuidCreator.getTimeOrdered().toString())
                    .convertType(ConvertType.PDF)
                    .targetUploadSummary(targetUpload)
                    .build();
        }

        private TargetUploadCreateSummary givenTargetUploadSummary() {
            String uploadId = UuidCreator.getTimeOrdered().toString();
            TargetUploadCreateSummary targetUpload = TargetUploadCreateSummaryBuilder
                    .builder()
                    .uploadId(uploadId)
                    .expiredAt(Instant.now())
                    .uploadType(TargetUploadType.FILE)
                    .objectKey("/target/upload/%s.csv".formatted(uploadId))
                    .status(TargetUploadStatus.WAITING)
                    .presignedUrl("http://presigned_url.com/%s".formatted(uploadId))
                    .build();
            return targetUpload;
        }

        private ResultActions givenMockMvcResult(String json) throws Exception {
            return mvc.perform(
                    MockMvcRequestBuilders.post(URL)
                            .header("X-CUSTOMER-ID", "hyejin_company")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON)
            );
        }
    }
}