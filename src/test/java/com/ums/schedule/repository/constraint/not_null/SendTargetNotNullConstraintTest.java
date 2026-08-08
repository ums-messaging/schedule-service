package com.ums.schedule.repository.constraint.not_null;

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


import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class SendTargetNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    private SendTargetEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage);
        TargetUploadReport targetUpload = givenTargetUploadReport(sendRequest);

        entityBuilder = SendTargetEntityBuilder.builder()
                .targetUpload(targetUpload);
    }

    @Test
    @DisplayName("status는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenStatusIsNull() {
        SendTarget sendTarget = entityBuilder.state(null).build();

        assertThatThrownBy(() -> persist(sendTarget))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "STATUS");
    }
}