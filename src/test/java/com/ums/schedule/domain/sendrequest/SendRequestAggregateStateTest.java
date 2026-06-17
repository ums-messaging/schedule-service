package com.ums.schedule.domain.sendrequest;

import com.ums.schedule.domain.sendrequest.code.SendRequestEventEnum;
import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.domain.sendrequest.state.SendRequestCreateState;
import com.ums.schedule.domain.sendrequest.state.SendRequestState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SendRequestAggregateStateTest {
    @Test
    @DisplayName("정상 발송 상태 전이는 CREATE → HOLDING → READY → REQUEST → SENDING → COMPLETE 이다.")
    void shouldChangeState_whenSendCompleted() {
        SendRequestState state = new SendRequestCreateState();
        state = state.onEvent(SendRequestEventEnum.SEND_REQUEST_UPDATED);
        state = state.onEvent(SendRequestEventEnum.SEND_REQUEST_READY);
        state = state.onEvent(SendRequestEventEnum.SEND_REQUEST_REQUESTED);
        state = state.onEvent(SendRequestEventEnum.SEND_REQUEST_SEND_STARTED);
        state = state.onEvent(SendRequestEventEnum.SEND_REQUEST_SEND_COMPLETED);

        assertThat(state.getCurrentCode()).isEqualTo(SendRequestStatusEnum.COMPLETED);
    }
}
