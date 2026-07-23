package com.ums.schedule.domain.request.target.upload;

import com.ums.schedule.application.sendrequest.target.command.TargetFileUploadRequestCommand;
import com.ums.schedule.common.code.api.TargetUploadErrorCode;
import com.ums.schedule.common.code.target_upload.TargetUploadType;
import com.ums.schedule.domain.request.target.upload.builder.TargetUploadRequestCommandBuilder;
import com.ums.schedule.common.code.target_upload.TargetUploadEvent;
import com.ums.schedule.common.code.target_upload.TargetUploadFormatEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatus;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.exception.InvalidTargetUploadStateException;
import com.ums.schedule.domain.target.upload.exception.TargetUploadPolicyViolationException;
import com.ums.schedule.domain.target.upload.state.*;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.Disabled;
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
            .uploadType(TargetUploadType.FILE);

    @Nested
    @DisplayName("upload_type이 JSON일 때")
    class WhenUploadTypeIsJson {
        private TargetUploadReportEntityBuilder domain = TargetUploadReportEntityBuilder.builder()
                .uploadType(TargetUploadType.JSON)
                .uploadStatus(new TargetUploadWaitingState());

        @Test
        @DisplayName("익셉션이 발생한다.")
        void shouldThrowException() {
            TargetUploadReport report = domain.build();

            TargetUploadPolicyViolationException expect = TargetUploadPolicyViolationException.of(TargetUploadErrorCode.UNSUPPORTED_UPLOAD_TYPE);
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
        InvalidTargetUploadStateException expect = InvalidTargetUploadStateException.of(TargetUploadStatus.CREATED, TargetUploadStatus.REQUEST);

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

        TargetUploadStatus expect = report.getState().getCurrentCode();

        assertThat(expect).isEqualTo(TargetUploadStatus.REQUEST);
    }

    @Test
    @DisplayName("state가 REQUEST일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsRequest() {
        TargetUploadReport report = domain
                .uploadStatus(new TargetUploadRequestState())
                .build();

        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();

        InvalidTargetUploadStateException expect =
                InvalidTargetUploadStateException.of(TargetUploadStatus.REQUEST, TargetUploadStatus.REQUEST);

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

        InvalidTargetUploadStateException expect =
                InvalidTargetUploadStateException.of(TargetUploadStatus.PARSING, TargetUploadStatus.REQUEST);

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

        InvalidTargetUploadStateException expect =
                InvalidTargetUploadStateException.of(TargetUploadStatus.COMPLETED, TargetUploadStatus.REQUEST);

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

        TargetUploadPolicyViolationException expect = TargetUploadPolicyViolationException.of(TargetUploadErrorCode.UNSUPPORTED_UPLOAD_FORMAT);

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

        TargetUploadPolicyViolationException expect =
                TargetUploadPolicyViolationException.of(TargetUploadErrorCode.TARGET_UPLOAD_LIMIT_EXCEEDED);


        assertThatThrownBy(() -> report.requestFileUpload(command))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @Disabled
    @DisplayName("파일이 존재하지 않으면 익셉션이 발생한다. ")
    void shouldThrowException_whenFileDoesNotExists() {
        TargetUploadReport report = domain.uploadFormat(TargetUploadFormatEnum.CSV).build();
        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder()
                .isExistFile(false)
                .build();


//        assertThatThrownBy(() -> report.requestFileUpload(command))
//                .isInstanceOf(expect.getClass())
//                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("TargetUploadRequestedEvent가 발행된다.")
    void shouldPublishTargetUploadRequestedEvent() {
        TargetUploadReport report = domain.uploadFormat(TargetUploadFormatEnum.CSV).build();
        TargetFileUploadRequestCommand command = TargetUploadRequestCommandBuilder.builder().build();

        report.requestFileUpload(command);

        assertThat(report.getEvent()).isEqualTo(TargetUploadEvent.TARGET_UPLOAD_REQUESTED);
    }
}