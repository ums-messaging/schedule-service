package com.ums.schedule.application.sendrequest;

import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.provider.FileTargetUploadResult;
import com.ums.schedule.application.target.report.TargetUploadReportCreateService;
import com.ums.schedule.application.target.provider.FileTargetUploadUrlProvider;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.mapper.exception.EnumMapperNotFoundException;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.request.target.upload.builder.TargetUploadCreateCommandBuilder;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TargetUploadCreateServiceTest {
    @Mock private EnumMapperFactory factory;
    @Mock private TargetUploadProperties properties;
    @Mock private FileTargetUploadUrlProvider provider;
    @Mock private TargetUploadReportJpaRepository repository;
    @InjectMocks private TargetUploadReportCreateService targetUploadService;

    private TargetUploadCreateCommandBuilder commandBuilder;
    private SendRequest sendRequest;

    @BeforeEach
    void setUp() {
        commandBuilder = TargetUploadCreateCommandBuilder.builder();
        sendRequest = SendRequestEntityBuilder.builder()
                .id(1L)
                .state(new SendRequestCreateState())
                .customerRequestKey("hyejin_company", "my_request")
                .build();
    }

    @Nested
    @DisplayName("FileTargetUploadProvider 실행 테스트")
    class WhenFileTargetUploadProvider {
        @Test
        @DisplayName("업로드 유형이 FILE인 경우, FileTargetUploadUrlProvider가 실행된다.")
        void shouldCallFileTargetUploadUrlProvider_whenUploadTypeIsFile() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn(mock(TargetUploadReport.class)).when(repository).save(any(TargetUploadReport.class));

            targetUploadService.create(sendRequest, command);

            verify(provider).provide(any());
        }

        @Test
        @DisplayName("업로드 유형이 JSON인 경우, FileTargetUploadUrlProvider는 실행되지 않는다.")
        void shouldNotCallTargetUploadUrlProvider_whenUploadTypeisJson() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(mock(TargetUploadReport.class)).when(repository).save(any(TargetUploadReport.class));
            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());

            targetUploadService.create(sendRequest, command);

            verify(provider, never()).provide(any());
        }
        @Test
        @DisplayName("FileTargetUploadUrlProvider 처리 실패 시 대상자 업로드는 저장되지 않는다.")
        void shouldNotSave_whenFilTargetUploadUrlProviderFails() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doThrow(mock(BusinessException.class)).when(provider).provide(any());

            assertThatThrownBy(() -> targetUploadService.create(sendRequest, command));
            verify(repository, never()).save(any(TargetUploadReport.class));
        }
    }

    @Nested
    @DisplayName("TargetUploadProperties 테스트")
    class WhenTargetUploadProperties {
        @Test
        @DisplayName("설정 파일에서 다운로드 키 정보를 가져온다.")
        void shouldGetDownloadKeyPrefix() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(properties).getPrefixDownloadKey();
        }

        @Test
        @DisplayName("다운로드 키는 정해진 규칙에 의해 생성된다.")
        void shouldGenerateDownloadKeyAccordingToRule() {
            ArgumentCaptor<TargetUploadReport> captor = ArgumentCaptor.forClass(TargetUploadReport.class);
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(repository).save(captor.capture());
            TargetUploadReport report = captor.getValue();

            String expect = "/target/upload/download/hyejin_company/1/email/%s.xlsx".formatted(report.getId().toString());
            assertThat(report.getDownloadKey()).isEqualTo(expect);
        }

        @Test
        @DisplayName("다운로드 키 생성 경로가 빈 값이면 예외가 발생한다.")
        void shouldThrowException_whenDownloadKeyPrefixIsEmpty() {
            ArgumentCaptor<TargetUploadReport> captor = ArgumentCaptor.forClass(TargetUploadReport.class);
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();

            TargetUploadReportNotConfiguredException expect = TargetUploadReportNotConfiguredException.of(TargetUploadConfiguration.DOWNLOAD_KEY);

            assertThatThrownBy(() -> targetUploadService.create(sendRequest, command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
            }

        @Test
        @DisplayName("설정 파일에서 업로드 키 경로를 가져온다.")
        void shouldGetUploadkeyPrefix() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            verify(properties).getPrefixUploadKey();
        }
        @Test
        @DisplayName("업로드 키는 정해진 규칙에 의해 생성된다.")
        void shouldGenerateUploadKeyAccordingToRule() {
            ArgumentCaptor<TargetUploadReport> captor = ArgumentCaptor.forClass(TargetUploadReport.class);
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(repository).save(captor.capture());
            TargetUploadReport report = captor.getValue();

            String expect = "/target/upload/hyejin_company/1/email/%s.csv".formatted(report.getId().toString());
            assertThat(report.getUploadKey()).isEqualTo(expect);

        }

        @Test
        @DisplayName("업로드 키 생성 경로가 빈 값이면 예외가 발생한다.")
        void shouldThrowException_whenUploadKeyPrefixIsEmpty() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("").when(properties).getPrefixUploadKey();

            TargetUploadReportNotConfiguredException expect = TargetUploadReportNotConfiguredException.of(TargetUploadConfiguration.UPLOAD_KEY);

            assertThatThrownBy(() -> targetUploadService.create(sendRequest, command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Nested
    @DisplayName("업로드 포맷")
    class WhenUploadFormat {
        @Test
        @DisplayName("파일 업로드의 경우 업로드 포맷이 입력되지 않아도 대상자 업로드 리포트는 저장된다.")
        void shouldReturnCsv_whenTargetUploadTypeIsFileAndUploadFormatIsEmpty() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn(null).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            verify(repository).save(any(TargetUploadReport.class));
        }

        @Test
        @DisplayName("JSON 업로드의 경우 업로드 포맷이 입력되지 않아도 대상자 업로드 리포트는 저장된다.")
        void shouldNotSetUploadFormat_whenTargetUploadTypeIsJsonAndUploadFormatIsEmpty() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doReturn(null).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(repository).save(any(TargetUploadReport.class));
        }

        @Test
        @DisplayName("업로드 포맷 처리 실패 시 대상자 업로드 리포트는 저장되지 않는다.")
        void shouldThrowException_whenInvalidUploadFormat() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.JSON).build();

            doThrow(mock(EnumMapperNotFoundException.class)).when(factory).findEnumMapperValue(any() ,any());

            assertThatThrownBy(() -> targetUploadService.create(sendRequest, command));
            verify(repository,never()).save(any(TargetUploadReport.class));
        }
    }

    @Nested
    @DisplayName("대상자 업로드 리포트 생성 및 저장")
    class WhenTargetUploadReport {
        @Test
        @DisplayName("발송 요청 데이터에 대상자 업로드 리포트가 지정된다.")
        void shouldCreateTargetUploadReport() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            assertThat(sendRequest.getCurrentTargetUpload()).isNotNull();
        }

        @Test
        @DisplayName("발송 요청 데이터 상태는 HOLDING으로 변경된다.")
        void shouldChangeSendRequestStateToHolding() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            assertThat(sendRequest.getState().getCurrentCode()).isEqualTo(SendRequestStatus.HOLDING);
        }

        @Test
        @DisplayName("대상자 업로드 리포트가 저장된다.")
        void shouldSaveTargetUploadReport() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadType.FILE).build();

            doReturn("/target/upload/download").when(properties).getPrefixDownloadKey();
            doReturn("/target/upload").when(properties).getPrefixUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(repository).save(any(TargetUploadReport.class));
        }


        @Test
        @DisplayName("대상자 업로드 생성 실패 시 대상자 업로드 리포트는 저장되지 않는다.")
        void shouldNotSaveTargetUploadReport_whenTargetUploadCreateFails() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(null).build();

            assertThatThrownBy(() -> targetUploadService.create(sendRequest, command));

            verify(repository, never()).save(any(TargetUploadReport.class));
        }
    }
}