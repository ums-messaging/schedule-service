package com.ums.schedule.domain.event.target;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetUploadUrlCreatedEventTest {
    @Test
    @DisplayName("TargetUploadState가 CREATE일때 ,TargetUploadUrlCreatedEvent가 발행되면, PENDING이 반환된다.")
    void shouldReturnTargetUploadStateIsPending_whenTargetUploadStateIsCreate() {

    }

    @Test
    @DisplayName("TargetUploadState가 PENDING일 때, TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsPending() {

    }

    @Test
    @DisplayName("TargetUploadState가 PARSING일 때, TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsParsing() {

    }

    @Test
    @DisplayName("TargetUploadState가 UPLOAD일 떄, TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUploadState가 COMPLETE일 때, TargetUploadUrlCreatedEvent가 발행되면 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadUrlCreatedEvent() {

    }
}