package com.ums.schedule.repository.mapping.channel;

import com.ums.schedule.domain.message.email.attachment.EmailAttachment;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class EmailSendRequestMappingTest {
    @Autowired
    private EntityManager entityManager;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();

        scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_request 연관관계 테스트")
    class SendRequestMappingTest {
        @Test
        @DisplayName("send_request에서 연관관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistSendRequestFK_whenSetByInverseOnlySide() {
            Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
            SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();

            EmailAttachmentBuilder.builder().sendRequest(sendRequest).build();

            entityManager.persist(sendRequest);
            entityManager.flush();

            Long id = sendRequest.getId();
            entityManager.clear();

            EmailAttachment expect = entityManager.find(EmailAttachment.class, id);
            assertThat(expect).isNull();
        }

        @Test
        @DisplayName("email_send_request에서 send_request와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkSendRequest_whenSetByEmailSendRequest() {
            Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
            SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();

            EmailAttachment emailSendRequest = EmailAttachmentBuilder.builder().sendRequest(sendRequest).build();

            entityManager.persist(emailSendRequest);
            entityManager.flush();

            Long id = sendRequest.getId();
            entityManager.clear();

            EmailAttachment expect = entityManager.find(EmailAttachment.class, id);
            assertThat(expect.getId()).isNotNull();
        }

        @Test
        @DisplayName("FK인 send_request은 NULL을 허용하지 않는다.")
        void shouldThrowException_whenSendRequestIsNull() {
            EmailAttachment emailSendRequest = EmailAttachmentBuilder.builder()
                    .sendRequest(null).build();

            assertThatThrownBy(() -> {
                entityManager.persist(emailSendRequest);
                entityManager.flush();
            }).isInstanceOf(IdentifierGenerationException.class);
        }
    }
}
