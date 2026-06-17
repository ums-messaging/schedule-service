package com.ums.schedule.repository.lazy.target;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.upload.TargetUploadTestBuilder;
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
public class TargetUploadLazyLoadingTest {
    @Autowired private EntityManager entityManager;
    private Long uploadId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest request = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().sendRequest(request).build();

        entityManager.persist(schedule);
        entityManager.persist(request);
        entityManager.persist(targetUpload);

        entityManager.flush();

        this.uploadId = targetUpload.getUploadId();
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
