package com.ums.schedule.repository.constraint.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TargetUploadCheckConstraintTest {
    @Nested
    @DisplayName("status 허용 범위 테스트")
    class StatusCheckConstraintTest {
        @Test
        @DisplayName("status에 CREATE를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsCreate() {

        }

        @Test
        @DisplayName("status에 PENDING을 입력하면 저장된다.")
        void shouldPersist_whenStatusIsPending() {

        }

        @Test
        @DisplayName("status에 PARSING을 입력하면 저장된다.")
        void shouldPersist_whenStatusIsParsing() {

        }

        @Test
        @DisplayName("status에 UPLOAD를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsUpload() {

        }

        @Test
        @DisplayName("status에 FAIL을 입력하면 저장된다.")
        void shouldPersist_whenStatusIsFail() {

        }

        @Test
        @DisplayName("status에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenStatusIsInvalid() {

        }
    }

    @Nested
    @DisplayName("upload_type 허용 범위 테스트")
    class UploadTypeCheckConstraintTest {
        @Test
        @DisplayName("upload_type에 FILE을 입력하면 저장된다.")
        void shouldPersist_whenUploadTypeIsFile() {

        }

        @Test
        @DisplayName("upload_type에 JSON을 입력하면 저장된다.")
        void shouldPersist_whenUploadTypeIsJson() {

        }

        @Test
        @DisplayName("upload_type에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenUploadTypeIsInvalid() {

        }
    }
}
