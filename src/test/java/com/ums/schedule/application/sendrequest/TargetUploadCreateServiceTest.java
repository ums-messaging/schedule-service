package com.ums.schedule.application.sendrequest;

import com.ums.schedule.application.exception.ApplicationException;
import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.EnumMapperNotFoundException;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.domain.sendrequest.state.SendRequestCreateState;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.sendrequest.target.upload.builder.TargetUploadCreateCommandBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @InjectMocks private TargetUploadCreateService targetUploadService;

    private TargetUploadCreateCommandBuilder commandBuilder;
    private SendRequest sendRequest;

    @BeforeEach
    void setUp() {
        commandBuilder = TargetUploadCreateCommandBuilder.builder();
        sendRequest = SendRequestEntityBuilder.builder()
                .state(new SendRequestCreateState())
                .build();
    }

    @Nested
    @DisplayName("FileTargetUploadProvider 실행 테스트")
    class WhenFileTargetUploadProvider {
        @Test
        @DisplayName("업로드 유형이 FILE인 경우, FileTargetUploadUrlProvider가 실행된다.")
        void shouldCallFileTargetUploadUrlProvider_whenUploadTypeIsFile() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();
            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn(mock(TargetUploadReport.class)).when(repository).save(any(TargetUploadReport.class));

            targetUploadService.create(sendRequest, command);

            verify(provider).provide(any());
        }

        @Test
        @DisplayName("업로드 유형이 JSON인 경우, FileTargetUploadUrlProvider는 실행되지 않는다.")
        void shouldNotCallTargetUploadUrlProvider_whenUploadTypeisJson() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.JSON).build();

            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn(mock(TargetUploadReport.class)).when(repository).save(any(TargetUploadReport.class));
            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());

            targetUploadService.create(sendRequest, command);

            verify(provider, never()).provide(any());
        }
        @Test
        @DisplayName("FileTargetUploadUrlProvider 처리 실패 시 대상자 업로드는 저장되지 않는다.")
        void shouldNotSave_whenFilTargetUploadUrlProviderFails() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();
            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doThrow(mock(ApplicationException.class)).when(provider).provide(any());

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
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.JSON).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(properties).getDownloadKey();
        }
        @Test
        @DisplayName("설정 파일에서 업로드 키 경로를 가져온다.")
        void shouldGetUploadkeyPrefix() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn(EnumMapperValue.fromEnumMapperType(TargetUploadFormatEnum.CSV)).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            verify(properties).getUploadKey();
        }
    }

    @Nested
    @DisplayName("업로드 포맷")
    class WhenUploadFormat {
        @Test
        @DisplayName("파일 업로드의 경우 업로드 포맷이 입력되지 않아도 대상자 업로드 리포트는 저장된다.")
        void shouldReturnCsv_whenTargetUploadTypeIsFileAndUploadFormatIsEmpty() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn(null).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            verify(repository).save(any(TargetUploadReport.class));
        }

        @Test
        @DisplayName("JSON 업로드의 경우 업로드 포맷이 입력되지 않아도 대상자 업로드 리포트는 저장된다.")
        void shouldNotSetUploadFormat_whenTargetUploadTypeIsJsonAndUploadFormatIsEmpty() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.JSON).build();

            doReturn(null).when(factory).findEnumMapperValue(any() ,any());
            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(repository).save(any(TargetUploadReport.class));
        }

        @Test
        @DisplayName("업로드 포맷 처리 실패 시 대상자 업로드 리포트는 저장되지 않는다.")
        void shouldThrowException_whenInvalidUploadFormat() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.JSON).build();

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
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            assertThat(sendRequest.getCurrentTargetUpload()).isNotNull();
        }

        @Test
        @DisplayName("발송 요청 데이터 상태는 HOLDING으로 변경된다.")
        void shouldChangeSendRequestStateToHolding() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();
            doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

            targetUploadService.create(sendRequest, command);

            assertThat(sendRequest.getState().getCurrentCode()).isEqualTo(SendRequestStatusEnum.HOLDING);
        }

        @Test
        @DisplayName("대상자 업로드 리포트가 저장된다.")
        void shouldSaveTargetUploadReport() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

            doReturn("/target/upload/download").when(properties).getDownloadKey();
            doReturn("/target/upload").when(properties).getUploadKey();

            targetUploadService.create(sendRequest, command);

            verify(repository).save(any(TargetUploadReport.class));
        }


        @Test
        @DisplayName("대상자 업로드 생성 실패 시 대상자 업로드 리포트는 저장되지 않는다.")
        void shouldNotSaveTargetUploadReport_whenTargetUploadCreateFails() {
            TargetUploadCreateCommand command = commandBuilder.uploadType(null).build();
            doReturn("/target/upload/download").when(properties).getDownloadKey();

            assertThatThrownBy(() -> targetUploadService.create(sendRequest, command));

            verify(repository, never()).save(any(TargetUploadReport.class));
        }
    }
}