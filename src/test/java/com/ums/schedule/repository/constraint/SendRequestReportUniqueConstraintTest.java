package com.ums.schedule.repository.constraint;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestReportRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
public class SendRequestReportUniqueConstraintTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendRequestReportRepository sendRequestReportRepository;
    @Autowired private EntityManager entityManager;

    private SendRequest sendRequest;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);

        scheduleRepository.saveAndFlush(schedule);
        this.sendRequest = sendRequest;

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("total_cnt는 null을 허용하지 않는다.")
    void shouldFail_TotalCntIsNotNull() {
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();
        setField(sendRequestReport, "totalCnt", null);
        sendRequestReport.applySendRequest(sendRequest);

        assertThatThrownBy(() -> sendRequestReportRepository.saveAndFlush(sendRequestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("fail_cnt는 null을 허용하지 않는다.")
    void shouldFail_FailCntIsNotNull() {
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();
        setField(sendRequestReport, "failCnt", null);
        sendRequestReport.applySendRequest(sendRequest);

        assertThatThrownBy(() -> sendRequestReportRepository.saveAndFlush(sendRequestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("made_cnt는 null을 허용하지 않는다.")
    void shouldFail_MadeCntIsNotNull() {
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();
        setField(sendRequestReport, "madeCnt", null);
        sendRequestReport.applySendRequest(sendRequest);

        assertThatThrownBy(() -> sendRequestReportRepository.saveAndFlush(sendRequestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("success_cnt는 null을 허용하지 않는다.")
    void shouldFail_SuccessCntIsNotNull() {
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();
        setField(sendRequestReport, "successCnt", null);
        sendRequestReport.applySendRequest(sendRequest);

        assertThatThrownBy(() -> sendRequestReportRepository.saveAndFlush(sendRequestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("aggregated_at은 null을 허용하지 않는다.")
    void shouldFail_AggregatedAtIsNotNull() {
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();
        setField(sendRequestReport, "aggregatedAt", null);
        sendRequestReport.applySendRequest(sendRequest);

        assertThatThrownBy(() -> sendRequestReportRepository.saveAndFlush(sendRequestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("FK인 send_request_id는 null을 허용하지 않는다.")
    void shouldFail_SendRequestId() {
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();

        assertThatThrownBy(()-> sendRequestReportRepository.saveAndFlush(sendRequestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
