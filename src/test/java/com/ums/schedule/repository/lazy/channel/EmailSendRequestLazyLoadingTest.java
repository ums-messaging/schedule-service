package com.ums.schedule.repository.lazy.channel;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachmentBuilder;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailSendRequestLazyLoadingTest {
    @Autowired private EntityManager entityManager;
    private Long requestId;
    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        EmailAttachment emailSendRequest = EmailAttachmentBuilder.builder().sendRequest(sendRequest).build();

        entityManager.persist(schedule);
        entityManager.persist(emailSendRequest);
        entityManager.flush();

        requestId = sendRequest.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_request 조회 테스트")
    class SendRequestLazyLoadingTest {
//        @Test
//        @DisplayName("email_send_request 조회 시 send_request는 조회되지 않는다.")
//        void shouldNotLoadSendRequest_whenFindSendRequest() {
//            EmailAttachment emailSendRequest = entityManager.find(EmailAttachment.class, requestId);
//
//            assertThat(Hibernate.isInitialized(emailSendRequest.getSendRequest()))
//                    .isFalse();
//        }
//
//        @Test
//        @DisplayName("email_send_request 조회 시 send_request에 접근하면 쿼리가 실행된다.")
//        void shouldLoadSendRequest_whenGetSendRequest() {
//            EmailAttachment emailSendRequest = entityManager.find(EmailAttachment.class, requestId);
//
//            emailSendRequest.getSendRequest().getRetryCnt();
//
//            assertThat(Hibernate.isInitialized(emailSendRequest.getSendRequest()))
//                    .isTrue();
//        }
//
//        @Test
//        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
//        void shouldThrowLazyInitializationException_whenGetSendRequestOutsideTransaction() {
//            EmailAttachment emailSendRequest = entityManager.find(EmailAttachment.class, requestId);
//
//            entityManager.clear();
//
//            assertThatThrownBy(() -> emailSendRequest.getSendRequest().getRetryCnt())
//                    .isInstanceOf(LazyInitializationException.class);
//
//        }
    }
}
