package com.ums.schedule.application.ums.email.security;

import com.ums.schedule.application.ums.email.config.SecurityMailProperties;
import com.ums.schedule.application.ums.email.exception.SecurityMailNotConfiguredException;
import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.email.security.PasswordType;
import com.ums.schedule.common.code.email.security.SecurityMailCode;
import com.ums.schedule.fixture.email.security.SecurityMailCommandBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityMailAssemblerTest {
    @Mock private EnumMapperFactory factory;
    @Mock private SecurityMailProperties properties;
    @InjectMocks private SecurityMailAssembler assembler;

    private SecurityMailCommandBuilder commandBuilder;
    private Map<SecurityMailCode, String> policyMap;
    private Map<PasswordType, String> passwordTypeMap;

    @BeforeEach
    void setUp() {
        this.commandBuilder = SecurityMailCommandBuilder.builder();
        this.policyMap = commandBuilder.getPolicyMap();
        this.passwordTypeMap = commandBuilder.getPasswordTypeMap();
    }

    @Nested
    @DisplayName("보안 메일 정책 테스트")
    class WhenSecurityMailPolicy {
        @Nested
        @DisplayName("암호화 타입")
        class WhenEncryptionType {

            @Test
            @DisplayName("설정 파일에서 암호화 타입 기본 값을 조회한다.")
            void shouldGetEncryptionTypeDefaultValue() {
                SecurityMailCommand command = commandBuilder.build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(properties).defaultEncryptType();
            }

            @Test
            @DisplayName("암호화 타입 기본 값이 빈 값이면 예외가 발생한다.")
            void shouldThrowException_whenEncryptionTypeDefaultValueIsEmpty() {
                SecurityMailCommand command = commandBuilder.build();
                doReturn("").when(properties).defaultEncryptType();

                SecurityMailNotConfiguredException expect = SecurityMailNotConfiguredException.of(SecurityMailCode.ENCRYPTION_TYPE);

                assertThatThrownBy(() -> assembler.assemble(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("보안 메일 정책이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenSecurityPolicyDoesNotExist() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(null)
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("ASE-126"));
            }

            @Test
            @DisplayName("암호화 타입 입력 값이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenEncryptionTypeIsEmpty() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(Map.of(SecurityMailCode.ENCRYPTION_TYPE, ""))
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("ASE-126"));
            }

            @Test
            @DisplayName("암호화 타입 입력 값이 존재하면 입력 값이 반환된다.")
            void shouldReturnEncryptionType_whenEncryptionTypeIsNotEmpty() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(Map.of(SecurityMailCode.ENCRYPTION_TYPE, "ASE-256"))
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("ASE-256"));
            }
        }

        @Nested
        @DisplayName("비밀번호 해시")
        class WhenPasswordHash {

            @Test
            @DisplayName("설정 파일에서 비밀번호 해시 기본 값을 조회한다.")
            void shouldGetPasswordHashDefaultValue() {
                SecurityMailCommand command = commandBuilder.build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(properties).defaultPasswordHash();
            }

            @Test
            @DisplayName("비밀번호 해시 기본 값이 빈 값이면 예외가 발생한다.")
            void shouldThrowException_whenPasswordHashDefaultValueIsEmpty() {
                SecurityMailCommand command = commandBuilder.build();

                doReturn("ASE-126").when(properties).defaultEncryptType();
                doReturn("").when(properties).defaultPasswordHash();

                SecurityMailNotConfiguredException expect = SecurityMailNotConfiguredException.of(SecurityMailCode.PASSWORD_HASH);

                assertThatThrownBy(() -> assembler.assemble(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("보안 메일 정책이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenSecurityPolicyDoesNotExist() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(null)
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("SHA-256"));
            }

            @Test
            @DisplayName("비밀번호 해시 입력 값이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenPasswordHashIsEmpty() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(Map.of(SecurityMailCode.PASSWORD_HASH, ""))
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("SHA-256"));
            }

            @Test
            @DisplayName("비밀번호 해시 입력 값이 존재하면 입력 값이 반환된다.")
            void shouldReturnEncryptionType_whenEncryptionTypeIsNotEmpty() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(Map.of(SecurityMailCode.PASSWORD_HASH, "SHA-128"))
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("SHA-128"));
            }
        }

        @Nested
        @DisplayName("접근 권한")
        class WhenPermissionMask {
            @Test
            @DisplayName("설정 파일에서 접근 권한 기본 값을 조회한다.")
            void shouldGetPermissionMaskDefaultValue() {
                SecurityMailCommand command = commandBuilder.build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(properties).defaultPermissionMask();
            }

            @Test
            @DisplayName("접근 권한 타입 기본 값이 빈 값이면 예외가 발생한다.")
            void shouldThrowException_whenPermissionMaskDefaultValueIsEmpty() {
                SecurityMailCommand command = commandBuilder.build();

                doReturn("ASE-128").when(properties).defaultEncryptType();
                doReturn("SHA-256").when(properties).defaultPasswordHash();
                doReturn("").when(properties).defaultPermissionMask();

                SecurityMailNotConfiguredException expect = SecurityMailNotConfiguredException.of(SecurityMailCode.PERMISSION_MASK);

                assertThatThrownBy(() -> assembler.assemble(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("보안 메일 정책이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenSecurityPolicyDoesNotExist() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(null)
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("NONE"));
            }

            @Test
            @DisplayName("접근 권한 입력 값이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenPasswordHashIsEmpty() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(Map.of(SecurityMailCode.PERMISSION_MASK, ""))
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("NONE"));
            }

            @Test
            @DisplayName("접근 권한 입력 값이 존재하면 입력 값이 반환된다.")
            void shouldReturnEncryptionType_whenEncryptionTypeIsNotEmpty() {
                ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
                SecurityMailCommand command = commandBuilder
                        .policyMap(Map.of(SecurityMailCode.PASSWORD_HASH, "ALL"))
                        .build();
                givenSecurityMailAllConfigure();
                doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());

                assembler.assemble(command);

                verify(factory, times(3)).findEnumMapperValue(any(), captor.capture());
                assertThat(captor.getAllValues())
                        .anyMatch(s -> s.equals("ALL"));
            }
        }
    }

    private void givenSecurityMailAllConfigure() {
        doReturn("ASE-126").when(properties).defaultEncryptType();
        doReturn("SHA-256").when(properties).defaultPasswordHash();
        doReturn("NONE").when(properties).defaultPermissionMask();
        doReturn("birthday").when(properties).defaultPasswordPolicy();
    }

    @Nested
    @DisplayName("비밀번호 타입 테스트")
    class WhenPasswordType {
        @BeforeEach
        void setUp() {
            givenConfiguredPasswordPolicy();
            doReturn(mock(EnumMapperValue.class)).when(factory).findEnumMapperValue(any(), any());
        }

        private void givenConfiguredPasswordPolicy() {
            doReturn("ASE-126").when(properties).defaultEncryptType();
            doReturn("SHA-256").when(properties).defaultPasswordHash();
            doReturn("NONE").when(properties).defaultPermissionMask();
        }

        @Nested
        @DisplayName("비밀번호 정책")
        class WhenPasswordPolicy {
            @Test
            @DisplayName("설정 파일에서 비밀번호 정책 기본 값을 조회한다.")
            void shouldGetPasswordPolicyDefaultValue() {
                SecurityMailCommand command = commandBuilder.build();
                doReturn("birthday").when(properties).defaultPasswordPolicy();

                assembler.assemble(command);

                verify(properties).defaultPasswordPolicy();
            }

            @Test
            @DisplayName("비밀번호 정책 기본 값이 빈 값이면 예외가 발생한다.")
            void shouldThrowException_whenPasswordPolicyDefaultValueIsEmpty() {
                SecurityMailCommand command = commandBuilder
                        .build();
                doReturn("").when(properties).defaultPasswordPolicy();

                SecurityMailNotConfiguredException expect = SecurityMailNotConfiguredException.of(PasswordType.PASSWORD_POLICY);

                assertThatThrownBy(() -> assembler.assemble(command))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
            }

            @Test
            @DisplayName("비밀번호 타입이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenPasswordTypeDoesNotExist() {
                SecurityMailCommand command = commandBuilder
                        .passwordTypeMap(null)
                        .build();
                doReturn("birthday").when(properties).defaultPasswordPolicy();

                SecurityMail result = assembler.assemble(command);

                assertThat(result.passwordPolicy())
                        .isEqualTo("birthday");
            }

            @Test
            @DisplayName("비밀번호 정책 입력 값이 존재하지 않으면 설정 파일에서 조회한 값이 반환된다.")
            void shouldReturnConfiguredValue_whenPasswordPolicyIsEmpty() {
                SecurityMailCommand command = commandBuilder
                        .passwordTypeMap(Map.of(PasswordType.PASSWORD_POLICY, ""))
                        .build();
                doReturn("birthday").when(properties).defaultPasswordPolicy();

                SecurityMail result = assembler.assemble(command);

                assertThat(result.passwordPolicy())
                        .isEqualTo("birthday");
            }

            @Test
            @DisplayName("비밀번호 정책 입력값이 존재하면 입력값이 반환된다.")
            void shouldReturnPasswordPolicy_whenCommandValueIsNotEmpty() {
                SecurityMailCommand command = commandBuilder
                        .passwordTypeMap(Map.of(PasswordType.PASSWORD_POLICY, "id"))
                        .build();
                doReturn("birthday").when(properties).defaultPasswordPolicy();

                SecurityMail result = assembler.assemble(command);

                assertThat(result.passwordPolicy())
                        .isEqualTo("id");
            }

        }

        @Nested
        @DisplayName("비밀번호 형식")
        class WhenPasswordFormat {

            @BeforeEach
            void setUp() {
                doReturn("birthday").when(properties).defaultPasswordPolicy();
            }

            @Test
            @DisplayName("비밀번호 형식 입력 값이 존재하지 않으면 NULL을 반환한다.")
            void shouldReturnNull_whenPasswordFormatDoesNotExist() {
                SecurityMailCommand command = commandBuilder
                        .passwordTypeMap(Map.of(PasswordType.PASSWORD_POLICY, ""))
                        .build();

                SecurityMail result = assembler.assemble(command);

                assertThat(result.passwordFormat())
                        .isNull();
            }
        }
    }
}