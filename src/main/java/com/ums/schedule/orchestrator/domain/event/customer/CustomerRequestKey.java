package com.ums.schedule.orchestrator.domain.event.customer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequestKey {
    private String customerId;
    private String customerkey;

    public static CustomerRequestKey of(String customerId, String customerRequestId) {
        return new CustomerRequestKey(customerId, customerRequestId);
    }
}
