package com.ums.schedule.application.sendrequest;

import com.ums.schedule.adapter.api.request.SendRequestCreateRequest;
import com.ums.schedule.adapter.api.sendrequest.email.request.SendRequestCreateRequestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleJpaRepository;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendRequestCreateServiceIntergrationTest {
    @Autowired private ScheduleJpaRepository scheduleRepository;

    private Long scheduleId;
    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        scheduleRepository.save(schedule);
        scheduleId = schedule.getId();
    }

    @Test
    @DisplayName("이메일 발송 요청을 생성한다.")
    void shouldCreateEmailSendRequest() {
        SendRequestCreateRequest sendRequestRequest = SendRequestCreateRequestBuilder.builder()
                .scheduleId(scheduleId)
                .build();
    }
}