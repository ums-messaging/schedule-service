package com.ums.schedule.repository.mapping.request;

import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.send.group.SendGroupTestBuilder;
import com.ums.schedule.domain.sendrequest.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestEventMappingTest {
    @Autowired
    private EntityManager entityManager;
    private Long requestId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule).build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.flush();

        requestId = sendRequest.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_request 연관관계 테스트")
    class SendRequestMappingTest {
        @Test
        @DisplayName("send_request에서 연관관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistSendRequestFK_whenSetByInverseOnlySide() {
            SendRequest request = entityManager.getReference(SendRequest.class, requestId);

            SendGroupEvent event = SendGroupTestBuilder.builder()
                    .sendRequest(request)
                    .build();

            entityManager.persist(request);
            entityManager.flush();
            entityManager.clear();

            List<SendGroupEvent> expect = entityManager.createQuery("select s from SendGroupEvent s where s.sendRequest.id = :id", SendGroupEvent.class)
                    .setParameter("id", requestId)
                    .getResultList();

            assertThat(expect).hasSize(0);
        }

        @Test
        @DisplayName("send_request_event에서 send_request와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkSendRequest_whenSetBySendRequestEvent() {
            SendRequest request = entityManager.getReference(SendRequest.class, requestId);

            SendGroupEvent event = SendGroupTestBuilder.builder()
                    .sendRequest(request)
                    .build();

            entityManager.persist(event);
            entityManager.flush();
            entityManager.clear();


            List<SendGroupEvent> expect = entityManager
                    .createQuery("select s from SendGroupEvent s where s.sendRequest.id = :id", SendGroupEvent.class)
                    .setParameter("id", requestId)
                    .getResultList();

            assertThat(expect).hasSize(1);
        }

        @Test
        @DisplayName("FK인 send_request은 NULL을 허용하지 않는다.")
        void shouldThrowException_whenSendRequestIsNull() {
            SendGroupEvent event =
                    SendGroupTestBuilder.builder()
                            .sendRequest(null)
                            .build();

            assertThatThrownBy(() -> {
                entityManager.persist(event);
                entityManager.flush();
            }).isInstanceOf(ConstraintViolationException.class);
        }
    }
}
