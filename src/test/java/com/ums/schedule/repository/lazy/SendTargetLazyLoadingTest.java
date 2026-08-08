package com.ums.schedule.repository.lazy;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.fixture.entity.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class SendTargetLazyLoadingTest extends EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;
    private UUID targetId;

    private SendTargetEntityBuilder targetEntityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        EmailSendMessage sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        TargetUploadReport targetUpload = givenTargetUploadReport(sendRequest);
        SendTarget target = SendTargetEntityBuilder.builder()
                .targetUpload(targetUpload)
                .targetMessage(EmailTargetMessageEntityBuilder.builder()
                        .sendMessage(sendMessage)
                        .build()).build();

        persist(target);
        targetId = target.getId();
        entityManager.clear();
    }


    @Nested
    @DisplayName("target_message 조회 테스트")
    class WhenTargetMessage {
        @Test
        @DisplayName("send_target 조회 시 target_message는 조회되지 않는다.")
        void shouldNotLoadTargetUpload_whenFindSendTarget() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            assertThat(Hibernate.isInitialized(sendTarget.getTargetMessage())).isFalse();
        }

        @Test
        @DisplayName("send_target 조회 시 target_message에 접근하면 쿼리가 실행된다.")
        void shouldLoadTargetUpload_whenGetTargetUpload() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            assertThat(Hibernate.isInitialized(sendTarget.getTargetMessage().getMessageId())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetTargetUploadOutsideTransaction() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            entityManager.clear();

            assertThatThrownBy(() -> sendTarget.getTargetMessage().getSendTarget())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }

    @Nested
    @DisplayName("target_upload 조회 테스트")
    class TargetUploadLazyLoadingTest {
        @Test
        @DisplayName("send_target 조회 시 target_upload는 조회되지 않는다.")
        void shouldNotLoadTargetUpload_whenFindSendTarget() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            assertThat(Hibernate.isInitialized(sendTarget.getTargetUpload())).isFalse();
        }

        @Test
        @DisplayName("send_target 조회 시 target_upload에 접근하면 쿼리가 실행된다.")
        void shouldLoadTargetUpload_whenGetTargetUpload() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            sendTarget.getTargetUpload().getState();

            assertThat(Hibernate.isInitialized(sendTarget.getTargetUpload())).isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetTargetUploadOutsideTransaction() {
            SendTarget sendTarget = entityManager.find(SendTarget.class, targetId);

            entityManager.clear();

            assertThatThrownBy(() -> sendTarget.getTargetUpload().getState())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }
}
