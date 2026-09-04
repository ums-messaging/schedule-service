package com.ums.schedule.application.ums.email.request;

import com.ums.schedule.adapter.api.request.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;
import com.ums.schedule.application.sendrequest.command.SendRequestCreateCommand;
import com.ums.schedule.application.sendrequest.email.command.EmailSendCreateRequestBuilder;
import com.ums.schedule.application.ums.common.request.SendRequestCreateService;
import com.ums.schedule.application.ums.common.request.model.SendRequestCreateResult;
import com.ums.schedule.application.ums.email.message.EmailMessageCreateService;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSendRequestCreateServiceTest {
    @Mock private EmailMessageCreateService messageService;
    @Mock private SendRequestCreateService sendRequestService;
    @InjectMocks private EmailSendRequestCreateService emailRequestService;

    private SendRequestCreateRequest request;

    @BeforeEach
    void setUp() {
        request = givenSendRequest();
    }


    private SendRequestCreateRequest givenSendRequest() {
        return SendRequestCreateRequestBuilder.builder()
                .scheduleId(1L)
                .templateKey("my_template")
                .senderKey("test@test.com")
                .customerRequestId("my_send_request")
                .uploadFormat("csv")
                .retryCount(3)
                .build();
    }

    @Test
    @DisplayName("이메일 메시지가 생성된다.")
    void shouldCreateEmailSendMessage() {
        doReturn(mock(EmailSendMessage.class)).when(messageService).create(any(), any());
        doReturn(mock(SendRequestCreateResult.class)).when(sendRequestService).create(any(), any());

        EmailSendCreateRequest request = EmailSendCreateRequestBuilder.builder().request(this.request).build();
        emailRequestService.create("hyejin_company", TargetUploadType.FILE, request);

        verify(messageService).create("hyejin_company", request);

    }

    @Test
    @DisplayName("이메일 메시지가 생성되면 발송 요청이 생성된다.")
    void shouldCreateSendRequest() {
        ArgumentCaptor<SendRequestCreateCommand> captor = ArgumentCaptor.forClass(SendRequestCreateCommand.class);

        doReturn(mock(EmailSendMessage.class)).when(messageService).create(any(), any());
        doReturn(mock(SendRequestCreateResult.class)).when(sendRequestService).create(any(), any());

        EmailSendCreateRequest request = EmailSendCreateRequestBuilder.builder()
                .senderKey("test@test.com")
                .request(this.request).build();

        emailRequestService.create("hyejin_company", TargetUploadType.FILE, request);
        verify(sendRequestService).create(captor.capture(), any());

        assertThat(captor.getValue())
                .extracting(
                        SendRequestCreateCommand::scheduleId,
                        SendRequestCreateCommand::templateKey,
                        SendRequestCreateCommand::senderKey,
                        SendRequestCreateCommand::customerKey,
                        SendRequestCreateCommand::uploadFormat,
                        SendRequestCreateCommand::channelType,
                        SendRequestCreateCommand::uploadType,
                        SendRequestCreateCommand::retryCnt
                )
                .containsExactly(1L,
                        "my_template",
                        "test@test.com",
                        "my_send_request",
                        "csv",
                        ChannelType.EMAIL,
                        TargetUploadType.FILE,
                        3
                );
    }
}