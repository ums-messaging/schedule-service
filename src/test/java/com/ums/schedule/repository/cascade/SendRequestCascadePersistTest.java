package com.ums.schedule.repository.cascade;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendRequestCascadePersistTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendRequestRepository sendRequestRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("SendRequest 저장 시 SendMessage도 함께 저장된다.")
    void shouldPersistSendMessage_whenSavingSendRequest() {
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
        sendRequest.applySchedule(schedule);
        sendMessage.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();

        assertThat(findSendRequest.getSendMessages()).hasSize(1);
    }

    @Test
    @DisplayName("SendRequest 저장 시 SendRequestReport도 함께 저장된다.")
    void shouldPersistSendRequestReport_whenSavingSendRequest() {
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendRequestReport sendReport = ScheduleFixture.ofSendRequestReport();
        sendRequest.applySchedule(schedule);
        sendReport.applySendRequest(sendRequest);

        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();
        assertThat(findSendRequest.getSendRequestReportList()).hasSize(1);
    }
}
