package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
public class SendRequestMappingTest extends EntityJpaTestSupport {
    private SendRequestEntityBuilder entityBuilder;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();

        entityBuilder = SendRequestEntityBuilder.builder()
                .schedule(schedule)
                .sendMessage(sendMessage);
    }

    @Test
    @DisplayName("send_request에서 schedule과 연관관계를 설정하면 FK가 저장된다.")
    void shouldPersistFkSchedule_whenSetBySendRequest() {
        Long requestId = persistSendRequestGetRequestId();

        SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

        assertThat(findSendRequest.getSchedule()).isNotNull();
    }

    @Nested
    @DisplayName("target_upload 연관관계 테스트")
    class TargetUploadMappingTest {
        @Test
        @DisplayName("send_request에서 current_target_upload와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkCurrentTargetUpload_whenSetBySendRequest() {
            Long requestId = persistSendRequestGetRequestId();
            persistAndFindTargetUploadReport(requestId);

            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

            assertThat(findSendRequest.getCurrentTargetUpload()).isNotNull();
        }

        private TargetUploadReport persistAndFindTargetUploadReport(Long requestId) {
            SendRequest sendRequest = entityManager.find(SendRequest.class, requestId);
            TargetUploadReport targetUploadReport = TargetUploadReportEntityBuilder.builder()
                    .sendRequest(sendRequest)
                    .build();

            persist(targetUploadReport);
            UUID id = targetUploadReport.getId();
            entityManager.clear();

            SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);
            TargetUploadReport findTargetUpload = entityManager.find(TargetUploadReport.class, id);
            findSendRequest.assignTargetUpload(findTargetUpload);

            entityManager.flush();
            entityManager.clear();
            return targetUploadReport;
        }
    }

    @Test
    @DisplayName("send_request에서 send_message와 연관관계를 설정하면 FK가 저장된다.")
    void shouldPersistFkSendMessage_whenSetBySendRequest() {
        Long requestId = persistSendRequestGetRequestId();

        SendRequest findSendRequest = entityManager.find(SendRequest.class, requestId);

        assertThat(findSendRequest.getSendMessage()).isNotNull();
    }

    private Long persistSendRequestGetRequestId() {
        SendRequest sendRequest = entityBuilder.build();
        persist(sendRequest);
        Long requestId = sendRequest.getId();
        entityManager.clear();
        return requestId;
    }
}
