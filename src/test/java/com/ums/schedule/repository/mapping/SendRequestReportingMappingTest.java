package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.*;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestReportRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestReportingMappingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendRequestRepository sendRequestRepository;
    @Autowired private SendRequestReportRepository reportRepository;
    @Autowired private EntityManager entityManager;

    private SendRequest sendRequest;

    @BeforeEach
    void setUp() {
        Schedule schedule = scheduleRepository.saveAndFlush(ScheduleFixture.ofSchedule());
        sendRequest = ScheduleFixture.ofSendRequest();
        sendRequest.applySchedule(schedule);
        entityManager.clear();
    }

    @Test
    @DisplayName("연관관계 주인인 SendRequestReport로 관계를 설정하면 FK가 저장된다.")
    void shouldPersistSendRequestReportFK_whenSetBySendRequest() {
        SendRequest sendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
        SendRequestReport requestReport = ScheduleFixture.ofSendRequestReport();
        requestReport.applySendRequest(sendRequest);

        SendRequestReport saveReport = reportRepository.saveAndFlush(requestReport);
        entityManager.clear();

        assertThat(saveReport.getSendRequest().getId()).isEqualTo(sendRequest.getId());
    }

    @Test
    @DisplayName("비주인인 SendRequest에서만 연관관계를 설정하면 FK는 저장되지 않는다.")
    void shouldNotPersistSendMessageFK_whenSetByInverseOnlySide() {
        // given
        SendRequest sendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
        SendRequestReport report = ScheduleFixture.ofSendRequestReport();

        sendRequest.getSendRequestReportList().add(report);

        assertThatThrownBy(() -> reportRepository.saveAndFlush(report))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFailPersistFK_SendRequestIdIsNull() {
        SendRequestReport requestReport = ScheduleFixture.ofSendRequestReport();

        assertThatThrownBy(() -> reportRepository.saveAndFlush(requestReport))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
