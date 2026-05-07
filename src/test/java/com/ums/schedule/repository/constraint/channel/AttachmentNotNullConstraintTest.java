package com.ums.schedule.repository.constraint.channel;

import com.ums.schedule.domain.channel.email.attachment.*;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
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
public class AttachmentNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    @Autowired private EntityManager entityManager;

    private UUID targetId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();
        SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.persist(target);

        entityManager.flush();

        this.targetId = target.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("attachment_name은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenAttachmentNameIsNull() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        AttachmentPolicy policy = AttachmentPolicyTestBuilder.builder().attachmentName(null).build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .attachmentPolicy(policy)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();

        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "ATTACHMENT_NAME");
    }

    @Test
    @DisplayName("download_name은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenDownloadNameIsNull() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        AttachmentPolicy policy = AttachmentPolicyTestBuilder.builder().downloadName(null).build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .attachmentPolicy(policy)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "DOWNLOAD_NAME");
    }

    @Test
    @DisplayName("storage_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenStorageTypeIsNull() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().storageType(null).build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .fileMetaData(fileMetaData)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "STORAGE_TYPE");
    }

    @Test
    @DisplayName("content_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenContentTypeIsNull() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().contentType(null).build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .fileMetaData(fileMetaData)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "CONTENT_TYPE");
    }

    @Test
    @DisplayName("file_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenFileKeyIsNull() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().fileKey(null).build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .fileMetaData(fileMetaData)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "FILE_KEY");
    }

    @Test
    @DisplayName("file_size는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenFileSizeIsNull() {
        SendTarget target = entityManager.getReference(SendTarget.class, targetId);
        FileMetaData fileMetaData = FileMetaDataTestBuilder.builder().fileSize(null).build();
        Attachment attachment = AttachmentTestBuilder.builder()
                .sendTarget(target)
                .fileMetaData(fileMetaData)
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(attachment);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "FILE_SIZE");
    }
}
