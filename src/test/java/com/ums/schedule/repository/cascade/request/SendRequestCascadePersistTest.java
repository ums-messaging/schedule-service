package com.ums.schedule.repository.cascade.request;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.upload.TargetUploadTestBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendRequestCascadePersistTest {
    @Autowired private EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();

        this.scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("send_request 저장 시 target_upload도 함께 저장된다.")
    void shouldPersistTargetUpload_whenSendTargetCreate() {
        Schedule schedule = entityManager.find(Schedule.class, scheduleId);
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).currentTargetUpload(targetUpload).build();
//        targetUpload.assignSendRequest(sendRequest);

        entityManager.persist(sendRequest);
        entityManager.flush();

        Long id = sendRequest.getId();
        entityManager.clear();
        SendRequest find = entityManager.find(SendRequest.class, id);
        TargetUploadReport expect = find.getCurrentTargetUpload();

        assertThat(expect.getUploadId()).isNotNull();
//        assertThat(find.getTargetUploadList()).hasSize(1);

    }
}
