package com.ums.schedule.application.target.reader;

import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.reader.model.FileTargetUploadRequestedEvent;
import com.ums.schedule.application.target.report.TargetUploadReportService;
import com.ums.schedule.application.target.uploader.EmailTargetUploader;
import com.ums.schedule.application.target.uploader.TargetUploader;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.target.upload.state.TargetUploadRequestState;
import com.ums.schedule.fixture.target_upload.FileTargetUploadRequestedEventBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TargetUploadFileReaderTest {
    @Mock private EmailTargetUploader targetUploader;
    @Spy private List<TargetUploader> uploaders = new ArrayList<>();

    @Mock private AwsS3Repository fileRepository;
    @Mock private TargetUploadReportService targetUploadService;
    @Mock private TargetUploadReportJpaRepository jpaRepository;

    @InjectMocks private TargetUploadFileReader reader;

    private FileTargetUploadRequestedEventBuilder builder;
    private TargetUploadReport targetUploadReport;
    private InputStream is;

    @BeforeEach
    void setUp() {
        uploaders.add(targetUploader);
        builder = FileTargetUploadRequestedEventBuilder.builder()
                .uploadKey("/target_upload/target_upload_test.xlsx");
        targetUploadReport = TargetUploadReportEntityBuilder.builder()
                .uploadStatus(new TargetUploadRequestState())
                .build();
        is = getClass().getResourceAsStream("/target_upload/target_upload_test.xlsx");
    }

    @Nested
    @DisplayName("예외가 발생할 때")
    class WhenThrowException {
        @Test
        @DisplayName("대상자 업로드 리포트가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadReportDoesNotExist() {
            doReturn(Optional.empty()).when(jpaRepository).findById(any());

            FileTargetUploadRequestedEvent event = builder.build();

            assertThatThrownBy(() -> reader.listen(event))
                    .isInstanceOf(TargetUploadReportNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("업로드 상태가 FAIL로 변할 때")
    class WhenUploadStateIsFail {
        @Test
        @DisplayName("지원하는 대상자 업로더가 없으면 업로드 상태를 FAIL로 변경한다.")
        void shouldThrowException_whenTargetUploaderDoesNotSupport() {
            FileTargetUploadRequestedEvent event = builder.build();

            doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
            doReturn(false).when(targetUploader).supports(any());

            reader.listen(event);

            assertThat(targetUploadReport.getState().getCurrentCode()).isEqualTo(TargetUploadStatus.FAIL);
        }

        @Test
        @DisplayName("대상자 업로드 실행 도중 오류 발생 시 대상자 업로드 상태는 FAIL로 변경된다.")
        void shouldChangeToFail_whenTargetUploaderFails() {
            FileTargetUploadRequestedEvent event = builder.build();

            doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
            doReturn(true).when(targetUploader).supports(any());
            doThrow(mock(BusinessException.class)).when(targetUploader).upload(any(), any());

            reader.listen(event);

            assertThat(targetUploadReport.getState().getCurrentCode())
                    .isEqualTo(TargetUploadStatus.FAIL);
        }

        @Test
        @DisplayName("파일 읽기 실패 시 대상자 업로드 상태는 FAIL로 변경된다.")
        void shouldChangeToFail_whenFileReadFails() {
            FileTargetUploadRequestedEvent event = builder.build();

            doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
            doThrow(mock(AwsServiceException.class)).when(fileRepository).getFileContent(anyString());
            doReturn(true).when(targetUploader).supports(any());

            reader.listen(event);

            assertThat(targetUploadReport.getState().getCurrentCode())
                    .isEqualTo(TargetUploadStatus.FAIL);
        }

        @Test
        @DisplayName("지원하는 대상자 업로더가 없으면 업로드 상태를 FAIL로 변경한다.")
        void shouldFailWhenNoTargetUploaderExists() {
            FileTargetUploadRequestedEvent event = builder.build();

            doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
            doReturn(false).when(targetUploader).supports(any());

            reader.listen(event);

            assertThat(targetUploadReport.getState().getCurrentCode())
                    .isEqualTo(TargetUploadStatus.FAIL);
        }

        @Test
        @DisplayName("파일 조회 실패 시 대상자 업로드 상태를 FAIL로 변경된다.")
        void shouldThrowException_when() {
            FileTargetUploadRequestedEvent event = builder.build();
            is = getClass().getResourceAsStream("/target_upload/not_found.xlsx");

            doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
            doReturn(is).when(fileRepository).getFileContent(anyString());
            doReturn(true).when(targetUploader).supports(any());
            doReturn(mock(Consumer.class)).when(targetUploader).upload(any(), any());

            reader.listen(event);

            assertThat(targetUploadReport.getState().getCurrentCode())
                    .isEqualTo(TargetUploadStatus.FAIL);
        }
    }

    @Nested
    @DisplayName("대상자 업로드 정상 실행")
    class WhenTargetUploadFileRead {
        @BeforeEach
        void setUp() {
            doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
            doReturn(true).when(targetUploader).supports(any());
            doReturn(mock(Consumer.class)).when(targetUploader).upload(any(), any());
            doReturn(is).when(fileRepository).getFileContent(anyString());
        }
        @Test
        @DisplayName("대상자 업로드 리포트를 조회한다.")
        void shouldFindTargetUploadReport() {
            FileTargetUploadRequestedEvent event = builder.build();

            reader.listen(event);

            verify(jpaRepository).findById(any());
        }

        @Test
        @DisplayName("대상자 업로드가 정상적으로 완료되면, 대상자 업로드 완료 이벤트를 발행한다.")
        void shouldPublishTargetUploadCompletedEvent() {
            FileTargetUploadRequestedEvent event = builder.build();
            reader.listen(event);

            verify(targetUploadService).reportingAndOnCompleted(any());
        }

        @Test
        @DisplayName("파일을 읽는다.")
        void shouldReadFileContent() {
            FileTargetUploadRequestedEvent event = builder.build();

            reader.listen(event);

            verify(fileRepository).getFileContent(anyString());
        }

        @Test
        @DisplayName("대상자 업로드가 정상적으로 완료되면, 업로드 상태는 FAIL이 아니다.")
        void shouldReturnNotFail() {
            FileTargetUploadRequestedEvent event = builder.build();

            reader.listen(event);

            assertThat(targetUploadReport.getState().getCurrentCode()).isNotEqualTo(TargetUploadStatus.FAIL);
        }
    }
}