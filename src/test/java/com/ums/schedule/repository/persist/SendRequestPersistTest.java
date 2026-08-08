package com.ums.schedule.repository.persist;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.repository.EntityJpaTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestPersistTest extends EntityJpaTestSupport {
    @Autowired
    private EntityManager entityManager;

    private Schedule schedule;
    private SendMessage sendMessage;

    @BeforeEach
    void setUp() {
        schedule = givenSchedule();
        sendMessage = givenSendMessage();
    }

    @Test
    @DisplayName("SEND_REQUEST 등록 테스트")
    void shouldPersistSendRequest_whenCreatingSendRequest() {
        SendRequest sendRequest = SendRequestEntityBuilder.builder()
                .schedule(schedule)
                .sendMessage(sendMessage)
                .build();

        persist(sendRequest);

        Long requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest expect = entityManager.find(SendRequest.class, requestId);
        assertThat(expect.getId()).isNotNull();
    }

    @Nested
    @DisplayName("NOT NULL 테스트")
    class WhenNotNull {
        @Test
        @DisplayName("Schedule이 NULL이면 예외가 발생한다.")
        void shouldThrowException_whenScheduleIsNull() {
            SendRequest sendRequest = SendRequestEntityBuilder.builder()
                    .schedule(null)
                    .sendMessage(sendMessage)
                    .build();

            assertThatThrownBy(() -> {
                persist(sendRequest);
            }).isInstanceOf(PersistenceException.class)
            ;
        }

        @Test
        @DisplayName("SendMessage가 NULL이면 예외가 발생한다.")
        void shouldThrowException_whenSendMessageIsNull() {
            SendRequest sendRequest = SendRequestEntityBuilder.builder()
                    .schedule(schedule)
                    .sendMessage(null)
                    .build();

            assertThatThrownBy(() -> {
                persist(sendRequest);
            }).isInstanceOf(PersistenceException.class);
        }
    }
}
