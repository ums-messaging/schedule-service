package com.ums.schedule.domain.request.customer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequestKey {
    private String customerId;
    private String customerRequestId;

    public static CustomerRequestKey of(String customerId, String customerRequestId) {
        return new CustomerRequestKey(customerId, customerRequestId);
    }
}
