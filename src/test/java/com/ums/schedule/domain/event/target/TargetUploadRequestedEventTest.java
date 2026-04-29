package com.ums.schedule.domain.event.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TargetUploadRequestedEventTest {

    @Test
    @DisplayName("TargetUpload의 State가 Pending일 때, TargetUploadRequested 이벤트가 발행되면, State는 REQUEST가 반환된다.")
    void shouldReturnTargetUploadStateIsRequest_whenTargetUploadStateIsPending() {

    }

    @Test
    @DisplayName("TargetUpload의 State가 Create일 때, TargetUploadRequested 이벤트가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsCreate() {

    }

    @Test
    @DisplayName("TargetUpload의 State가 Request일 때, TargetUploadRequested 이벤트가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsRequest() {

    }

    @Test
    @DisplayName("TargetUpload의 State가 Parsing일 때, TargetUploadRequested 이벤트가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsParsing() {

    }

    @Test
    @DisplayName("TargetUpload의 State가 Upload일 때, TargetUploadRequested 이벤트가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUpload의 State가 Complete일 때, TargetUploadRequested 이벤트가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsComplete() {

    }
}