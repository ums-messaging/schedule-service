package com.ums.schedule.repository.constraint.target;

import com.ums.schedule.common.code.target_upload.TargetUploadStatusEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TargetUploadDefaultConstraintTest {
    @Autowired
    private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.flush();
        this.requestId = sendRequest.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("status의 기본 값은 CREATE이다.")
    void shouldReturnStatusIsCreate_whenStatusIsNull() {
        SendRequest sendRequest = entityManager.getReference(SendRequest.class, requestId);
        TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder()
                .sendRequest(sendRequest)
                .uploadStatus(null).build();

        entityManager.persist(targetUpload);

        assertThat(targetUpload.getState().getCurrentCode()).isEqualTo(TargetUploadStatusEnum.CREATED);

    }

    @Test
    @DisplayName("created_at의 기본 값은 현재 시각이다.")
    void shouldReturnCurrentTime_whenCreatedAtIsNull() {
        SendRequest sendRequest = entityManager.getReference(SendRequest.class, requestId);
        TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder()
                .sendRequest(sendRequest)
                .createdAt(null)
                .build();

        entityManager.persist(targetUpload);

        assertThat(targetUpload.getCreatedAt().toLocalDate().atStartOfDay())
                .isEqualTo(LocalDateTime.now().toLocalDate().atStartOfDay());
    }
}
