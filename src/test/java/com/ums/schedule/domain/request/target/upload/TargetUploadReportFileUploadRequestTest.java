package com.ums.schedule.domain.request.target.upload;

import com.ums.schedule.application.sendrequest.target.command.TargetFileUploadRequestCommand;
import com.ums.schedule.domain.exception.validation.FileNotFoundException;
import com.ums.schedule.domain.exception.validation.FileSizeExceededException;
import com.ums.schedule.domain.request.target.upload.builder.TargetUploadRequestCommandBuilder;
import com.ums.schedule.common.code.target_upload.TargetUploadEventEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import com.ums.schedule.domain.exception.target_upload.InvalidTargetUploadReportStateException;
import com.ums.schedule.domain.exception.target_upload.TargetUploadFileFormatMismatchException;
import com.ums.schedule.domain.exception.target_upload.UnSupportedTargetUploadTypeException;
import com.ums.schedule.domain.request.target.upload.state.*;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class TargetUploadReportFileUploadRequestTest {
    private TargetUploadReportEntityBuilder domain = TargetUploadReportEntityBuilder.builder()
            .uploadStatus(new TargetUploadWaitingState())
            .uploadType(TargetUploadTypeEnum.FILE);

    @Nested
    @DisplayName("upload_type이 JSON일 때")
    class WhenUploadTypeIsJson {
        private TargetUploadReportEntityBuilder domain = TargetUploadReportEntityBuilder.builder()
                .uploadType(TargetUploadTypeEnum.JSON)
                .uploadStatus(new TargetUploadWaitingState());

        @Test
        @DisplayName("익셉션이 발생한다.")
        void shouldThrowException() {
            TargetUploadReport report = domain.build();

            UnSupportedTargetUploadTypeException expect = UnSupportedTargetUploadTypeException.of();
            TargetFileUploadRequestCommand command = mock(TargetFileUploadRequestCommand.class);

            assertThatThrownBy(() -> report.requestFileUpload(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Test
    @DisplayName("state가 CREATE일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsCreate() {
        TargetUploadReport report = domain
                .uploadStatus(new TargetUploadCreateState())
                .build();

        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();
        InvalidTargetUploadReportStateException expect = InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.CREATED, TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);

        assertThatThrownBy(() -> report.requestFileUpload(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("state가 WAITING일 때 REQUEST로 변경된다.")
    void shouldChangeStateToRequest_whenStateIsHolding() {
        TargetUploadReport report = domain
                .uploadStatus(new TargetUploadWaitingState())
                .build();

        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();
        report.requestFileUpload(command);

        TargetUploadStatusEnum expect = report.getState().getCurrentCode();

        assertThat(expect).isEqualTo(TargetUploadStatusEnum.REQUEST);
    }

    @Test
    @DisplayName("state가 REQUEST일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsRequest() {
        TargetUploadReport report = domain
                .uploadStatus(new TargetUploadRequestState())
                .build();

        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();

        InvalidTargetUploadReportStateException expect =
                InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.REQUEST, TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);

        assertThatThrownBy(() -> report.requestFileUpload(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("state가 PARSING일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsParsing() {
        TargetUploadReport report = domain
                .uploadStatus(new TargetUploadParsingState())
                .build();

        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();

        InvalidTargetUploadReportStateException expect =
                InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.PARSING, TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);

        assertThatThrownBy(() -> report.requestFileUpload(command))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("state가 COMPLETE일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsComplete() {
        TargetUploadReport report = domain
                .uploadStatus(new TargetUploadCompleteState())
                .build();

        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();

        InvalidTargetUploadReportStateException expect =
                InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.COMPLETED, TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);

        assertThatThrownBy(() -> report.requestFileUpload(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }


    @Test
    @DisplayName("파일이 정해진 확장자가 아니면 익셉션이 발생한다.")
    void shouldThrowException_whenFileExtIsNotExcel() {
        TargetUploadReport report = domain.uploadFormat(TargetUploadFormatEnum.CSV).build();
        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder()
                .fileName("%s.%s".formatted(UUID.randomUUID().toString(), "xlsx"))
                .build();

        TargetUploadFileFormatMismatchException expect = TargetUploadFileFormatMismatchException.of(TargetUploadFormatEnum.CSV);

        assertThatThrownBy(() -> report.requestFileUpload(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("파일 사이즈를 초과하면 익셉션이 발생한다.")
    void shouldThrowException_whenFileSizeExceed() {
        TargetUploadReport report = domain.uploadFormat(TargetUploadFormatEnum.CSV).build();
        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder()
                .fileSize(10L)
                .fileMaxSize(5L)
                .build();

        FileSizeExceededException expect = FileSizeExceededException.of(command.maxFileSize(), command.fileSize());

        assertThatThrownBy(() -> report.requestFileUpload(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("파일이 존재하지 않으면 익셉션이 발생한다. ")
    void shouldThrowException_whenFileDoesNotExists() {
        TargetUploadReport report = domain.uploadFormat(TargetUploadFormatEnum.CSV).build();
        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder()
                .isExistFile(false)
                .build();

        FileNotFoundException expect = FileNotFoundException.of(report.getUploadKey());

        assertThatThrownBy(() -> report.requestFileUpload(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행된다.")
    void shouldPublishTargetUploadRequestedEvent() {
        TargetUploadReport report = domain.uploadFormat(TargetUploadFormatEnum.CSV).build();
        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();

        report.requestFileUpload(command);

        assertThat(report.getEvent()).isEqualTo(TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);
    }
}