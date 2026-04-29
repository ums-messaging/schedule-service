package com.ums.schedule.domain.event.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetUploadUploadedEventTest {
    @Test
    @DisplayName("TargetUploadState가 PARSING일 때, TargetUploadUploadedEvent가 발행되면 State는 Complete가 반환된다.")
    void shouldReturnTargetUpload_whenTargetUploadStateIsParsing() {

    }

    @Test
    @DisplayName("TargetUploadState가 CREATED일 때, TargetUploadUploadedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsCreate() {

    }

    @Test
    @DisplayName("TargetUploadState가 UPLOAD일 때, TargetUploadUploadedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUploadState가 COMPLETE일 때, TargetUploadUploadedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsComplete() {

    }
}