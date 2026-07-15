package com.ums.schedule.domain.request.target.upload;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TargetUploadReportUploadCompleteTest {
    @Test
    @DisplayName("state가 CREATE일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsCreate() {

    }

    @Test
    @DisplayName("state가 HOLDING일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsHolding() {

    }

    @Test
    @DisplayName("state가 REQUEST일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsRequest() {

    }

    @Test
    @DisplayName("state가 PARSING일 때 COMPLETE로 변경된다.")
    void shouldChangeStateToComplete_whenStateIsParsing() {

    }

    @Test
    @DisplayName("success_count의 fail_count의 합계가 total_size가 다르면 익셉션이 발생한다.")
    void shouldThrowException_whenSuccessCountAddFailCountIsNotEqualsToTotalCount() {

    }

    @Test
    @DisplayName("TargetUploadCompletedEvent가 발행된다.")
    void shouldPublishTargetUploadCompletedEvent() {

    }

    @Test
    @DisplayName("익셉션 발생 시 state는 ERROR로 변경된다.")
    void shouldChangeStateToError_whenOccurException() {

    }
}
