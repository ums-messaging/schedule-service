package com.ums.schedule.repository.constraint.target;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadReport;
import com.ums.schedule.domain.sendrequest.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.fixture.field.TargetUploadField;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class TargetUploadNotNullConstraintTest {
    private static final String ERROR_MESSAGE = "NULL not allowed for column";
    @Autowired EntityManager entityManager;
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
    @DisplayName("upload_type은 NULL을 허용하지 않는다.")
    void shouldThrowException_whenUploadTypeIsNull() {
        SendRequest sendRequest = entityManager.getReference(SendRequest.class, requestId);
        TargetUploadReport targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest)
                .uploadType(null).build();

        TargetUploadField field = TargetUploadField.UPLOAD_TYPE;

        assertThatThrownBy(() -> {
            entityManager.persist(targetUpload);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, field.name());
    }
}
