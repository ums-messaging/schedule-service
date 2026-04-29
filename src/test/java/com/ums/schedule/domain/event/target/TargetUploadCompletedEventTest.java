package com.ums.schedule.domain.event.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TargetUploadCompletedEventTest {

    @Test
    @DisplayName("TargetUploadState가 UPLOAD일 때, TargetUploadCompleteEvent가 발행되면 TargetUploadState는 Complete가 반횐된다.")
    void shouldReturnTargetUploadStateIsComplete_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUploadState가 CREATE일 때, TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsCreate() {

    }

    @Test
    @DisplayName("TargetUploadState가 PENDING일 때 TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsPending() {

    }

    @Test
    @DisplayName("TargetUploadState가 PARSING일 때 TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsParsing() {

    }

    @Test
    @DisplayName("TargetUploadState가 UPLOAD일 때 TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUploadState가 COMPLETE일 때 TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsComplete() {

    }

    @Test
    @DisplayName("SendRequest 상태가 CREATE일 때, TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStateIsCreate() {

    }

    @Test
    @DisplayName("SendRequest 상태가 PENDING일 때, TargetUploadCompleteEvent가 발행되면 SendRequest의 상태는 READY로 변경된다.")
    void shouldReturnSendRequestStateIsReady_whenSendRequestStateIsPending() {

    }

    @Test
    @DisplayName("SendRequest 상태가 READY일 때, TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStateIsReady() {

    }

    @Test
    @DisplayName("SendRequest 상태가 REQUEST일 때, TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStateIsRequest() {

    }

    @Test
    @DisplayName("SendRequest 상태가 SENDING 일 때, TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStateIsSending() {

    }

    @Test
    @DisplayName("SendRequest 상태가 COMPLETE 일 때, TargetUploadCompleteEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStateIsComplete() {

    }
}