package com.ums.schedule.repository.constraint.channel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EmailSendRequestCheckConstraintTest {
    @Nested
    @DisplayName("convert_type 허용 범위 테스트")
    class ConvertTypeCheckConstraintTest {
        @Test
        @DisplayName("convert_type에 NONE을 입력하면 저장된다.")
        void shouldPersist_whenConvertTypeIsNone() {

        }

        @Test
        @DisplayName("convert_type에 PDF를 입력하면 저장된다.")
        void shouldPersist_whenConvertTypeIsPdf() {

        }

        @Test
        @DisplayName("convert_type에 HTML을 입력하면 저장된다.")
        void shouldPersist_whenConvertTypeIsHtml() {

        }

        @Test
        @DisplayName("convert_type에 허용되지 않는 값일 입력하면 예외가 발생한다.")
        void shouldThrowException_whenConvertTypeIsInvalid(){

        }
    }

    @Nested
    @DisplayName("encryption_type 허용 범위 테스트")
    class EncryptionTypeCheckConstraintTest {
        @Test
        @DisplayName("encryption_type에 ASE-128을 입력하면 저장된다.")
        void shouldPersist_whenEncryptionTypeIsAse128(){

        }

        @Test
        @DisplayName("encryption_type에 ASE-256을 입력하면 저장된다.")
        void shouldPersist_whenEncryptionTypeIsAse256(){

        }

        @Test
        @DisplayName("encryption_type에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenEncryptionTypeIsInvalid() {

        }
    }

    @Nested
    @DisplayName("password_hash 허용 범위 테스트")
    class PasswordHashCheckConstraintTest {
        @Test
        @DisplayName("password_hash에 SHA-256을 입력하면 저장된다.")
        void shouldPersist_whenPasswordHashIsSha256(){

        }

        @Test
        @DisplayName("password_hash에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenPasswordHashIsInvalid() {

        }
    }

    @Nested
    @DisplayName("permission_mask 허용 범위 테스트")
    class PermissionMaskCheckConstraintTest {
        @Test
        @DisplayName("permission_mask에 ALL을 입력하면 저장된다.")
        void shouldPersist_whenPermissionMaskIsAll() {

        }

        @Test
        @DisplayName("permission_mask에 COPY를 입력하면 저장된다.")
        void shouldPersist_whenPermissionMaskIsCopy() {

        }

        @Test
        @DisplayName("permission_mask에 PRINT를 입력하면 저장된다.")
        void shouldPersist_whenPermissionMaskIsPrint() {

        }

        @Test
        @DisplayName("permission_mask에 WRITE를 입력하면 저장된다.")
        void shouldPersist_whenPermissionMaskIsWrite() {

        }

        @Test
        @DisplayName("permission_mask에 NONE을 입력하면 저장된다.")
        void shouldPersist_whenPermissionMaskIsNone() {

        }

        @Test
        @DisplayName("permission_mask에 허용되지 않는 범위의 값을 입력하면 예외가 발생한다.")
        void shouldThrowException_whenPermissionMaskIsInvalid() {

        }
    }
}
