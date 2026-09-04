package com.ums.schedule.repository.constraint.not_null;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ActiveProfiles("test")
@DataJpaTest
public class EmailTargetMessageNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    private TargetUploadReport targetUpload;
    private TargetMessage targetMessage;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        targetUpload = givenTargetUploadReport(sendRequest);
        targetMessage = EmailTargetMessageEntityBuilder.builder()
                .subject("subject")
                .bodyMessage("body message")
                .sendMessage(sendMessage)
                .build();
        initializeTargetMessage(targetMessage, 1L);
    }

    @Test
    @DisplayName("group_id는 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenGroupIdIsNotNull() {
        ReflectionTestUtils.setField(targetMessage, "groupId", null);

        assertThatThrownBy(() -> persist(targetMessage))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "GROUP_ID");
    }

    @Test
    @DisplayName("status는 NULL을 허용하지 않는다.")
    void shouldThrowException_whenStatusIsNull() {
        ReflectionTestUtils.setField(targetMessage, "state", null);

        assertThatThrownBy(() -> persist(targetMessage))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "STATUS");
    }
    private void initializeTargetMessage(TargetMessage targetMessage, Long id) {
        ReflectionTestUtils.setField(targetMessage, "id", id);
        ReflectionTestUtils.setField(targetMessage, "groupId", TsidCreator.getTsid().toLong());
        ReflectionTestUtils.setField(targetMessage, "targetUploadReport", targetUpload);
        ReflectionTestUtils.setField(targetMessage, "state", new SendTargetCreateState());
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang314");
        ReflectionTestUtils.setField(targetMessage, "contact", "jang314@test.com");
    }
}