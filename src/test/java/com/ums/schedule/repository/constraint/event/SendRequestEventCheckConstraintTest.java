package com.ums.schedule.repository.constraint.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SendRequestEventCheckConstraintTest {
    @Nested
    @DisplayName("event_type 허용 범위 테스트")
    class EventTypeCheckConstraintTest {
        @Test
        @DisplayName("event_type에 REQUEST_CREATED를 입력하면 저장된다.")
        void shouldPersist_whenEventTypeIsRequestCreated() {

        }

        @Test
        @DisplayName("event_type에 SEND_REQUESTED를 입력하면 저장된다. ")
        void shouldPersist_whenEventTypeIsSendRequested() {

        }

        @Test
        @DisplayName("event_type에 TARGET_UPLOAD_REQUESTED를 입력하면 저장된다.")
        void shouldPersist_whenEventTypeIsTargetUploadRequested() {

        }

        @Test
        @DisplayName("event_type에 TARGET_UPLOAD_COMPLETED를 입력하면 저장된다")
        void shouldPersist_whenEventTypeIsTargetUploadCompleted() {

        }

        @Test
        @DisplayName("event_type에 SCHEDULED를 입력하면 저장된다.")
        void shouldPersist_whenEventTypeIsScheduled() {

        }

        @Test
        @DisplayName("event_type에 SEND_STARTED를 입력하면 저장된다.")
        void shouldPersist_whenEventTypeIsSendStarted() {

        }

        @Test
        @DisplayName("event_type에 SEND_ENDED를 입력하면 저장된다.")
        void shouldPersist_whenEventTypeIsSendEnded() {

        }

        @Test
        @DisplayName("event_type에 허용되지 않은 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenEventTypeIsInvalid() {

        }
    }

    @Nested
    @DisplayName("result_code 허용 범위 테스트")
    class ResultCodeCheckConstraintTest {
        @Test
        @DisplayName("result_code에 SUCCESS를 입력하면 저장된다.")
        void shouldPersist_whenResultCodeIsSuccess() {

        }

        @Test
        @DisplayName("result_code에 FAIL을 입력하면 저장된다.")
        void shouldPersist_whenResultCodeIsFail() {

        }

        @Test
        @DisplayName("result_code에 RETRY를 입력하면 저장된다.")
        void shouldPersist_whenResultCodeIsRetry() {

        }

        @Test
        @DisplayName("result_code에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenResultCodeIsInvalid() {

        }
    }
}
