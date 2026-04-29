package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadParsingStateException;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadFailState;
import com.ums.schedule.domain.target.state.upload.TargetUploadParsingState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ums.schedule.code.send.TargetUploadStatusEnum.UPLOAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetParsingStateTest {

    @Nested
    @DisplayName("TargetUploadStatus가 PARSING일 때")
    class whenTargetUploadStatusIsParsing {
        @Test
        @DisplayName("TargetCreatedEvent 발행 시 예외가 발생한다.")
        void shouldThrowException_whenTargetCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadCreatedEvent given = TargetUploadCreatedEvent.of(targetUpload, null);
            TargetUploadParsingStateException expect = TargetUploadParsingStateException.of(given.getToStatus().currentStatus());
            TargetUploadState status = new TargetUploadParsingState();

            // when
            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUrlCreatedEvent 발행 시 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadUrlCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadUrlCreatedEvent given = TargetUploadUrlCreatedEvent.of(targetUpload);
            TargetUploadState status = new TargetUploadParsingState();

            // when
            TargetUploadParsingStateException expect = TargetUploadParsingStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadRequestedEvent 발행 시 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadRequestedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadParsingState();
            TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);

            TargetUploadParsingStateException expect =
                    TargetUploadParsingStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("MessageCreatedEvent 발행 시 예외가 발생한다.")
        void shouldThrowException_whenMessageCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            // when
            TargetUploadState status = new TargetUploadParsingState();
            TargetMessageCreatedEvent given = TargetMessageCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate(), List.of());

            TargetUploadParsingStateException expect = TargetUploadParsingStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUploadedEvent 발행 시 UPLOAD가 반환된다.")
        void shouldThrowException_whenTargetUploadUploadedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            // when
            TargetUploadState status = new TargetUploadParsingState();
            TargetUploadUploadedEvent given = TargetUploadUploadedEvent.of(targetUpload);
            TargetUploadState result = status.onEvent(given);

            assertThat(result.currentStatus()).isEqualTo(UPLOAD);
        }

        @Test
        @DisplayName("TargetUploadCompletedEvent 발행 시 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadCompletedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            // when
            TargetUploadState status = new TargetUploadParsingState();
            TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
            TargetUploadParsingStateException expect = TargetUploadParsingStateException.of(given.getToStatus().currentStatus());

            assertThatThrownBy(() -> status.onEvent(given))
                        .isInstanceOf(expect.getClass())
                        .hasMessage(expect.getMessage());
        }
    }
    @Test
    @DisplayName("onFail메소드 호출 시 업로드 상태는 FAIL을 반환한다.")
    void shouldReturnFail_whenOnFailMethodCalled() {
        TargetUploadState status = new TargetUploadParsingState();
        TargetUploadState expect = status.onFail();

        assertThat(expect).isInstanceOf(TargetUploadFailState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.FAIL);
    }
}