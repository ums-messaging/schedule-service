package com.ums.schedule.domain.target.state.upload;

import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadCreateStateException;
import com.ums.schedule.domain.target.state.upload.TargetUploadCreateState;
import com.ums.schedule.domain.target.state.upload.TargetUploadFailState;
import com.ums.schedule.domain.target.state.upload.TargetUploadState;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetCreateStateTest {

    @Nested
    @DisplayName("TargetUpload의 상태가 CREATE 일 때")
    class whenTargetUploadStatusIsCreate {

        @Test
        @DisplayName("TargetUploadCreatedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_whenTargetUploadCreatedEventPublished() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadCreateState state = new TargetUploadCreateState();
            TargetUploadCreatedEvent given = TargetUploadCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate());
            TargetUploadCreateStateException expect = TargetUploadCreateStateException.of(TargetUploadStatusEnum.CREATED);

            assertThatThrownBy(() -> state.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadUrlCreatedEvent가 발행되면, TargetUpload의 상태는 PENDING이 반환된다.")
        void shouldReturnTargetUploadStateIsPending_whenTargetUploadUrlCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            // when
            TargetUploadState status = new TargetUploadCreateState();
            TargetUploadUrlCreatedEvent given = TargetUploadUrlCreatedEvent.of(targetUpload);
            TargetUploadState result = status.onEvent(given);

            // then
            assertThat(result.currentStatus()).isEqualTo(TargetUploadStatusEnum.PENDING);
        }

        @Test
        @DisplayName("TargetUploadRequestedEvent가 발행되면, 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadRequestedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadCreateState();

            TargetUploadCreateStateException expect =
                    TargetUploadCreateStateException.of(TargetUploadStatusEnum.REQUEST);
            TargetUploadRequestedEvent given = TargetUploadRequestedEvent.of(targetUpload);

            // when
            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("MessageCreatedEvent가 발행되면 예외가 발생한다.")
        void shouldThrowException_whenMessageCreatedEventCreate() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadState status = new TargetUploadCreateState();
            TargetUploadCreateStateException exception = TargetUploadCreateStateException.of(TargetUploadStatusEnum.PARSING);
            TargetMessageCreatedEvent given = TargetMessageCreatedEvent.of(targetUpload, TargetUploadDomainFixture.createTemplate(), List.of());

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(exception.getClass())
                    .hasMessage(exception.getMessage());
        }

        @Test
        @DisplayName("TargetUploadedEvent가 발행되면 예외가 발생한다.")
        void shouldThrowException_whenTargetUploadedEventCreate(){
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadUploadedEvent given = TargetUploadUploadedEvent.of(targetUpload);
            TargetUploadState status = new TargetUploadCreateState();
            TargetUploadCreateStateException expect = TargetUploadCreateStateException.of(TargetUploadStatusEnum.UPLOAD);

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }

        @Test
        @DisplayName("TargetUploadCompletedEvent가 발행되면 익셉션이 발생한다.")
        void shouldThrowException_TargetUploadCompletedEvent() {
            TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

            TargetUploadCompletedEvent given = TargetUploadCompletedEvent.of(targetUpload);
            TargetUploadState status = new TargetUploadCreateState();
            TargetUploadCreateStateException expect = TargetUploadCreateStateException.of(TargetUploadStatusEnum.COMPLETED);

            assertThatThrownBy(() -> status.onEvent(given))
                    .isInstanceOf(expect.getClass())
                    .hasMessage(expect.getMessage());
        }
    }

    @Test
    @DisplayName("onFail메소드 호출 시 업로드 상태는 FAIL을 반환한다.")
    void shouldReturnFail_whenOnFailMethodCalled() {
        TargetUploadState status = new TargetUploadCreateState();
        TargetUploadState expect = status.onFail();

        assertThat(expect).isInstanceOf(TargetUploadFailState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.FAIL);
    }

}