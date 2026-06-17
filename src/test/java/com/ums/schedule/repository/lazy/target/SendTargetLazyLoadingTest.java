package com.ums.schedule.repository.lazy.target;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.SendTargetTestBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadTestBuilder;
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
public class SendTargetLazyLoadingTest {
    @Autowired private EntityManager entityManager;
    private UUID targetId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest request = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().sendRequest(request).build();
        SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

        entityManager.persist(schedule);
        entityManager.persist(request);
        entityManager.persist(targetUpload);
        entityManager.persist(target);

        entityManager.flush();

        targetId = target.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("target_upload 조회 테스트")
    class TargetUploadLazyLoadingTest {
        @Test
        @DisplayName("send_target 조회 시 target_upload는 조회되지 않는다.")
        void shouldNotLoadTargetUpload_whenFindSendTarget() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            assertThat(Hibernate.isInitialized(sendTarget.getTargetUpload())).isFalse();
        }

        @Test
        @DisplayName("send_target 조회 시 target_upload에 접근하면 쿼리가 실행된다.")
        void shouldLoadTargetUpload_whenGetTargetUpload() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            sendTarget.getTargetUpload().getState();

            assertThat(Hibernate.isInitialized(sendTarget.getTargetUpload())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetTargetUploadOutsideTransaction() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            entityManager.clear();

            assertThatThrownBy(() -> sendTarget.getTargetUpload().getState())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }
}
