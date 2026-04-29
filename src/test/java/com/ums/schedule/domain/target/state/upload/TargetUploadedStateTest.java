package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadUploadStateException;

import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ums.schedule.code.send.TargetUploadStatusEnum.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetUploadedStateTest {
    @Nested
    @DisplayName("TargetUploadStatus가 UPLOAD일 때")
    class whenTargetUploadStatusIsUpload {
        @Test
        @DisplayName("TargetUploadCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadUploadedState();
            TargetUploadCreatedEvent given = TargetUploadCreatedEvent.of(targetUpload, null);
            TargetUploadUploadStateException expect = TargetUploadUploadStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());

        }

        @Test
        @DisplayName("TargetUploadRequestedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadRequestedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadUploadedState();
            TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
            TargetUploadUploadStateException expect = TargetUploadUploadStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadUrlCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadUploadedState();
            TargetUploadUrlCreatedEvent given = TargetUploadUrlCreatedEvent.of(targetUpload);
            TargetUploadUploadStateException expect = TargetUploadUploadStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("MessageCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenMessageCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadUploadedState();
            TargetMessageCreatedEvent given = TargetMessageCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate(), List.of());
            TargetUploadUploadStateException expect = TargetUploadUploadStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUploadedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadUploadedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadUploadedState();
            TargetUploadUploadedEvent given = TargetUploadUploadedEvent.of(targetUpload);
            TargetUploadUploadStateException expect = TargetUploadUploadStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadCompletedEvent가 발행되면 COMPLETE가 반환된다.")
        void shouldReturnTargetUploadStateIsComplete_whenTargetUploadCompleteEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadUploadedState();
            TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
            TargetUploadState expect = status.onEvent(given);

            assertThat(expect.currentStatus()).isEqualTo(COMPLETED);
        }
    }
    @Test
    @DisplayName("onFail메소드 호출 시 업로드 상태는 FAIL을 반환한다.")
    void shouldReturnFail_whenOnFailMethodCalled() {
        TargetUploadState status = new TargetUploadUploadedState();
        TargetUploadState expect = status.onFail();

        assertThat(expect).isInstanceOf(TargetUploadFailState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.FAIL);
    }
}