package com.ums.schedule.domain.request;

import com.ums.schedule.domain.request.exception.customer_key.CustomerKeyRequiredException;
import com.ums.schedule.domain.request.exception.customer_key.CustomerKeyDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerRequestKeyTest {
    @Test
    @DisplayName("CustomerRequestKey가 존재하면 익셉션이 발생한다.")
    void shouldThrowException_whenCustomerRequestKeyExists() {
        boolean exists = true;

        CustomerKeyDuplicatedException expect = CustomerKeyDuplicatedException.of();

        assertThatThrownBy(() -> CustomerRequestKey.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), exists))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());

    }

    @Test
    @DisplayName("CustomerRequestId가 빈 값이거나 Null이면 익셉션이 발생한다.")
    void shouldThrowException_whenCustomerRequestIdIsNullNotEmpty() {
        CustomerKeyRequiredException expect = CustomerKeyRequiredException.of();
        assertThatThrownBy(() -> CustomerRequestKey.of(UUID.randomUUID().toString(), " ", false))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

    @Test
    @DisplayName("CustomerId가 빈 값이거나 Null이면 익셉션이 발생한다.")
    void shouldThrowException_whenCustomerIdIsNullNotEmpty() {
        CustomerKeyRequiredException expect = CustomerKeyRequiredException.of();
        assertThatThrownBy(() -> CustomerRequestKey.of(" ", UUID.randomUUID().toString(), false))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }

}