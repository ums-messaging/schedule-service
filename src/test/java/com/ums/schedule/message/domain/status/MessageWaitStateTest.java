package com.ums.schedule.message.domain.status;

import com.ums.schedule.domain.send.domain.status.MessageState;
import com.ums.schedule.domain.send.domain.status.MessageWaitState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageWaitStateTest {

    @Test
    @DisplayName("WAIT 상태에서 INACTIVE로 변경 시 INACTIVE를 반환한다.")
    void shouldReturnStatusIsInActive_whenTransitionToInActive() {
        MessageWaitState state = new MessageWaitState();

        MessageState result = state.toInActive();

        assertThat(result.currentState()).isEqualTo(MessageStatusEnum.INACTIVE);
    }

    @Test
    @DisplayName("WAIT 상태에서 Active()를 호출하면 Status는 ACTIVE를 반환한다.")
    void shouldReturnStatusIsActive_whenTransitionToActive() {
        MessageWaitState state = new MessageWaitState();

        MessageState result = state.toActive();

        assertThat(result.currentState()).isEqualTo(MessageStatusEnum.ACTIVE);
    }

}