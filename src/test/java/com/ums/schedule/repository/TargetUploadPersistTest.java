package com.ums.schedule.repository;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadTestBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TargetUploadPersistTest {
    @Autowired private EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();
        scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("target_upload 생성 시 저장된다.")
    void shouldPersist_whenTargetUploadCreate() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        TargetUploadReport targetUploadReport = TargetUploadTestBuilder.builder().id(null).build();
        SendRequest sendRequest = SendRequestTestBuilder.builder()
                .id(null)
                .schedule(schedule)
                .currentTargetUpload(targetUploadReport)
                .build();

        targetUploadReport.assignSendRequest(sendRequest);

        entityManager.persist(sendRequest);
        entityManager.flush();

        Long requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
        assertThat(findSendRequest.getCurrentTargetUpload()).isNotNull();

        TargetUploadReport findTargetUpload = findSendRequest.getCurrentTargetUpload();
        assertThat(findTargetUpload.getSendRequest()).isNotNull();
    }
}
