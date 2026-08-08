package com.ums.schedule.repository.constraint.unique;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class SendRequestUniqueConstraintTest extends EntityJpaTestSupport {
    private final String ERROR_MESSAGE = DbErrorMessage.UNIQUE_CONSTRAINT.getMessage();
    private SendRequestEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        entityBuilder = SendRequestEntityBuilder.builder().schedule(schedule)
                .sendMessage(sendMessage);

        SendRequest sendRequest = entityBuilder
                .customerRequestKey("test1", "test2").build();
        persist(sendRequest);
    }

    @Test
    @DisplayName("customer_id와 customer_request_id가 중복되면 예외가 발생한다.")
    void shouldThrowException_whenCustomerIdAndCustomerRequestIdIsDuplicated() {
        SendRequest sendRequest = entityBuilder.customerRequestKey("test1", "test2")
                .build();

        assertThatThrownBy(() -> persist(sendRequest))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UQ_CUSTOMER_REQUEST");
    }

    @Test
    @DisplayName("customer_id와 중복되지 않은 customer_request_id를 입력하면 저장된다.")
    void shouldPersist_whenCustomerRequestIdIsNotDuplicatedAboutCustomerId() {
        SendRequest sendRequest = entityBuilder
                .customerRequestKey("test1", "test3")
                .build();

        SendRequest findSendRequest = persist(sendRequest);

        assertThat(findSendRequest).isNotNull();
    }
}
