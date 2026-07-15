package com.ums.schedule.repository.constraint.request;

import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class SendRequestUniqueConstraintTest {
    @Autowired private EntityManager entityManager;
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();
    private Long scheduleId ;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule)
                .customerRequestKey("test1", "test2").build();

        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.flush();

        scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("customer_id와 customer_request_id가 중복되면 예외가 발생한다.")
    void shouldThrowException_whenCustomerIdAndCustomerRequestIdIsDuplicated() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule)
                .customerRequestKey("test1", "test2")
                .build();

        assertThatThrownBy(() -> {
            entityManager.persist(sendRequest);
            entityManager.flush();

        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_CUSTOMER_REQUEST");
    }

    @Test
    @DisplayName("customer_id와 중복되지 않은 customer_request_id를 입력하면 저장된다.")
    void shouldPersist_whenCustomerRequestIdIsNotDuplicatedAboutCustomerId() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest sendRequest = SendRequestEntityBuilder.builder().schedule(schedule)
                .customerRequestKey("test1", "test3")
                .build();

        entityManager.persist(sendRequest);
        entityManager.flush();

        Long requestId = sendRequest.getId();
        entityManager.clear();

        SendRequest expect = entityManager.find(SendRequest.class, requestId);

        assertThat(expect).isNotNull();
    }
}
