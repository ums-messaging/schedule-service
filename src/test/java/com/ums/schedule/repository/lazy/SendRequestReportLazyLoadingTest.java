package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.domain.SendRequestReport;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestReportLazyLoadingTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendRequestRepository sendRequestRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("SendRequest 조회 시 SendRequestReport 는 조회되지 않는다.")
    void shouldNotLoadSendRequestReportList_whenFindSendRequest() {
        // Given
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();

        sendRequest.applySchedule(schedule);
        sendRequestReport.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        // When
        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();

        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSendRequest, "sendRequestReportList"))
                .isFalse();
    }

    @Test
    @DisplayName("SendRequest 조회 시 SendRequestReport에 접근하면 쿼리가 실행된다.")
    void shouldLoadSendRequestReportList_whenAccessed() {
        // Given
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();

        sendRequest.applySchedule(schedule);
        sendRequestReport.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        // When
        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();
        findSendRequest.getSendRequestReportList().size();

        PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(util.isLoaded(findSendRequest, "sendRequestReportList"))
                .isTrue();
    }

    @Test
    @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
    void shouldThrowLazyInitializationException_whenAccessSendRequestReportListOutsideTransaction() {
        // Given
        Long requestId = null;
        Schedule schedule = ScheduleFixture.ofSchedule();
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        SendRequestReport sendRequestReport = ScheduleFixture.ofSendRequestReport();

        sendRequest.applySchedule(schedule);
        sendRequestReport.applySendRequest(sendRequest);
        scheduleRepository.saveAndFlush(schedule);
        requestId = sendRequest.getId();
        entityManager.clear();

        // When
        SendRequest findSendRequest = sendRequestRepository.findById(requestId).orElseThrow();
        entityManager.detach(findSendRequest);

        assertThatThrownBy(() -> findSendRequest.getSendRequestReportList().size())
                .isInstanceOf(LazyInitializationException.class);
    }
}
