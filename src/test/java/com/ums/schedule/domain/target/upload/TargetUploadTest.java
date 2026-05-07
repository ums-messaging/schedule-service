package com.ums.schedule.domain.target.upload;

import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendEvent;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.event.*;
import com.ums.schedule.domain.target.exeption.TargetMessageCreatedEventException;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadObjectKeyRequiredException;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadCompleteStateException;
import com.ums.schedule.domain.target.exeption.upload.TargetUploadRequestStateException;
import com.ums.schedule.domain.target.state.upload.*;
import com.ums.schedule.fixture.FakeTemplate;
import com.ums.schedule.fixture.SendTargetDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TargetUploadTest {
    @Test
    @DisplayName("생성 시 업로드 상태는 CREATE다")
    void shouldReturnCreateStatus_whenCreated() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        TargetUploadState expect = targetUpload.getUploadStatus();

        assertThat(expect).isInstanceOf(TargetUploadCreateState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.CREATED);
    }

    @Test
    @DisplayName("ObjectKey 설정 시 업로드 상태는 PENDING이다")
    void shouldReturnPendingStatus_whenObjectKeyApplied() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        targetUpload.createTargetUploadUrlEvent("Object Key");
        TargetUploadState expect = targetUpload.getUploadStatus();

        assertThat(expect).isInstanceOf(TargetUploadPendingState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.PENDING);
    }

    @Test
    @DisplayName("ObjectKey 설정 시 TargetUploadUrlCreatedEvent가 발행된다")
    void shouldPublishUrlCreatedEvent_whenObjectKeyApplied() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        TargetUploadEvent expect = targetUpload.createTargetUploadUrlEvent("Object Key");

        assertThat(expect).isInstanceOf(TargetUploadUrlCreatedEvent.class);
    }

    @Test
    @DisplayName("업로드 요청 시 상태는 REQUEST다")
    void shouldReturnRequestStatus_whenRequested() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("objectKey");
        targetUpload.requestTargetUpload();

        TargetUploadState expect = targetUpload.getUploadStatus();

        assertThat(expect).isInstanceOf(TargetUploadRequestState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.REQUEST);
    }

    @Test
    @DisplayName("업로드 요청 시 TargetUploadRequestedEvent가 발행된다")
    void shouldPublishRequestedEvent_whenRequested() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("objectKey");

        SendRequestEvent requestEvent = targetUpload.requestTargetUpload();
        SendEvent expect = requestEvent.getEvent();

        assertThat(expect).isInstanceOf(TargetUploadRequestedEvent.class);
    }

    @Test
    @DisplayName("발송 요청 업로드 대상자 재지정 시 TargetUploadRequestedEvent가 발행된다.")
    void shouldPublishTargetUploadRequestedEvent_whenReAssignedTargetUpload() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("objectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());
        targetUpload.addTargetList(SendTargetDomainFixture.createSendTarget());
        targetUpload.uploadComplete(1);

        SendRequestEvent requestEvent = targetUpload.assignTargetUpload();
        SendEvent expect = requestEvent.getEvent();

        assertThat(expect).isInstanceOf(TargetUploadRequestedEvent.class);
    }

    @Test
    @DisplayName("발송 요청 업로드 대상자 재지정 시 업로드 상태가 COMPLETED가 아니면 익셉션이 발생한다.")
    void shouldThrowException_whenReAssignedToTargetUpload() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();

        TargetUploadCompleteStateException expect = TargetUploadCompleteStateException.targetUploadUnComplete();

        assertThatThrownBy(() -> targetUpload.assignTargetUpload())
                .isInstanceOf(TargetUploadCompleteStateException.class)
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("UploadType이 FILE이고 ObjectKey가 없으면 예외가 발생한다")
    void shouldThrowException_whenFileTypeWithoutObjectKey() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload(TargetUploadTypeEnum.FILE);

        TargetUploadObjectKeyRequiredException expect = TargetUploadObjectKeyRequiredException.of();

        assertThatThrownBy(() -> targetUpload.createTargetUploadUrlEvent(" "))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("메시지 파싱 시 업로드 상태는 PARSING으로 변경된다.")
    void shouldReturnParsingStatus_whenMessageParse() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());

        TargetUploadState expect = targetUpload.getUploadStatus();

        assertThat(expect).isInstanceOf(TargetUploadParsingState.class);
        assertThat(expect.currentStatus()).isEqualTo(expect.currentStatus());
    }

    @Test
    @DisplayName("메시지 파싱 시 MessageCreatedEvent가 발행된다.")
    void shouldPublishMessageCreatedEvent_whenMessageParse() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();

        TargetUploadEvent expect = targetUpload.parseMessage(new FakeTemplate(), List.of());

        assertThat(expect).isInstanceOf(TargetMessageCreatedEvent.class);
    }

    @Test
    @DisplayName("메시지 파싱 시 템플릿이 NULL이면 익셉션이 발생한다.")
    void shouldThrowException_whenTemplateIsNull() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();

        TargetMessageCreatedEventException expect = TargetMessageCreatedEventException.ofTemplate();

        assertThatThrownBy(() -> targetUpload.parseMessage(null, List.of()))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("대상자 추가 시 업로드 상태는 UPLOAD로 변경된다.")
    void shouldReturnUploadStatus_whenSendTargetAdded() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());

        // when
        targetUpload.addTargetList(SendTargetDomainFixture.createSendTarget());

        TargetUploadState expect = targetUpload.getUploadStatus();
        assertThat(expect).isInstanceOf(TargetUploadUploadedState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.UPLOAD);
    }

    @Test
    @DisplayName("대상자 추가 시 TargetUploadedEvent가 발행된다.")
    void shouldPublishTargetUploadedEvent_whenSendTargetAdded() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());

        // when
        TargetUploadEvent expect = targetUpload.addTargetList(SendTargetDomainFixture.createSendTarget());

        assertThat(expect).isInstanceOf(TargetUploadUploadedEvent.class);
    }

    @Test
    @DisplayName("대상자 업로드 완료 시 업로드 상태틑 COMPLETE로 변경된다.")
    void shouldReturnCompleteStatus_whenSendTargetUploadComplete() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());
        targetUpload.addTargetList(SendTargetDomainFixture.createSendTarget());

        targetUpload.uploadComplete(1);
        TargetUploadState expect = targetUpload.getUploadStatus();

        assertThat(expect).isInstanceOf(TargetUploadCompleteState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.COMPLETED);
    }


    @Test
    @DisplayName("대상자 업로드 완료 시 TargetUploadCompletedEvent가 발행된다.")
    void shouldPublishTargetUploadCompletedEvent_whenTargetUploadCompleted() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());
        targetUpload.addTargetList(SendTargetDomainFixture.createSendTarget());

        SendRequestEvent requestEvent = targetUpload.uploadComplete(1);
        SendEvent expect = requestEvent.getEvent();

        assertThat(expect).isInstanceOf(TargetUploadCompletedEvent.class);
    }

    @Test
    @DisplayName("대상자 업로드 수와, 실제 업로드 된 대상자 수가 다르면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadTotalSizeNotEqualsSendTargetListSize() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        targetUpload.createTargetUploadUrlEvent("ObjectKey");
        targetUpload.requestTargetUpload();
        targetUpload.parseMessage(new FakeTemplate(), List.of());
        targetUpload.addTargetList(SendTargetDomainFixture.createSendTarget());

        TargetUploadCompleteStateException expect = TargetUploadCompleteStateException.ofDifferentTargetSize(0, 1);

        assertThatThrownBy(() -> targetUpload.uploadComplete(0))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("대상자 업로드 오류 시 상태는 FAIL로 변경된다.")
    void shouldReturnFailStatus_whenUploadError() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadObjectKeyRequiredException given = TargetUploadObjectKeyRequiredException.of();

        targetUpload.onError(given);

        TargetUploadState expect = targetUpload.getUploadStatus();

        assertThat(expect).isInstanceOf(TargetUploadFailState.class);
        assertThat(expect.currentStatus()).isEqualTo(TargetUploadStatusEnum.FAIL);
    }

    @Test
    @DisplayName("대상자 업로드 익셉션 발생 시 결과 메시지는 익셉션 메시지로 반환된다.")
    void shouldReturnExceptionMessage_whenThrowException(){
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadObjectKeyRequiredException given = TargetUploadObjectKeyRequiredException.of();

        targetUpload.onError(given);

        String expect = targetUpload.getResultMessage();

        assertThat(given.getMessage()).isEqualTo(expect);
    }

    @Test
    @DisplayName("대상자 업로드 실패 시 TargetUploadFailEvent가 발행된다.")
    void shouldPublishTargetUploadFailEvent_whenTargetUploadFailed() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        TargetUploadRequestStateException given = TargetUploadRequestStateException.of(TargetUploadStatusEnum.CREATED);

        TargetUploadEvent expect = targetUpload.onError(given);

        assertThat(expect).isInstanceOf(TargetUploadFailedEvent.class);
    }
}