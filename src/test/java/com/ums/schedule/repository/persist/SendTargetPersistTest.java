package com.ums.schedule.repository.persist;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.state.SendTargetCreateState;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class SendTargetPersistTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private TargetUploadReport targetUploadReport;


    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage);

        this.targetUploadReport = givenTargetUploadReport(sendRequest);
    }

    @Test
    @DisplayName("send_target 생성 시 저장된다.")
    void shouldPersist_whenSendTargetCreate() {
        SendTarget sendTarget = SendTargetEntityBuilder.builder()
                .targetUpload(targetUploadReport)
                .state(new SendTargetCreateState())
                .build();

        entityManager.persist(sendTarget);
        entityManager.flush();
        UUID id = sendTarget.getId();
        entityManager.clear();

        SendTarget findSendTarget = entityManager.find(SendTarget.class, id);

        assertThat(findSendTarget).isNotNull();
    }

    @Test
    @DisplayName("TARGET_UPLOAD_REPORT가 NULL이면 예외가 발생한다.")
    void shouldThrowException_whenTargetUploadReportIsNull() {
        SendTarget sendTarget = SendTargetEntityBuilder.builder()
                .targetUpload(null)
                .state(new SendTargetCreateState())
                .build();

        assertThatThrownBy(() -> persist(sendTarget)).isInstanceOf(PersistenceException.class);
    }
}