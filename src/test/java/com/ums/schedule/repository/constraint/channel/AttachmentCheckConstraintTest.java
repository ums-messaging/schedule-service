package com.ums.schedule.repository.constraint.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AttachmentCheckConstraintTest {
    @Nested
    @DisplayName("storage_type 허용범위 테스트")
    class StorageTypeCheckConstraintTest {
        @Test
        @DisplayName("storage_type에 S3를 입력하면 저장된다.")
        void shouldPersist_whenStorageTypeIsS3() {

        }

        @Test
        @DisplayName("storage_type에 LOCAL을 입력하면 저장된다.")
        void shouldPersist_whenStorageTypeIsLocal() {

        }
    }
}
