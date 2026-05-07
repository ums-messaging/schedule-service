package com.ums.schedule.repository.constraint.channel;

import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.attachment.AttachmentTestBuilder;
import com.ums.schedule.domain.channel.email.attachment.FileMetaData;
import com.ums.schedule.domain.channel.email.attachment.FileMetaDataTestBuilder;
import com.ums.schedule.domain.request.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.SendTargetTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AttachmentUniqueConstraintTest {
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();

    @Autowired
    private EntityManager entityManager;

    private UUID targetId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();
        SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();
        FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().fileKey("test").build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .fileMetaData(fileMetaData)
                .build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.persist(target);
        entityManager.persist(attachment);
        entityManager.flush();

        this.targetId = target.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("target_id와 file_key는 중복될 수 없다.")
    void shouldThrowException_whenTargetIdAndFileKeyAreDuplicated() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().fileKey("test").build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .fileMetaData(fileMetaData)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_FILE_KEY");
    }
}
