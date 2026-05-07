package com.ums.schedule.domain.event.target;

import com.ums.schedule.adapter.api.schedule.ScheduleCreateRequest;
import com.ums.schedule.code.schedule.CycleCdEnum;
import com.ums.schedule.code.schedule.ScheduleEventEnum;
import com.ums.schedule.code.send.ChannelTypeEnum;
import com.ums.schedule.code.send.TargetUploadStatusEnum;
import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.schedule.cycle_policy.CyclePolicyValue;
import com.ums.schedule.domain.schedule.cycle_policy.ScheduleCyclePolicy;
import com.ums.schedule.domain.schedule.period.SchedulePeriod;
import com.ums.schedule.domain.schedule.period.SchedulePeriodTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MessageCreatedEventTest {

    @Test
    @DisplayName("TargetUpload의 상태가 REQUEST일 때, MessageCreatedEvent가 발행되면, TargetUpload의 상태는 PARSING이 반환된다.")
    void shouldReturnTargetUploadStateIsRequest_whenTargetUploadStateIsPending() {
        SendRequest request = SendRequestTestBuilder.builder().build();
        TargetUpload targetUpload = TargetUpload.of(TargetUploadTypeEnum.JSON, request);


        // when

        // then
        assertThat(targetUpload.getStatus()).isEqualTo(TargetUploadStatusEnum.PARSING);
    }

    @Test
    @DisplayName("TargetUpload의 상태가 CREATE일 때, MessageCreateEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsCreate() {
        SendRequest request = SendRequestTestBuilder.builder().build();
        TargetUpload targetUpload = TargetUpload.of(TargetUploadTypeEnum.JSON, request);

        // when

    }

    @Test
    @DisplayName("TargetUpload의 상태가 PARSING일 때, MessageCreateEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsParsing() {

    }

    @Test
    @DisplayName("TargetUpload의 상태가 UPLOAD일 때, MessageCreateEvent가 발행되면, 익셉션이 발생한다.")
    void shouldThrowException_whenTargetUploadStateIsUpload() {

    }

    @Test
    @DisplayName("TargetUpload의 상태가 COMPLETE일 때, MessageCreateEvent가 발행되면, 익셉션이 발생한다. ")
    void shouldThrowException_whenTargetUploadStateIsComplete() {

    }
}