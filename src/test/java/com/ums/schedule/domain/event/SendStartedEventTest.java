package com.ums.schedule.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendStartedEventTest {
    @Test
    @DisplayName("SendRequestStatus가 HOLDING이면 SENDING을 반환한다.")
    void shouldReturnSendRequestStatusIsSending_whenSendRequestStatusIsHolding() {

    }

    @Test
    @DisplayName("SendRequestStatus가 HOLDING이면 SendRequestEvent의 타입은 SEND_STARTED이다.")
    void shouldReturnSendRequestEventTypeIsSendStarted_whenSendRequestStatusIsHolding() {

    }

    @Test
    @DisplayName("SendRequestStatus가 CREATED이면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsCreated() {

    }

    @Test
    @DisplayName("SendRequestStatus가 SENDING이면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsSending() {

    }

    @Test
    @DisplayName("SendRequestStatus가 COMPLETE이면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsComplete() {

    }
}