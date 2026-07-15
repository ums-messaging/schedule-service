package com.ums.schedule.repository.constraint.target;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.request.target.SendTarget;
import com.ums.schedule.domain.request.target.SendTargetTestBuilder;
import com.ums.schedule.domain.request.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import com.ums.schedule.fixture.field.SendTargetField;
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
public class SendTargetNotNullConstraintTest {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();
    @Autowired
    private EntityManager entityManager;
    private UUID uploadId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();
        TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().sendRequest(sendRequest).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.flush();

        uploadId = targetUpload.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("target_key는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenTargetKeyIsNull() {
        TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);

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
        TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);

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
        TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);

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
        TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);

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
        TargetUploadReport targetUpload = entityManager.getReference(TargetUploadReport.class, uploadId);

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
