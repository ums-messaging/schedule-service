package com.ums.schedule.domain.request.state;

import com.ums.schedule.code.send.SendRequestStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestDomainFixture;
import com.ums.schedule.domain.request.SendRequestEvent;
import com.ums.schedule.domain.request.event.SendRequestCompletedEvent;
import com.ums.schedule.domain.request.event.SendRequestScheduledEvent;
import com.ums.schedule.domain.request.event.SendRequestStartedEvent;
import com.ums.schedule.domain.request.event.SendRequestedEvent;
import com.ums.schedule.domain.target.event.TargetUploadCompletedEvent;
import com.ums.schedule.domain.target.event.TargetUploadRequestedEvent;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SendRequestStateTest {
    @Test
    @DisplayName("발송 상태 전이 총 테스트")
    void shouldSendStateTransition_whenSendEventPublished() {
        TargetUpload targetUpload = TargetUploadDomainFixture.createTargetUpload();
        SendRequest request = SendRequestDomainFixture.createSendRequest();

        SendRequestState state = new SendRequestCreateState();
        state = state.onEvent(SendRequestEvent.of(request, TargetUploadRequestedEvent.of(targetUpload)));
        state = state.onEvent(SendRequestEvent.of(request, TargetUploadCompletedEvent.of(targetUpload)));
        state = state.onEvent(SendRequestEvent.of(request, TargetUploadRequestedEvent.of(targetUpload)));
        state = state.onEvent(SendRequestEvent.of(request, TargetUploadCompletedEvent.of(targetUpload)));
        state = state.onEvent(SendRequestEvent.of(request, SendRequestedEvent.of(request)));
        state = state.onEvent(SendRequestEvent.of(request, SendRequestScheduledEvent.of(request)));
        state = state.onEvent(SendRequestEvent.of(request, SendRequestStartedEvent.of(request)));
        state = state.onEvent(SendRequestEvent.of(request, SendRequestCompletedEvent.of(request)));

        assertThat(state).isInstanceOf(SendRequestCompleteState.class);
        assertThat(state.currentSendRequestStatus()).isEqualTo(SendRequestStatusEnum.COMPLETED);
    }
}