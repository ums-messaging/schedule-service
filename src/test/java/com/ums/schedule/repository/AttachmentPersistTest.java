package com.ums.schedule.repository;

import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.attachment.AttachmentTestBuilder;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.SendTargetTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AttachmentPersistTest {
    @Autowired private EntityManager entityManager;
    private UUID targetId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();
        SendTarget sendTarget = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.persist(sendTarget);

        targetId = sendTarget.getId();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("attachment 생성 시 저장된다.")
    void shouldPersist_whenAttachmentCreate() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        Attachment attachment = AttachmentTestBuilder.builder().sendTarget(target).build();

        entityManager.persist(attachment);
        entityManager.flush();
        UUID id = attachment.getId();
        entityManager.clear();

        Attachment expect = entityManager.find(Attachment.class, id);

        assertThat(expect.getId()).isNotNull();
    }
}
