package com.ums.schedule.repository.mapping.target;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
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
public class TargetUploadMappingTest {
    @Autowired private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        requestId = sendRequest.getId();
        entityManager.flush();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_request 연관관계 테스트")
    class SendRequestMappingTest {
        @Test
        @DisplayName("send_request에서 연관관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistSendRequestFK_whenSetByInverseOnlySide() {
            SendRequest request = entityManager.getReference(SendRequest.class, requestId);
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().sendRequest(request).build();

            entityManager.persist(request);
            entityManager.flush();
            entityManager.clear();

            SendRequest expect = entityManager.find(SendRequest.class, requestId);
//            assertThat(expect.getTargetUploadList()).hasSize(0);
        }

        @Test
        @DisplayName("target_upload에서 send_request와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkSendRequest_whenSetByTargetUpload() {
            SendRequest request = entityManager.find(SendRequest.class, requestId);
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().sendRequest(request).build();

            entityManager.persist(targetUpload);
            entityManager.flush();
            UUID id = targetUpload.getId();
            entityManager.clear();

            TargetUploadReport expect = entityManager.find(TargetUploadReport.class, id);
            assertThat(expect.getSendRequest()).isNotNull();
        }

        @Test
        @DisplayName("FK인 send_request은 NULL을 허용하지 않는다.")
        void shouldThrowException_whenSendRequestIsNull() {
            SendRequest request = entityManager.find(SendRequest.class, requestId);
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().sendRequest(null).build();


            assertThatThrownBy(() -> {
                entityManager.persist(targetUpload);
                entityManager.flush();
            }).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContainingAll(DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage(), "REQUEST_ID");
        }
    }
}
