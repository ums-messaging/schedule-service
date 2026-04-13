package com.ums.schedule.message.domain.status;

import com.ums.schedule.domain.send.domain.status.MessageActiveState;
import com.ums.schedule.domain.send.domain.status.MessageState;
import com.ums.schedule.domain.request.exception.MessageActiveStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageActiveStateTest {

    @Test
    @DisplayName("ACTIVE상태에서 INACTIVE로 변경 시 INACTIVE가 반환된다..")
    void shouldReturnStatusIsInActive_whenTransitionToInActive() {
        MessageActiveState state = new MessageActiveState();

        MessageState result = state.toInActive();

        assertThat(result.currentState()).isEqualTo(MessageStatusEnum.INACTIVE);
    }

    @Test
    @DisplayName("ACTIVE상태에서 ACTIVE로 변경 시 에러가 발생한다.")
    void shouldThrowException_whenTransitionToActive() {
        MessageActiveState state = new MessageActiveState();

        MessageActiveStateException result = MessageActiveStateException.of(MessageStatusEnum.ACTIVE.code());

        assertThatThrownBy(() -> state.toActive())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }
}
