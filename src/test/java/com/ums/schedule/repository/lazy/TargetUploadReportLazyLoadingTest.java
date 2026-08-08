package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class TargetUploadReportLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;
    private UUID uploadId;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage);
        persistTargetUploadReportAndGetUploadId(sendRequest);
    }

    private void persistTargetUploadReportAndGetUploadId(SendRequest sendRequest) {
        TargetUploadReport targetUploadReport = TargetUploadReportEntityBuilder.builder()
                .sendRequest(sendRequest)
                .build();
        persist(targetUploadReport);
        uploadId = targetUploadReport.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_request 조회 테스트")
    class SendRequestLazyLoadingTest {
        @Test
        @DisplayName("target_upload 조회 시 send_request는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            TargetUploadReport targetUpload = entityManager.find(TargetUploadReport.class, uploadId);

            assertThat(Hibernate.isInitialized(targetUpload.getSendRequest()))
                    .isFalse();
        }

        @Test
        @DisplayName("target_upload 조회 시 send_request에 접근하면 쿼리가 실행된다.")
        void shouldLoadSendRequest_whenGetSendRequest() {
            TargetUploadReport targetUpload = entityManager.find(TargetUploadReport.class, uploadId);

            targetUpload.getSendRequest().getChannelType();

            assertThat(Hibernate.isInitialized(targetUpload.getSendRequest()))
                    .isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetSendRequestOutsideTransaction() {
            TargetUploadReport targetUpload = entityManager.find(TargetUploadReport.class, uploadId);

            entityManager.clear();

            assertThatThrownBy(() -> targetUpload.getSendRequest().getChannelType())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }
}
