package com.ums.schedule.repository.mapping.target;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.SendTargetTestBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendTargetMappingTest {
    @Autowired private EntityManager entityManager;
    private Long uploadId;

    @BeforeEach
    public void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.flush();

        uploadId = targetUpload.getUploadId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("target_upload 연관관계 테스트")
    class TargetUploadMappingTest {
        @Test
        @DisplayName("target_upload에서 연관관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistTargetUploadFK_whenSetByInverseOnlySide() {
            TargetUploadReport targetUpload = entityManager.find(TargetUploadReport.class, uploadId);
            SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

            entityManager.persist(targetUpload);
            entityManager.flush();
            entityManager.clear();

            TargetUploadReport expect = entityManager.find(TargetUploadReport.class, uploadId);
        }

        @Test
        @DisplayName("send_target에서 target_upload와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkTargetUpload_whenSetBySendTarget() {
            TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);
            SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

            entityManager.persist(target);
            entityManager.flush();
            entityManager.clear();

            TargetUploadReport expect = entityManager.find(TargetUploadReport.class, uploadId);
        }

        @Test
        @DisplayName("FK인 target_upload는 NULL을 허용하지 않는다.")
        void shouldThrowException_whenSendRequestIsNull() {
            TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);
            SendTarget target = SendTargetTestBuilder.builder().targetUpload(null).build();

            assertThatThrownBy(() -> {
                entityManager.persist(target);
                entityManager.flush();
            }).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContainingAll(DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage(), "UPLOAD_ID");

        }
    }
}
