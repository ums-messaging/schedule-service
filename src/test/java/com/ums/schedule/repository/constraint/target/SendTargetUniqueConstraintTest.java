package com.ums.schedule.repository.constraint.target;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendTargetUniqueConstraintTest {
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();

    @Autowired
    private EntityManager entityManager;
    private Long uploadId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();
        SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload)
                .contact("jang314@naver.com")
                .targetKey("test")
                .build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.persist(target);

        entityManager.flush();

        uploadId = targetUpload.getUploadId();
        entityManager.clear();
    }

    @Test
    @DisplayName("upload_id와 contact가 중복되면 익셉션이 발생한다.")
    void shouldThrowException_whenUploadIdAndContactAreDuplicated(){
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);
        SendTarget target = SendTargetTestBuilder.builder()
                .targetUpload(targetUpload)
                .targetKey("test2")
                .contact("jang314@naver.com").build();

        assertThatThrownBy(() -> {
            entityManager.persist(target);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_CONTACT");

    }

    @Test
    @DisplayName("upload_id와 target_key가 중복되면 익셉션이 발생한다.")
    void shouldThrowException_whenUploadIdAndTargetKeyAreDuplicated() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);
        SendTarget sendTarget = SendTargetTestBuilder.builder().targetUpload(targetUpload)
                .contact("jang315@naver.com")
                .targetKey("test").build();


        assertThatThrownBy(() -> {
            entityManager.persist(sendTarget);
            entityManager.flush();
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_KEY");

    }

    @Test
    @DisplayName("target_key와 contact가 중복되지 않으면 저장된다.")
    void shouldPersist_whenTargetKeyAndContactAreNotDuplicated() {
        TargetUpload targetUpload = entityManager.getReference(TargetUpload.class, uploadId);
        SendTarget sendTarget = SendTargetTestBuilder.builder().targetUpload(targetUpload)
                .contact("jang315@naver.com")
                .targetKey("test1").build();

        entityManager.persist(sendTarget);
        entityManager.flush();

        assertThat(sendTarget).isNotNull();
    }
}
