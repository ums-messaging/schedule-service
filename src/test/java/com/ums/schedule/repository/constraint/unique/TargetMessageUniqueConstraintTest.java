package com.ums.schedule.repository.constraint.unique;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class TargetMessageUniqueConstraintTest extends EntityJpaTestSupport {
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();
    private EmailSendMessage sendMessage;
    private TargetUploadReport targetUpload;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        targetUpload = givenTargetUploadReport(sendRequest);

        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                .subject("subject")
                .sendMessage(sendMessage)
                .bodyMessage("body message")
                .build();
        initializeTargetMessage(targetMessage, 1L);
        ReflectionTestUtils.setField(targetMessage, "contact", "jang314@test.com");
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang314");
        persist(targetMessage);
    }

    @Test
    @DisplayName("upload_id와 contact가 중복되면 익셉션이 발생한다.")
    void shouldThrowException_whenUploadIdAndContactAreDuplicated(){
        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                .subject("subject")
                .sendMessage(sendMessage)
                .bodyMessage("body message")
                .build();
        ReflectionTestUtils.setField(targetMessage, "contact", "jang314@test.com");
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang315");

        initializeTargetMessage(targetMessage, 2L);

        assertThatThrownBy(() -> persist(targetMessage))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_CONTACT");
    }

    @Test
    @DisplayName("upload_id와 target_key가 중복되면 익셉션이 발생한다.")
    void shouldThrowException_whenUploadIdAndTargetKeyAreDuplicated() {

        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                .subject("subject")
                .sendMessage(sendMessage)
                .bodyMessage("body message")
                .build();

        initializeTargetMessage(targetMessage, 2L);
        ReflectionTestUtils.setField(targetMessage, "contact", "jang315@test.com");
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang314");

        assertThatThrownBy(() -> persist(targetMessage))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_TARGET_KEY");
    }

    @Test
    @DisplayName("target_key와 contact가 중복되지 않으면 저장된다.")
    void shouldPersist_whenTargetKeyAndContactAreNotDuplicated() {

        TargetMessage targetMessage = EmailTargetMessageEntityBuilder.builder()
                .subject("subject")
                .sendMessage(sendMessage)
                .bodyMessage("body message")
                .build();
        ReflectionTestUtils.setField(targetMessage, "contact", "jang315@test.com");
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang315");
        initializeTargetMessage(targetMessage, 2L);

        persist(targetMessage);

        assertThat(targetMessage).isNotNull();
    }

    private void initializeTargetMessage(TargetMessage targetMessage, Long id) {
        ReflectionTestUtils.setField(targetMessage, "id", id);
        ReflectionTestUtils.setField(targetMessage, "groupId", TsidCreator.getTsid().toLong());
        ReflectionTestUtils.setField(targetMessage, "targetUploadReport", targetUpload);
        ReflectionTestUtils.setField(targetMessage, "state", new SendTargetCreateState());
    }
}
