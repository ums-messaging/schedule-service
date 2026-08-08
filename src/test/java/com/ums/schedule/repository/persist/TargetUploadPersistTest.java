package com.ums.schedule.repository.persist;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
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
public class TargetUploadPersistTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    private SendRequest sendRequest;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        sendRequest = givenSendRequest(schedule, sendMessage);
    }

    @Test
    @DisplayName("TARGET_UPLOAD_REPORT 저장 테스트")
    void shouldPersist_whenTargetUploadCreate() {
        TargetUploadReport targetUploadReport = TargetUploadReportEntityBuilder.builder().id(null)
                .id(null)
                .sendRequest(sendRequest)
                .build();

        persist(targetUploadReport);
        UUID id = targetUploadReport.getId();

        TargetUploadReport findTargetUploadReport = entityManager.find(TargetUploadReport.class, id);

        assertThat(findTargetUploadReport).isNotNull();
    }

    @Test
    @DisplayName("SEND_REQUEST가 NULL이면 예외가 발생한다.")
    void shouldThrowException_whenSendRequestIsNull() {
        TargetUploadReport targetUploadReport = TargetUploadReportEntityBuilder.builder().id(null)
                .sendRequest(null)
                .build();

        assertThatThrownBy(() -> persist(targetUploadReport))
                .isInstanceOf(PersistenceException.class);
    }
}