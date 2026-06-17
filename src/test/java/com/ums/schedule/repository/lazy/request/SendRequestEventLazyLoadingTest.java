package com.ums.schedule.repository.lazy.request;

import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.send.group.SendGroupEvent;
import com.ums.schedule.domain.send.group.SendGroupTestBuilder;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestEventLazyLoadingTest {
    @Autowired private EntityManager entityManager;
    private Long eventId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        SendGroupEvent event = SendGroupTestBuilder.builder().sendRequest(sendRequest).build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(event);

        entityManager.flush();

        this.eventId = event.getEventId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_request 조회 테스트")
    class SendRequestLazyLoadingTest {
        @Test
        @DisplayName("send_request_event 조회 시 send_request는 조회되지 않는다.")
        void shouldNotLoadSendRequest_whenFindSendRequestEvent() {
            SendGroupEvent event = entityManager.find(SendGroupEvent.class, eventId);

            assertThat(Hibernate.isInitialized(event.getSendRequest()))
                    .isFalse();
        }

        @Test
        @DisplayName("send_request_event 조회 시 send_request에 접근하면 쿼리가 실행된다.")
        void shouldLoadSendRequest_whenGetSendRequest() {
            SendGroupEvent event = entityManager.find(SendGroupEvent.class, eventId);

            event.getSendRequest().getChannelType();

            assertThat(Hibernate.isInitialized(event.getSendRequest()))
                    .isTrue();
        }

        @Test
        @DisplayName("트랜잭션 밖에서 Lazy 접근 시 예외가 발생한다.")
        void shouldThrowLazyInitializationException_whenGetSendRequestOutsideTransaction() {
            SendGroupEvent event = entityManager.find(SendGroupEvent.class, eventId);

            entityManager.clear();

            assertThatThrownBy(() -> event.getSendRequest().getChannelType())
                    .isInstanceOf(LazyInitializationException.class);
        }
    }

}
