package com.ums.schedule.domain.request;

import com.ums.schedule.common.code.request.SendRequestEvent;
import com.ums.schedule.common.code.request.SendRequestStatus;
import com.ums.schedule.domain.request.state.SendRequestCreateState;
import com.ums.schedule.domain.request.state.SendRequestState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SendRequestAggregateStateTest {
    @Test
    @DisplayName("정상 발송 상태 전이는 CREATE → HOLDING → READY → REQUEST → SENDING → COMPLETE 이다.")
    void shouldChangeState_whenSendCompleted() {
        SendRequestState state = new SendRequestCreateState();
        state = state.onEvent(SendRequestEvent.SEND_REQUEST_UPDATED);
        state = state.onEvent(SendRequestEvent.SEND_REQUEST_READY);
        state = state.onEvent(SendRequestEvent.SEND_REQUEST_REQUESTED);
        state = state.onEvent(SendRequestEvent.SEND_REQUEST_SEND_STARTED);
        state = state.onEvent(SendRequestEvent.SEND_REQUEST_SEND_COMPLETED);

        assertThat(state.getCurrentCode()).isEqualTo(SendRequestStatus.COMPLETED);
    }
}
