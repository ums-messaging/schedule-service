package com.ums.schedule.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SendRequestEventTest {

    @Test
    @DisplayName("발송 요청이 오면 발송 요청 Event가 발행되고, SendRequest와 TargetUpload 상태가 초기화된다.")
    void shouldProduceSendRequestStatusAndTargetUploadStatusIsInit_whenCreateSendRequest() {
    }

    @Test
    @DisplayName("메시지 생성이 완료되면, MessageStatus 상태가 COMPLETE로 변경된다.")
    void shouldReturnMessageStatusIsComplete_whenMessageCreated() {
    }

    @Test
    @DisplayName("대상자 업로드가 완료되면, SendTargetStatus는 READY로 변경되고 TargetUpload Status는 COMPLETE로 변경된다.")
    void shouldReturnSendTargetStatusIsReadyAndTargetUploadStatusIsComplete_whenTargetUploaded() {
    }

    @Test
    @DisplayName("대상자 업로드 및 메시지 생성외 완료되면 SendReadyEvent가 발생되어, SendRequestSTatus가 READY로 변경된다.")
    void shouldReturnSendRequestStatusIsReady_whenTargetUploadAndMessageComplete() {
    }

    @Test
    @DisplayName("SendRequest 이벤트가 발행되면, SendRequest의 상태는 REQUEST로 변경된다.")
    void shouldReturnSendRequestStatusIsRequest_whenSendRequestEvent() {
    }

    @Test
    @DisplayName("SendRequest의 Status가 READY가 아니면 오류가 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsNotReady() {
    }

    @Test
    @DisplayName("MessageStatus 혹은 TargetUpload Status가 ERROR이면 SendRequest의 Status는 ERROR로 변경된다.")
    void shouldReturnSendRequestStatusIsError_whenMessageOrTargetUploadStatusIsError() {
    }
}