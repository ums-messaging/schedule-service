package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.exception.ApplicationException;
import com.ums.schedule.application.sendrequest.command.TargetUploadCreateCommand;
import com.ums.schedule.application.sendrequest.target.result.FileTargetUploadResult;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.domain.sendrequest.state.SendRequestCreateState;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.sendrequest.target.upload.builder.TargetUploadCreateCommandBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TargetUploadCreateServiceTest {
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

    @Test
    @DisplayName("업로드 유형이 FILE인 경우, FileTargetUploadUrlProvider가 실행된다.")
    void shouldCallFileTargetUploadUrlProvider_whenUploadTypeIsFile() {
        TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

        doReturn("/target/upload/download").when(properties).getDownloadKey();
        doReturn("/target/upload").when(properties).getUploadKey();
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

        targetUploadService.create(sendRequest, command);

        verify(provider, never()).provide(any());
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
    @DisplayName("FileTargetUploadUrlProvider 처리 실패 시 대상자 업로드는 저장되지 않는다.")
    void shouldNotSave_whenFilTargetUploadUrlProviderFails() {
        TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

        doReturn("/target/upload/download").when(properties).getDownloadKey();

        doThrow(mock(ApplicationException.class)).when(provider).provide(any());

        assertThatThrownBy(() -> targetUploadService.create(sendRequest, command));
        verify(repository, never()).save(any(TargetUploadReport.class));
    }

    @Test
    @DisplayName("설정 파일에서 다운로드 키 정보를 가져온다.")
    void shouldGetDownloadKeyPrefix() {
        TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.JSON).build();

        doReturn("/target/upload/download").when(properties).getDownloadKey();

        targetUploadService.create(sendRequest, command);

        verify(properties).getDownloadKey();
    }

    @Test
    @DisplayName("대상자 업로드 생성 실패 시 대상자 업로드 리포트는 저장되지 않는다.")
    void shouldNotSaveTargetUploadReport_whenTargetUploadCreateFails() {
        TargetUploadCreateCommand command = commandBuilder.uploadType(null).build();
        doReturn("/target/upload/download").when(properties).getDownloadKey();

        assertThatThrownBy(() -> targetUploadService.create(sendRequest, command));

        verify(repository, never()).save(any(TargetUploadReport.class));
    }

    @Test
    @DisplayName("업로드 유형이 FILE이면 설정 파일에서 업로드 키 경로를 가져온다.")
    void shouldGetUploadkeyPrefix_whenTargetUploadTypeIsFile() {
        TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

        doReturn("/target/upload/download").when(properties).getDownloadKey();
        doReturn("/target/upload").when(properties).getUploadKey();
        doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

        targetUploadService.create(sendRequest, command);

        verify(properties).getUploadKey();
    }

    @Test
    @Disabled
    @DisplayName("발송 요청 상태는 HOLDING으로 변경된다.")
    void shouldChangeSendRequestStateToHolding() {
        TargetUploadCreateCommand command = commandBuilder.uploadType(TargetUploadTypeEnum.FILE).build();

        doReturn("/target/upload/download").when(properties).getDownloadKey();
        doReturn("/target/upload").when(properties).getUploadKey();
        doReturn(mock(FileTargetUploadResult.class)).when(provider).provide(any());

        targetUploadService.create(sendRequest, command);

        assertThat(sendRequest.getState().getCurrentCode()).isEqualTo(SendRequestStatusEnum.HOLDING);

    }



    @Nested
    @DisplayName("파일 업로드 생성")
    class WhenFileTargetUploadUrlProviderTest {
        @Mock private EnumMapperFactory factory;
        @Mock private AwsS3Repository repository;
        @InjectMocks private FileTargetUploadUrlProvider provider;

        @Test
        @DisplayName("UPLOAD_FORMAT이 유요하지 않은 값이 입력되면 예외가 발생한다.")
        void shouldThrowException_whenInvalidUploadFormat() {

        }

        @Test
        @DisplayName("UPLOAD_FORMAT이 입력되지 않으면 CSV를 반환한다.")
        void shouldReturnCsv_whenUploadFormatIsNull() {

        }

        @Test
        @DisplayName("presigned_url이 발급된다.")
        void shouldIssuePresignedUrl() {

        }

        @Test
        @DisplayName("presigned_url 발급 실패 시 예외가 변환된다.")
        void shouldThrowException_whenPresignedUrlFails() {

        }

    }

}