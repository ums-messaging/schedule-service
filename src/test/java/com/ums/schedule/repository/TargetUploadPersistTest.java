package com.ums.schedule.repository;

import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
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
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.flush();

        this.requestId = sendRequest.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("target_upload 생성 시 저장된다.")
    void shouldPersist_whenTargetUploadCreate() {
        SendRequest request = entityManager.getReference(SendRequest.class, requestId);
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(request).build();

        entityManager.persist(targetUpload);
        entityManager.flush();

        Long uploadId = targetUpload.getUploadId();
        entityManager.clear();

        TargetUpload expect = entityManager.find(TargetUpload.class, uploadId);
        assertThat(expect.getUploadId()).isNotNull();
    }
}
