package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadPendingStateException;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadFailState;
import com.ums.schedule.domain.target.state.upload.TargetUploadPendingState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetPendingStateTest {

    @Nested
    @DisplayName("TargetStatus가 PENDING일 때 ")
    class whenTargetStatusIsPending {
        @Test
        @DisplayName("TargetCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadPendingState();
            TargetUploadPendingStateException exception = TargetUploadPendingStateException.of(TargetUploadStatusEnum.CREATED);

            assertThatThrownBy(() -> status.onEvent(TargetUploadCreatedEvent.of(targetUpload, null)))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadUrlCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadPendingState();
            TargetUploadPendingStateException exception = TargetUploadPendingStateException.of(TargetUploadStatusEnum.PENDING);

            assertThatThrownBy(() -> status.onEvent(TargetUploadUrlCreatedEvent.of(targetUpload)))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }

        @Test
        @DisplayName("TargetUploadRequestedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadRequestedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadPendingState();
            TargetUploadPendingStateException exception = TargetUploadPendingStateException.of(TargetUploadStatusEnum.PENDING);

            assertThatThrownBy(() -> status.onEvent(TargetUploadCreatedEvent.of(targetUpload, null)))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }

        @Test
        @DisplayName("MessageCreatedEvent가 발행되면 PARSING이 반환된다.")
        void shouldReturnTargetUploadStatusIsParsing_whenMessageCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadPendingState();
            TargetUploadPendingStateException exception = TargetUploadPendingStateException.of(TargetUploadStatusEnum.PARSING);

            assertThatThrownBy(() -> status.onEvent(TargetMessageCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate(), List.of())))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }

        @Test
        @DisplayName("TargetUploadedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadPendingState();
            TargetUploadPendingStateException exception = TargetUploadPendingStateException.of(TargetUploadStatusEnum.UPLOAD);

            assertThatThrownBy(() -> status.onEvent(TargetUploadUploadedEvent.of(targetUpload)))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }

        @Test
        @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadedCompletedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadPendingState();
            TargetUploadPendingStateException exception = TargetUploadPendingStateException.of(TargetUploadStatusEnum.COMPLETED);

            assertThatThrownBy(() -> status.onEvent(TargetUploadCompletedEvent.of(targetUpload)))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }
    }
    @Test
    @DisplayName("onFail메소드 호출 시 업로드 상태는 FAIL을 반환한다.")
    void shouldReturnFail_whenOnFailMethodCalled() {
        TargetUploadState status = new TargetUploadPendingState();
        TargetUploadState expect = status.onFail();

        assertThat(expect).isInstanceOf(TargetUploadFailState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.FAIL);
    }
}