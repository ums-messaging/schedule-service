package com.ums.schedule.repository.constraint.unique;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class SendTargetUniqueConstraintTest extends EntityJpaTestSupport {
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();

    private SendTargetEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage);
        TargetUploadReport targetUpload = givenTargetUploadReport(sendRequest);
        entityBuilder = SendTargetEntityBuilder.builder();

        SendTarget target = entityBuilder.targetUpload(targetUpload)
                .contact("jang314@naver.com")
                .targetKey("test")
                .build();
        persist(target);
    }

    @Test
    @DisplayName("upload_id와 contact가 중복되면 익셉션이 발생한다.")
    void shouldThrowException_whenUploadIdAndContactAreDuplicated(){
        SendTarget target = entityBuilder
                .targetKey("test2")
                .contact("jang314@naver.com").build();

        assertThatThrownBy(() -> persist(target))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_CONTACT");
    }

    @Test
    @DisplayName("upload_id와 target_key가 중복되면 익셉션이 발생한다.")
    void shouldThrowException_whenUploadIdAndTargetKeyAreDuplicated() {
        SendTarget sendTarget = entityBuilder.contact("jang315@naver.com")
                .targetKey("test").build();


        assertThatThrownBy(() -> persist(sendTarget))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_KEY");
    }

    @Test
    @DisplayName("target_key와 contact가 중복되지 않으면 저장된다.")
    void shouldPersist_whenTargetKeyAndContactAreNotDuplicated() {
        SendTarget sendTarget = entityBuilder
                .contact("jang315@naver.com")
                .targetKey("test1").build();

        persist(sendTarget);

        assertThat(sendTarget).isNotNull();
    }
}
