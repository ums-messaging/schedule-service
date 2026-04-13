package com.ums.schedule.domain.request;

import com.ums.schedule.domain.request.exception.CustomerKeyRequiredException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequestKey {
    private String customerId;
    private String customerRequestId;

    public static CustomerRequestKey of(String customerId, String customerRequestId) {
        if(!hasCustomerKey(customerId, customerRequestId)) {
            throw CustomerKeyRequiredException.of();
        }
        return new CustomerRequestKey(customerId, customerRequestId);
    }

    private static boolean hasCustomerKey(String customerId, String requestId) {
        return StringUtils.hasText(customerId) && StringUtils.hasText(requestId);
    }

}
