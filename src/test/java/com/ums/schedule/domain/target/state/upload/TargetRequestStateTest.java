package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadRequestStateException;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadFailState;
import com.ums.schedule.domain.target.state.upload.TargetUploadRequestState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ums.schedule.code.send.TargetUploadStatusEnum.PARSING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetRequestStateTest {

    @Nested
    @DisplayName("TargetUpload 상태가 REQUEST 일 때")
    class whenTargetUploadStatusIsRequest {
        @Test
        @DisplayName("TargetUploadCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadRequestState();
            TargetUploadRequestStateException expect = TargetUploadRequestStateException.of(TargetUploadStatusEnum.PENDING);

            assertThatThrownBy(() -> status.onEvent(TargetUploadUrlCreatedEvent.of(targetUpload)))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadUrlCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadRequestState();
            TargetUploadRequestStateException expect = TargetUploadRequestStateException.of(TargetUploadStatusEnum.PENDING);

            assertThatThrownBy(() -> status.onEvent(TargetUploadUrlCreatedEvent.of(targetUpload)))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadRequestedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadRequestedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadRequestState();
            TargetUploadRequestStateException expect = TargetUploadRequestStateException.of(TargetUploadStatusEnum.REQUEST);

            assertThatThrownBy(() -> status.onEvent(TargetUploadRequestedEvent.of(targetUpload)))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("MessageCreatedEvent가 발행되면 PARSING이 반환된다.")
        void shouldReturnTargetUploadStatusIsParsing_whenMessageCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadRequestState();
            TargetUploadState result = status.onEvent(TargetMessageCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate(), List.of()));

            assertThat(result.currentStatus()).isEqualTo(PARSING);
        }

        @Test
        @DisplayName("TargetUploadedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadRequestState();
            TargetUploadRequestStateException expect = TargetUploadRequestStateException.of(TargetUploadStatusEnum.UPLOAD);

            assertThatThrownBy(() -> status.onEvent(TargetUploadUploadedEvent.of(targetUpload)))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발행된다.")
        void shouldThrowException_whenTargetUploadCompletedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadRequestState();
            TargetUploadRequestStateException expect = TargetUploadRequestStateException.of(TargetUploadStatusEnum.COMPLETED);

            assertThatThrownBy(() -> status.onEvent(TargetUploadCompletedEvent.of(targetUpload)))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }
    @Test
    @DisplayName("onFail메소드 호출 시 업로드 상태는 FAIL을 반환한다.")
    void shouldReturnFail_whenOnFailMethodCalled() {
        TargetUploadState status = new TargetUploadRequestState();
        TargetUploadState expect = status.onFail();

        assertThat(expect).isInstanceOf(TargetUploadFailState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.FAIL);
    }
}