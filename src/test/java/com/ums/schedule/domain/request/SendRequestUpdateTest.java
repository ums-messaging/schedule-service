package com.ums.schedule.domain.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SendRequestUpdateTest {
    @Test
    @DisplayName("발송 요청 상태가 CREATE가 아니면 익셉션이 발생한다.")
    void shouldThrowException_whenStatusIsNotCreate() {

    }

    @Test
    @DisplayName("target_upload 지정 시 target_upload가 NULL이면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadIsNull() {

    }


}
