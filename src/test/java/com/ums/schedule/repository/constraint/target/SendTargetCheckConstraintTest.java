package com.ums.schedule.repository.constraint.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SendTargetCheckConstraintTest {
    @Nested
    @DisplayName("status 허용 범위 테스트")
    class StatusCheckConstraintTest {
        @Test
        @DisplayName("status에 READY를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsReady() {

        }

        @Test
        @DisplayName("status에 ERROR를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsError() {

        }

        @Test
        @DisplayName("status에 RETRY를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsRetry() {

        }

        @Test
        @DisplayName("status에 COMPLETE를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsComplete() {

        }

        @Test
        @DisplayName("status에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenStatusIsInvalid() {

        }
    }
}
