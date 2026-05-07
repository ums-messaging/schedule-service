package com.ums.schedule.domain.request;

import com.ums.schedule.domain.request.exception.customer_key.CustomerKeyRequiredException;
import com.ums.schedule.domain.request.exception.customer_key.CustomerKeyDuplicatedException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerRequestKey {
    private String customerId;
    private String customerRequestId;

    public static CustomerRequestKey of(String customerId, String customerRequestId, boolean exists) {
        validate(customerId, customerRequestId, exists);
        return new CustomerRequestKey(customerId, customerRequestId);
    }

    private static void validate(String customerId, String customerRequestId, boolean exists) {
        validate(exists);
        if(!hasCustomerKey(customerId, customerRequestId)) {
            throw CustomerKeyRequiredException.of();
        }
    }

    private static void validate(boolean exists) {
        if(exists) {
            throw CustomerKeyDuplicatedException.of();
        }
    }

    private static boolean hasCustomerKey(String customerId, String requestId) {
        return StringUtils.hasText(customerId) && StringUtils.hasText(requestId);
    }
}
