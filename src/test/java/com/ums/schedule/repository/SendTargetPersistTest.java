package com.ums.schedule.repository;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.SendTargetTestBuilder;
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
public class SendTargetPersistTest {
    @Autowired private EntityManager entityManager;
    private Long uploadId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().build();
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().build();


        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);

        uploadId = targetUpload.getUploadId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("send_target 생성 시 저장된다.")
    void shouldPersist_whenSendTargetCreate() {
        TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);
        SendTarget sendTarget = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

        entityManager.persist(sendTarget);
        entityManager.flush();

        assertThat(sendTarget.getId()).isNotNull();
    }
}