package com.ums.schedule.repository.constraint.target;

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
import com.ums.schedule.fixture.field.SendTargetField;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendTargetDefaultConstraintTest {
    @Autowired private EntityManager entityManager;
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
    @DisplayName("attempt_no는 기본 값이 1이다.")
    void shouldReturnAttemptNoIsOne_whenAttemptNoIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);
        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .attemptNo(null)
                .targetUpload(targetUpload).build();

        entityManager.persist(sendTarget);
        entityManager.flush();

        assertThat(sendTarget.getAttemptNo()).isEqualTo(1);
    }

    @Test
    @DisplayName("created_at의 기본 값은 현재 시각이다.")
    void shouldReturnCreatedAtIsCurrentTime_whenCreatedAtIsNull() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);
        SendTarget sendTarget = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .createdAt(null).build();
        SendTargetField field = SendTargetField.CREATED_AT;

        ReflectionTestUtils.setField(sendTarget,field.value(), null);

        entityManager.persist(sendTarget);
        entityManager.flush();

        assertThat(sendTarget.getCreatedAt().toLocalDate().atStartOfDay())
                .isEqualTo(LocalDate.now().atStartOfDay());
    }
}
