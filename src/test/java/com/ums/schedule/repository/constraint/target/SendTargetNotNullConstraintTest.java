package com.ums.schedule.repository.constraint.target;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.SendTargetTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.fixture.field.SendTargetField;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendTargetNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired
    private EntityManager entityManager;
    private Long uploadId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.flush();

        uploadId = targetUpload.getUploadId();
        entityManager.clear();
    }

    @Test
    @DisplayName("target_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenTargetKeyIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);

        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .targetKey(null)
                .build();
        SendTargetField field = SendTargetField.TARGET_KEY;

        assertThatThrownBy(() -> {
            entityManager.persist(sendTarget);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field.name(), ERROR_MESSAGE);
    }

    @Test
    @DisplayName("target_name은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenTargetNameIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);

        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .targetName(null)
                .build();

        SendTargetField field = SendTargetField.TARGET_NAME;

        assertThatThrownBy(() -> {
            entityManager.persist(sendTarget);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field.name(), ERROR_MESSAGE);
    }

    @Test
    @DisplayName("contact는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenContactIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);

        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .contact(null)
                .build();

        SendTargetField field = SendTargetField.CONTACT;

        entityManager.persist(sendTarget);

        assertThatThrownBy(() -> entityManager.flush())
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field.name(), ERROR_MESSAGE);
    }

    @Test
    @DisplayName("status는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenStatusIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);

        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .state(null)
                .build();

        SendTargetField field = SendTargetField.STATUS;

        assertThatThrownBy(() -> {
            entityManager.persist(sendTarget);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }

    @Test
    @DisplayName("content는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenContentIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);

        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .content(null)
                .build();

        SendTargetField field = SendTargetField.CONTENT;

        assertThatThrownBy(() -> {
            entityManager.persist(sendTarget);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(field.name(), ERROR_MESSAGE);
    }
}
