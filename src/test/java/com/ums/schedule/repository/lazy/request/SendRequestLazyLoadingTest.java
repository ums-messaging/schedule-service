package com.ums.schedule.repository.lazy.request;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestLazyLoadingTest {
    @Autowired private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest request = SendRequestEntityBuilder.builder().schedule(schedule).build();
        TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().sendRequest(request).build();
        entityManager.persist(schedule);
        entityManager.persist(request);
        entityManager.persist(targetUpload);

        request.assignTargetUpload(targetUpload);
        entityManager.flush();

        this.requestId = request.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("target_upload 조회 테스트")
    class TargetUploadLazyLoadingTest {
        @Test
        @DisplayName("send_request 조회 시 target_upload는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(sendRequest.getCurrentTargetUpload())).isFalse();
        }

        @Test
        @DisplayName("send_request 조회 시 target_upload에 접근하면 쿼리가 실행된다.")
        void shouldLoadCurrentTargetUpload_whenGetCurrentTargetUpload() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);

            sendRequest.getCurrentTargetUpload().getState();

            assertThat(Hibernate.isInitialized(sendRequest.getCurrentTargetUpload())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetCurrentTargetUploadOutsideTransaction() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);

            entityManager.clear();

            assertThatThrownBy(() -> sendRequest.getCurrentTargetUpload().getState().getCurrentCode())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }

    @Nested
    @DisplayName("schedule 조회 테스트")
    class ScheduleLazyLoadingTest {
        @Test
        @DisplayName("send_request 조회 시 schedule은 조회되지 않는다.")
        void shouldNotLoadSchedule_whenFindSendRequest() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(sendRequest.getSchedule())).isFalse();
        }

        @Test
        @DisplayName("send_request 조회 시 schedule에 접근하면 쿼리가 실행된다.")
        void shouldLoadSchedule_whenGetSchedule() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);

            sendRequest.getSchedule().getName();

            assertThat(Hibernate.isInitialized(sendRequest.getSchedule())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetScheduleOutsideTransaction() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);

            entityManager.clear();

            assertThatThrownBy(() -> sendRequest.getSchedule().getName())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }
}
