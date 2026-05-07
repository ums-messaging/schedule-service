package com.ums.schedule.repository;

import com.ums.schedule.code.send.TargetUploadTypeEnum;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.SendTargetTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.fixture.ScheduleDomainFixture;
import com.ums.schedule.domain.request.SendRequestDomainFixture;
import com.ums.schedule.fixture.SendTargetDomainFixture;
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
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().build();


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
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);
        SendTarget sendTarget = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

        entityManager.persist(sendTarget);
        entityManager.flush();

        assertThat(sendTarget.getId()).isNotNull();
    }
}