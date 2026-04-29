package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadFailStateException;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetUploadFailStateTest {
    @Nested
    @DisplayName("TargetUpload 상태가 Fail일 때")
    class whenTargetUploadStateIsFail {
        @Test
        @DisplayName("TargetUploadCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadFailState();
            TargetUploadCreatedEvent given = TargetUploadCreatedEvent.of(targetUpload, null);
            TargetUploadFailStateException expect = TargetUploadFailStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadRequestedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadRequestedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadFailState();
            TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);
            TargetUploadFailStateException expect = TargetUploadFailStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadUrlCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadFailState();
            TargetUploadUrlCreatedEvent given = TargetUploadUrlCreatedEvent.of(targetUpload);
            TargetUploadFailStateException expect = TargetUploadFailStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("MessageCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenMessageCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadFailState();
            TargetMessageCreatedEvent given = TargetMessageCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate(), List.of());
            TargetUploadFailStateException expect = TargetUploadFailStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUploadedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadUploadedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadFailState();
            TargetUploadUploadedEvent given = TargetUploadUploadedEvent.of(targetUpload);
            TargetUploadFailStateException expect = TargetUploadFailStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadCompleteEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadFailState();
            TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
            TargetUploadFailStateException expect = TargetUploadFailStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Test
    @DisplayName("onFail메소드 호출 시 익셉션이 발생한다.")
    void shouldThrowException_whenOnFailMethodCalled() {
        TargetUploadState status = new TargetUploadFailState();

        TargetUploadFailStateException expect = TargetUploadFailStateException.of(TargetUploadStatusEnum.FAIL);

        assertThatThrownBy(() -> status.onFail())
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }
}