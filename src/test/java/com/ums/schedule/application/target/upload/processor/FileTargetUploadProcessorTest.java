package com.ums.schedule.application.target.upload.processor;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.application.target.TargetUploadRequestService;
import com.ums.schedule.application.target.exception.TargetUploadProcessException;
import com.ums.schedule.application.target.exception.TargetUploadReportNotConfiguredException;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.processor.FileTargetUploadProcessor;
import com.ums.schedule.application.target.reader.model.FileTargetUploadRequestedEvent;
import com.ums.schedule.application.target.report.model.TargetUploadRequestResult;
import com.ums.schedule.common.code.api.CommonErrorCode;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.code.target_upload.TargetUploadConfiguration;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.common.exception.file.FileNotFoundException;
import com.ums.schedule.config.properties.TargetUploadProperties;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import com.ums.schedule.domain.target.upload.state.TargetUploadWaitingState;
import com.ums.schedule.fixture.file.AwsS3FileMetadataResponseBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileTargetUploadProcessorTest {
    @Mock private AwsS3Repository fileRepository;
    @Mock private TargetUploadReportJpaRepository jpaRepository;
    @Mock private TargetUploadRequestService uploadRequestService;
    @Mock private TargetUploadProperties properties;
    @Mock private ApplicationEventPublisher publisher;

    @InjectMocks private FileTargetUploadProcessor processor;

    private AwsS3FileMetadataResponseBuilder responseBuilder;
    private TargetUploadReport targetUploadReport;

    private String id;

    @BeforeEach
    void setUp() {
        this.id = givenId();
        this.responseBuilder = AwsS3FileMetadataResponseBuilder.builder();
        this.targetUploadReport = TargetUploadReportEntityBuilder.builder()
                .uploadType(TargetUploadType.FILE)
                .uploadStatus(new TargetUploadWaitingState())
                .build();
    }

    private String givenId() {
        UUID uuid = UuidCreator.getTimeOrdered();
        return uuid.toString();
    }

    @Nested
    @DisplayName("대상자 업로드 리포트가 존재할 때")
    class WhenTargetUploadReportExists {

        @BeforeEach
        void setUp() {
            givenFindTargetUploadReport();
            givenConfiguration();
            givenFileMetadata();
            givenTargetUploadRequest();
        }

        @Test
        @DisplayName("대상자 업로드 리포트를 조회한다.")
        void shouldFindTargetUploadReport() {
            processor.request(id);
            verify(jpaRepository).findById(any());
        }

        @Test
        @DisplayName("대상자 업로드 파일 메타 정보를 조회한다.")
        void shouldQueryFileMetadata() {
            processor.request(id);
            verify(fileRepository).getFileMetadata(anyString());
        }
    }

    @Nested
    @DisplayName("업로드 파일이 존재할 때")
    class WhenUploadFileExists {

        @BeforeEach
        void setUp() {
            givenFindTargetUploadReport();
            givenConfiguration();
            givenFileMetadata();
            givenTargetUploadRequest();
        }

        @Test
        @DisplayName("대상자 업로드 요청을 한다.")
        void shouldRequestTargetUpload() {
            processor.request(id);

            verify(uploadRequestService)
                    .request(any(TargetUploadReport.class));
        }

        @Test
        @DisplayName("파일 업로드 요청 이벤트를 발행한다.")
        void shouldPublishTargetUploadRequestedEvent() {
            processor.request(id);
            verify(publisher).publishEvent(any(FileTargetUploadRequestedEvent.class));
        }
    }

    @Nested
    @DisplayName("설정 파일 조회")
    class WhenGetConfiguration {
        @BeforeEach
        void setUp() {
            givenFindTargetUploadReport();
        }

        @Test
        @DisplayName("설정 파일에서 조회한 파일 제한 크기가 0이면, 예외가 발생한다.")
        void shouldThrowException_whenConfiguredFileLimitSizeIsEmpty() {
            doReturn(100).when(properties).getBatchSize();
            doReturn(0L).when(properties).getFileLimitSize();
            givenFileMetadata();

            assertThatThrownBy(() -> processor.request(id))
                    .isInstanceOf(TargetUploadReportNotConfiguredException.class);
        }

        @Test
        @DisplayName("설정 파일에서 조회한 배치 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenConfiguredBatchSizeIsEmpty() {
            doReturn(10000L).when(properties).getFileLimitSize();
            doReturn(0).when(properties).getBatchSize();
            doReturn(100).when(properties).getPartitionSize();

            givenFileMetadata();

            assertThatThrownBy(() -> processor.request(id))
                    .isInstanceOf(TargetUploadReportNotConfiguredException.class)
                    .extracting(v -> ((TargetUploadReportNotConfiguredException) v))
                    .extracting(v ->
                            assertThat(v.getArgs())
                                    .contains(TargetUploadConfiguration.FILE_BATCH_SIZE.description())
                    )
            ;
        }

        @Test
        @DisplayName("파일 크기가 파일 업로드 크기 제한보다 크면 예외가 발생한다.")
        void shouldThrowException_whenFileSizeRatherThanFileLimitSize() {
            AwsS3FileMetadataResponse response = responseBuilder.contentLength(20000L).build();
            doReturn(response).when(fileRepository).getFileMetadata(anyString());
            doReturn(10000L).when(properties).getFileLimitSize();
            doReturn(100).when(properties).getBatchSize();
            doReturn(10).when(properties).getPartitionSize();

            assertThatThrownBy(() -> processor.request(id))
                    .isInstanceOf(TargetUploadProcessException.class);
        }

        @Test
        @DisplayName("파티션 크기가 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenPartitionSizeDoesNotExist() {
            AwsS3FileMetadataResponse response = responseBuilder.contentLength(20000L).build();
            doReturn(response).when(fileRepository).getFileMetadata(anyString());
            doReturn(100000L).when(properties).getFileLimitSize();
            doReturn(100).when(properties).getBatchSize();
            doReturn(0).when(properties).getPartitionSize();

            assertThatThrownBy(() -> processor.request(id))
                    .isInstanceOf(TargetUploadReportNotConfiguredException.class)
                    .extracting(v -> ((TargetUploadReportNotConfiguredException) v))
                    .extracting(v ->
                            assertThat(v.getArgs())
                                    .contains(TargetUploadConfiguration.PARTITION_SIZE.description())
                    )
            ;
        }
    }

    @Nested
    @DisplayName("대상자 업로드 요청 실패")
    class WhenTargetUploadRequestFails {
        @Test
        @DisplayName("대상자 업로드 리포트 조회 실패 시 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadReportFails() {
            doReturn(Optional.empty()).when(jpaRepository).findById(any());

            assertThatThrownBy(() -> processor.request(id))
                    .isInstanceOf(TargetUploadReportNotFoundException.class);
        }

        @Test
        @DisplayName("파일이 존재하지 않으면 예외가 발생한다.")
        void shouldThrowException_whenFileDoesNotExist() {
            givenFindTargetUploadReport();
            givenConfiguration();
            doThrow(mock(FileNotFoundException.class))
                    .when(fileRepository).getFileMetadata(anyString());

            assertThatThrownBy(() -> processor.request(id))
                    .isInstanceOf(TargetUploadProcessException.class);
        }
    }

    private void givenTargetUploadRequest() {
        doReturn(mock(TargetUploadRequestResult.class))
                .when(uploadRequestService).request(any());
    }

    private void givenFindTargetUploadReport() {
        doReturn(Optional.ofNullable(targetUploadReport)).when(jpaRepository).findById(any());
    }

    private void givenFileMetadata() {
        AwsS3FileMetadataResponse response = AwsS3FileMetadataResponseBuilder
                .builder()
                .build();
        doReturn(response).when(fileRepository).getFileMetadata(anyString());
    }

    private void givenConfiguration() {
        doReturn(1000).when(properties).getBatchSize();
        doReturn(3000L).when(properties).getFileLimitSize();
        doReturn(100).when(properties).getPartitionSize();
    }
}