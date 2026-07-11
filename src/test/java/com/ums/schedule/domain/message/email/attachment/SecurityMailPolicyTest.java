package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.domain.message.exception.SecurityMailPolicyNotFoundException;
import com.ums.schedule.domain.message.email.code.PasswordTypeEnum;
import com.ums.schedule.domain.message.email.code.SecurityMailEnumMapper;
import com.ums.schedule.domain.message.email.SecurityMailPolicy;
import com.ums.schedule.fixture.email.attachment.SecurityMailBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityMailPolicyTest {
    private SecurityMailBuilder securityBuilder;

    @BeforeEach
    void setUp() {
        this.securityBuilder = SecurityMailBuilder.builder();
    }
    @Test
    @DisplayName("암호화 타입이 존재하지 않으면, 예외가 발생한다.")
    void shouldThrowException_whenEncryptionTypeIsNull() {
        SecurityMail securityMail = securityBuilder.encryptionType(null).build();

        SecurityMailPolicyNotFoundException expect = SecurityMailPolicyNotFoundException.of(SecurityMailEnumMapper.ENCRYPTION_TYPE);

        assertThatThrownBy(() -> SecurityMailPolicy.of(securityMail))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage())
        ;
    }

    @Test
    @DisplayName("접근 권한 타입이 존재하지 않으면, 예외가 발생한다.")
    void shouldThrowException_whenPermissionMaskIsNull() {
        SecurityMail securityMail = securityBuilder.permissionMask(null).build();

        SecurityMailPolicyNotFoundException expect = SecurityMailPolicyNotFoundException.of(SecurityMailEnumMapper.PERMISSION_MASK);

        assertThatThrownBy(() -> SecurityMailPolicy.of(securityMail))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("비밀번호 해시가 존재하지 않으면, 예외가 발생한다.")
    void shouldThrowException_whenPasswordHashIsNull() {
        SecurityMail securityMail = securityBuilder.passwordHash(null).build();

        SecurityMailPolicyNotFoundException expect = SecurityMailPolicyNotFoundException.of(SecurityMailEnumMapper.PASSWORD_HASH);

        assertThatThrownBy(() -> SecurityMailPolicy.of(securityMail))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("비밀번호 정책이 빈 값이면, 예외가 발생한다.")
    void shouldThrowException_whenPasswordPolicyIsEmpty() {
        SecurityMail securityMail = securityBuilder.passwordPolicy(null).build();

        SecurityMailPolicyNotFoundException expect = SecurityMailPolicyNotFoundException.of(PasswordTypeEnum.PASSWORD_POLICY);
        assertThatThrownBy(() -> SecurityMailPolicy.of(securityMail))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("비밀번호 포맷이 빈 값이면 NULL을 반환한다.")
    void shouldReturnNull_whenPasswordFormatIsNull() {
        SecurityMail securityMail = securityBuilder.passwordFormat(null).build();

        SecurityMailPolicy policy = SecurityMailPolicy.of(securityMail);

        assertThat(policy.getPasswordFormat()).isNull();
    }
}