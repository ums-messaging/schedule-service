package com.ums.schedule.application.target.generator;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;
import com.ums.schedule.application.ums.email.exception.EmailMessageConvertException;
import com.ums.schedule.application.ums.email.generator.EmailTargetMessageGenerator;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.resolver.EmailConvertResolver;
import com.ums.schedule.common.code.api.EmailMessageErrorCode;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.email.EmailTemplateBuilder;
import com.ums.schedule.fixture.email.convert.EmailConvertPolicyBuilder;
import com.ums.schedule.fixture.target.SendTargetResultBuilder;
import com.ums.schedule.fixture.target.TargetMessageDataBuilder;
import com.ums.schedule.fixture.target_upload.EmailGeneratorContextBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmailTargetMessageGeneratorTest {
    @Mock private EmailConvertResolver resolver;
    @InjectMocks private EmailTargetMessageGenerator generator;


    @Test
    @DisplayName("업로드 결과가 FAIL이면, EmailConvertPolicyResolver는 실행되지 않는다.")
    void shouldNotInvokeEmailConvertPolicyResolver_whenResultCodeIsFail() {
        EmailGeneratorContext context = givenGeneratorContext();

        SendTargetResult target = givenSendTargetResult(SendTargetResultCode.TARGET_ROW_READ_FAIL);

        generator.generate(context, target);

        verify(resolver, never()).resolve(any(), any());
    }

    private SendTargetResult givenSendTargetResult(SendTargetResultCode resultCode) {
        TargetMessageData targetMessage = TargetMessageDataBuilder.builder().dataParam(
                Map.of(
                        "month", 3
                )
        ).build();

        SendTargetResult target = SendTargetResultBuilder.builder()
                .resultCode(resultCode)
                .targetData(targetMessage)
                .build();
        return target;
    }

    private EmailGeneratorContext givenGeneratorContext() {
        return EmailGeneratorContextBuilder.builder()
                .targetUploadReport(mock(TargetUploadReport.class))
                .emailSendMessage(mock(EmailSendMessage.class))
                .template(EmailTemplateBuilder.builder().build())
                .build();
    }

    @Test
    @DisplayName("메시지 변환 실패 시 대상자 상태는 FAIL이다.")
    void shouldReturnFail_whenMessageConvertFails() {
        EmailGeneratorContext context = givenGeneratorContext();

        SendTargetResult target = givenSendTargetResult(SendTargetResultCode.SUCCESS);

        EmailMessageConvertException givenException = EmailMessageConvertException.of(EmailMessageErrorCode.NOT_CONVERT_MESSAGE);
        doThrow(givenException).when(resolver).resolve(any(), any());

        EmailTargetMessage result = generator.generate(context, target);

        String resultMessage = SendTargetResultCode.MESSAGE_GENERATE_FAIL.description().formatted(
                givenException.getErrorMessage()
        );

        assertThat(result)
                .extracting(
                        v -> assertThat(v.getState().getCurrentCode()).isEqualTo(SendTargetStatus.FAIL),
                        v -> assertThat(v.getResultMessage())
                                .contains(resultMessage)
                );
    }

    @Test
    @DisplayName("업로드 결과가 SUCCESS이면, 대상자 상태는 CREATED이다.")
    void shouldReturnCreated_whenResultCodeIsSuccess() throws IOException {
        EmailGeneratorContext context = givenGeneratorContext();
        SendTargetResult target = givenSendTargetResult(SendTargetResultCode.SUCCESS);

        doReturn(EmailConvertPolicyBuilder.builder()
                .body(new Template("body_template", "body", new Configuration(Configuration.VERSION_2_3_32)))
                .build())
                .when(resolver).resolve(any(), any());

        EmailTargetMessage result = generator.generate(context, target);

        assertThat(result.getState().getCurrentCode())
                .isEqualTo(SendTargetStatus.CREATE);
    }
}