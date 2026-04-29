package com.ums.schedule.domain.event.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetCreatedEventTest {

    @Test
    @DisplayName("TargetCreatedEvent가 발행되면 TargetUpload의 상태는 CREATE가 반환된다.")
    void shouldReturnTargetUploadStatusIsCreate_whenTargetCreatedEvent() {

    }

    @Test
    @DisplayName("TargetUpload의 상태가 PARSING 일 때, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsParsing() {

    }

    @Test
    @DisplayName("TargetUpload의 상태가 PENDING 일 때, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsPending() {

    }

    @Test
    @DisplayName("TargetUpload의 상태가 UPLOAD 일 때, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUpload의 상태가 COMPLETE 일 때, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsComplete() {

    }

    @Test
    @DisplayName("SendRequest의 상태가 CREATE일 때, TargetCreatedEvent가 발행되면, SendRequest의 상태는 PENDING으로 반환된다.")
    void shouldSendRequestStateIsPending_whenSendRequestStateIsCreate() {

    }

    @Test
    @DisplayName("SendRequest의 상태가 PENDING일 때, TargetCreatedEvent가 발행되면 익셉션이 발생한다. ")
    void shouldSendRequestStateIsPending_whenSendRequestStateIsPending() {

    }

    @Test
    @DisplayName("SendRequest의 상태가 READY일 때, TargetCreatedEvent가 발행되면 SendRequest의 상태는 PENDING이 반환된다.")
    void shouldReturnSendRequestStateIsPending_whenSendRequestStateIsReady() {

    }

    @Test
    @DisplayName("SendRequest의 상태가 REQUEST일 떄, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenSendRequestStateIsRequest() {

    }

    @Test
    @DisplayName("SendRequest의 상태가 SENDING일 때, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenSendRequestStateIsSending() {

    }

    @Test
    @DisplayName("SendRequest의 상태가 Complete일 때, TargetCreatedEvent가 발행되면 예외가 발생한다.")
    void shouldThrowException_whenSendRequestStateIsComplete() {

    }
}