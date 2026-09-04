package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class SendRequestLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        persistSendRequestAndGetRequestId(schedule, sendMessage);
    }

    @Nested
    @DisplayName("send_message 조회 테스트")
    class SendMessageLazyLoadingTest {
        @Test
        @DisplayName("send_request 조회 시 send_message는 조회되지 않는다.")
        void shouldNotLoadSchedule_whenFindSendRequest() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(findSendRequest.getSendMessage())).isFalse();
        }

        @Test
        @DisplayName("send_request 조회 시 send_message에 접근하면 쿼리가 실행된다.")
        void shouldLoadSchedule_whenGetSendMessage() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(findSendRequest.getSendMessage().getMessageType())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetSendMessageOutsideTransaction() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
            entityManager.clear();

            assertThatThrownBy(() -> findSendRequest.getSendMessage().getMessageType())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }

    @Nested
    @DisplayName("target_upload_report 조회 테스트")
    class TargetUploadReportLazyLoadingTest {
        @BeforeEach
        void setUp() {
            UUID uploadId = findSendRequestAndPersistTargetUploadReport();
            findSendRequestAndUpdateCurrentTargetUploadReport(uploadId);
        }

        private void findSendRequestAndUpdateCurrentTargetUploadReport(UUID uploadId) {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
            TargetUploadReport findTargetUpload = entityManager.find(TargetUploadReport.class, uploadId);
            findSendRequest.assignTargetUpload(findTargetUpload);
            entityManager.flush();
            entityManager.clear();
        }

        private UUID findSendRequestAndPersistTargetUploadReport() {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);
            TargetUploadReport targetUploadReport = TargetUploadReportEntityBuilder.builder().sendRequest(sendRequest).build();
            persist(targetUploadReport);
            UUID uploadId = targetUploadReport.getId();
            entityManager.clear();
            return uploadId;
        }

        @Test
        @DisplayName("send_request 조회 시 target_upload_report 는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(findSendRequest.getCurrentTargetUpload())).isFalse();
        }

        @Test
        @DisplayName("send_request 조회 시 target_upload_report에 접근하면 쿼리가 실행된다.")
        void shouldLoadCurrentTargetUpload_whenGetCurrentTargetUpload() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(
                    findSendRequest.getCurrentTargetUpload().getDownloadKey())
            ).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetCurrentTargetUploadOutsideTransaction() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
            entityManager.clear();
            assertThatThrownBy(() -> findSendRequest.getCurrentTargetUpload().getState().getCurrentCode())
                    .isInstanceOf(LazyInitializationException.class);

        }
    }

    @Nested
    @DisplayName("schedule 조회 테스트")
    class ScheduleLazyLoadingTest {
        @Test
        @DisplayName("send_request 조회 시 schedule은 조회되지 않는다.")
        void shouldNotLoadSchedule_whenFindSendRequest() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(Hibernate.isInitialized(findSendRequest.getSchedule())).isFalse();
        }

        @Test
        @DisplayName("send_request 조회 시 schedule에 접근하면 쿼리가 실행된다.")
        void shouldLoadSchedule_whenGetSchedule() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
            findSendRequest.getSchedule().getName();

            assertThat(Hibernate.isInitialized(findSendRequest.getSchedule())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetScheduleOutsideTransaction() {
            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
            entityManager.clear();

            assertThatThrownBy(() -> findSendRequest.getSchedule().getName())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }

    private void persistSendRequestAndGetRequestId(Schedule schedule, SendMessage sendMessage) {
        SendRequest sendRequest = SendRequestEntityBuilder.builder()
                .schedule(schedule)
                .sendMessage(sendMessage).build();
        persist(sendRequest);
        requestId = sendRequest.getId();
        entityManager.clear();
    }
}
