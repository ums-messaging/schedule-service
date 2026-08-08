package com.ums.schedule.repository.mapping;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TargetUploadMappingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage);
        requestId = sendRequest.getId();
    }

    @Nested
    @DisplayName("send_request 연관관계 테스트")
    class SendRequestMappingTest {
        @Test
        @DisplayName("target_upload에서 send_request와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkSendRequest_whenSetByTargetUpload() {
            SendRequest request = entityManager.getReference(SendRequest.class, requestId);
            TargetUploadReport targetUpload = givenTargetUploadReport(request);
            UUID id = targetUpload.getId();
            entityManager.clear();

            TargetUploadReport targetUploadReport = entityManager.find(TargetUploadReport.class, id);
            assertThat(targetUploadReport.getSendRequest()).isNotNull();
        }
    }
}
