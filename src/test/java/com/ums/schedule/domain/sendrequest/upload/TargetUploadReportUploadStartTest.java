package com.ums.schedule.domain.sendrequest.upload;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TargetUploadReportUploadStartTest {
    @Test
    @DisplayName("state가 CREATE일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsCreate() {

    }

    @Test
    @DisplayName("state가 HOLDING 일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsHolding() {

    }

    @Test
    @DisplayName("state가 REQUEST일 때, PARSING으로 변경된다.")
    void shouldChangeStateToParsing_whenStateIsRequest() {

    }

    @Test
    @DisplayName("state가 PARSING일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsParsing() {

    }

    @Test
    @DisplayName("state가 COMPLETE일 때 익셉션이 발생한다.")
    void shouldThrowException_whenStateIsComplete() {

    }

    @Test
    @DisplayName("template이 NULL이면 익셉션이 발생한다.")
    void shouldThrowException_whenTemplateIsNull() {

    }

    @Test
    @DisplayName("TargetUploadStartedEvent가 발행된다.")
    void shouldPublishTargetUploadParsedEvent() {

    }
    @Test
    @DisplayName("익셉션 발생 시 state는 ERROR로 변경된다.")
    void shouldChangeStateToError_whenOccurException() {

    }
}
