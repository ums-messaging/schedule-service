package com.ums.schedule.repository.constraint.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SendRequestCheckConstraintTest {
    @Nested
    @DisplayName("status 허용 범위 테스트")
    class StatusCheckConstraintTest {
        @Test
        @DisplayName("status에 CREATE를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsCreate(){

        }

        @Test
        @DisplayName("status에 HOLD를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsHold() {

        }

        @Test
        @DisplayName("status에 REQUEST를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsRequest() {

        }

        @Test
        @DisplayName("status에 READY를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsReady() {

        }

        @Test
        @DisplayName("status에 SCHEDULED를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsScheduled() {

        }

        @Test
        @DisplayName("status에 SENDING을 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsSending() {

        }

        @Test
        @DisplayName("status에 COMPLETE를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsComplete() {

        }

        @Test
        @DisplayName("status에 ERROR를 입력하면 저장된다.")
        void shouldSaveSendRequest_whenStatusIsError() {

        }

        @Test
        @DisplayName("status에 허용되지 않은 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenStatusIsInvalid(){

        }
    }

    @Nested
    @DisplayName("channel_type 허용 범위 테스트")
    class ChannelTypeCheckConstraintTest {
        @Test
        @DisplayName("channel_type에 EMAIL을 입력하면 저장된다.")
        void shouldSaveSendRequest_whenChannelTypeIsEmail(){

        }
        @Test
        @DisplayName("channel_Type에 허용되지 않은 값을 입력하면 익셉션이 발생한다.")
        void shouldThrowException_whenChannelTypeIsInvalid(){

        }
    }
}
