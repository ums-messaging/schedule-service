package com.ums.schedule.domain.sendrequest.target.upload;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.common.code.target_upload.TargetUploadEventEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.common.code.target_upload.TargetUploadTypeEnum;
import com.ums.schedule.domain.exception.target_upload.InvalidTargetUploadReportStateException;
import com.ums.schedule.domain.sendrequest.target.upload.state.*;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TargetUploadReportPrepareTest {


    @Nested
    @DisplayName("upload_type이 FILE일 때")
    class WhenUploadTypeIsFile {
        private final TargetUploadReportEntityBuilder domain = TargetUploadReportEntityBuilder.builder()
                .uploadType(TargetUploadTypeEnum.FILE)
                .sendRequest(SendRequestEntityBuilder.builder().build());
        private final SendRequest sendRequest = SendRequestEntityBuilder.builder().build();

        @Test
        @DisplayName("state가 CREATE일 때 state는 WAITING으로 변경된다.")
        void shouldChangeStateToWaiting_whenStateIsCreate() {
            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadCreateState()).build();

            targetUpload.prepareTargetUpload(sendRequest);

            TargetUploadStatusEnum expect = targetUpload.getState().getCurrentCode();

            assertThat(expect).isEqualTo(TargetUploadStatusEnum.WAITING);
        }

        @Test
        @DisplayName("state가 WAITING일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsWaiting() {
            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadWaitingState())
                    .build();

            InvalidTargetUploadReportStateException expect = InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.WAITING, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
        @Test
        @DisplayName("state가 REQUEST 일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsRequest() {
            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadRequestState())
                    .build();

            InvalidTargetUploadReportStateException expect =
                    InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.REQUEST, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
        @Test
        @DisplayName("state가 PARSING일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsParsing() {
            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadParsingState())
                    .build();

            InvalidTargetUploadReportStateException expect =
                    InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.PARSING, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("state가 COMPLETE일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsComplete(){
            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadCompleteState())
                    .build();

            InvalidTargetUploadReportStateException expect =
                    InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.COMPLETED, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TARGET_UPLOAD_READY 이벤트가 발행된다.")
        void shouldPublishTargetUploadReadyEvent() {
            SendRequest sendRequest = SendRequestEntityBuilder.builder().build();
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder()
                    .uploadType(TargetUploadTypeEnum.FILE)
                    .sendRequest(sendRequest)
                    .uploadStatus(new TargetUploadCreateState())
                    .build();

            targetUpload.prepareTargetUpload(sendRequest);

            TargetUploadEventEnum expect = targetUpload.getEvent();
            assertThat(expect).isEqualTo(TargetUploadEventEnum.TARGET_UPLOAD_READY);
        }

        @Test
        @DisplayName("SendRequest의 currentTargetUpload가 지정된다.")
        void shouldAssignedCurrentTargetUpload() {
            SendRequest givenSendRequest = SendRequestEntityBuilder.builder()
                    .currentTargetUpload(null)
                    .build();

            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadCreateState())
                    .sendRequest(givenSendRequest)
                    .build();

            SendRequest sendRequest = targetUpload.getSendRequest();
            TargetUploadReport beforeAssigned = sendRequest.getCurrentTargetUpload();

            targetUpload.prepareTargetUpload(sendRequest);
            TargetUploadReport afterAssigned = sendRequest.getCurrentTargetUpload();

            assertThat(beforeAssigned).isNull();
            assertThat(afterAssigned).isEqualTo(targetUpload);
        }
    }

    @Nested
    @DisplayName("upload_type이 JSON일 때")
    class WhenUploadTypeIsJson {
        private final TargetUploadReportEntityBuilder domain = TargetUploadReportEntityBuilder.builder()
                .sendRequest(SendRequestEntityBuilder.builder().build())
                .uploadType(TargetUploadTypeEnum.JSON);

        private final SendRequest sendRequest = SendRequestEntityBuilder.builder().build();

        @Test
        @DisplayName("state가 WAITING일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsWaiting() {
            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadWaitingState())
                    .build();

            InvalidTargetUploadReportStateException expect = InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.WAITING, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
        @Test
        @DisplayName("state가 CREATE일 때 state는 REQUEST로 변경된다.")
        void shouldChangeStateToRequest_whenUploadTypeIsJson() {
            TargetUploadReport targetUpload = domain.uploadStatus(new TargetUploadCreateState())
                    .build();

            targetUpload.prepareTargetUpload(sendRequest);
            TargetUploadStatusEnum expect = targetUpload.getState().getCurrentCode();

            assertThat(expect).isEqualTo(TargetUploadStatusEnum.REQUEST);
        }

        @Test
        @DisplayName("state가 PARSING일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsParsing() {
            TargetUploadReport targetUpload = domain.uploadStatus(new TargetUploadParsingState())
                    .build();

            InvalidTargetUploadReportStateException expect =
                    InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.PARSING, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
        @Test
        @DisplayName("state가 REQUEST 일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsRequest() {
            TargetUploadReport targetUpload = domain.uploadStatus(new TargetUploadRequestState())
                    .build();

            InvalidTargetUploadReportStateException expect =
                    InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.REQUEST, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("state가 COMPLETE일 때 익셉션이 발생한다.")
        void shouldThrowException_whenStateIsComplete(){
            TargetUploadReport targetUpload = domain.uploadStatus(new TargetUploadCompleteState())
                    .build();

            InvalidTargetUploadReportStateException expect =
                    InvalidTargetUploadReportStateException.of(TargetUploadStatusEnum.COMPLETED, TargetUploadEventEnum.TARGET_UPLOAD_READY);

            assertThatThrownBy(() -> targetUpload.prepareTargetUpload(sendRequest))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
        @Test
        @DisplayName("TARGET_UPLOAD_REQUESTED 이벤트가 발행된다.")
        void shouldPublishTargetUploadRequestedEvent_whenUploadTypeIsJson() {
            TargetUploadReport targetUpload = domain.uploadStatus(new TargetUploadCreateState())
                    .build();

            targetUpload.prepareTargetUpload(sendRequest);

            TargetUploadEventEnum expect = targetUpload.getEvent();
            assertThat(expect).isEqualTo(TargetUploadEventEnum.TARGET_UPLOAD_REQUESTED);
        }
        @Test
        @DisplayName("SendRequest의 currentTargetUpload가 지정된다.")
        void shouldAssignedCurrentTargetUpload() {
            SendRequest givenSendRequest = SendRequestEntityBuilder.builder()
                    .currentTargetUpload(null)
                    .build();

            TargetUploadReport targetUpload = domain
                    .uploadStatus(new TargetUploadCreateState())
                    .sendRequest(givenSendRequest)
                    .build();

            SendRequest sendRequest = targetUpload.getSendRequest();
            TargetUploadReport beforeAssigned = sendRequest.getCurrentTargetUpload();

            targetUpload.prepareTargetUpload(sendRequest);
            TargetUploadReport afterAssigned = sendRequest.getCurrentTargetUpload();

            assertThat(beforeAssigned).isNull();
            assertThat(afterAssigned).isEqualTo(targetUpload);
        }
    }
}
