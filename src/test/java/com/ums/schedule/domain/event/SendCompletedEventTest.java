package com.ums.schedule.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SendCompletedEventTest {
    @Test
    @DisplayName("SendRequestStatus가 SENDING이면, COMPLETE가 반환된다.")
    void shouldReturnSendRequestStatusIsComplete_whenSendRequestStatusIsSending() {

    }

    @Test
    @DisplayName("SendRequestStatus가 SENDING이면, SendRequestEvent타입은 SEND_ENDED이다.")
    void shouldSendRequestEventTypeIsSendEnded_whenSendRequestStatusIsSending() {

    }

    @Test
    @DisplayName("SendRequestStatus가 CREATE이면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsCreate() {

    }

    @Test
    @DisplayName("SendRequestStatus가 REQUEST이면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsRequest() {

    }

    @Test
    @DisplayName("SendRequestStatus가 COMPLETE이면 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsComplete() {

    }


}