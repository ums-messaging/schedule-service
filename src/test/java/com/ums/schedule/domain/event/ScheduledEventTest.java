package com.ums.schedule.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledEventTest {
    @Test
    @DisplayName("SendRequestStatus가 REQUEST이면, HOLDING이 반환된다.")
    void shouldReturnSendRequestStatusIsHolding_whenSendRequestStatusIsRequest() {

    }

    @Test
    @DisplayName("SendRequestStatus가 REQUEST이면, SendRequestEvent의 타입은 SCHEDULED가 반환된다.")
    void shouldReturnSendRequestEventTypeIsScheduled_whenSendRequestStatusIsRequest() {

    }

    @Test
    @DisplayName("SendRequestStatus가 CREATE이면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsCreate() {

    }

    @Test
    @DisplayName("SendRequestStatus가 HOLDING이면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsHolding() {

    }

    @Test
    @DisplayName("SendRequestStatus가 SENDING이면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsSending() {

    }

    @Test
    @DisplayName("SendRequestStatus가 COMPLETE이면, 익셉션이 발생한다.")
    void shouldThrowException_whenSendRequestStatusIsComplete() {

    }


}