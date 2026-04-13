package com.ums.schedule.message.domain.status;

import com.ums.schedule.domain.send.domain.status.MessageInActiveState;
import com.ums.schedule.domain.request.exception.MessageActiveStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MessageInActiveStateTest {

    @Test
    @DisplayName("INACTIVE상태에서 ACTIVE로 변경 시 에러가 발생한다.")
    void shouldReturnStatusIsInActive_whenTransitionToActive() {
        MessageInActiveState state = new MessageInActiveState();

        MessageActiveStateException result = MessageActiveStateException.of(MessageStatusEnum.INACTIVE.code());

        assertThatThrownBy(() -> state.toActive())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }

    @Test
    @DisplayName("INACTIVE상태에서 ACTIVE로 변경 시 에러가 발생한다.")
    void shouldThrowException_wheTransitionToActive() {
        MessageInActiveState state = new MessageInActiveState();

        MessageActiveStateException result = MessageActiveStateException.of(MessageStatusEnum.INACTIVE.code());

        assertThatThrownBy(() -> state.toActive())
                .isInstanceOf(result.getClass())
                .hasMessage(result.getMessage());
    }
}
