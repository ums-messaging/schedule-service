package com.ums.schedule.domain.sendrequest.customer;

import com.ums.schedule.common.exception.validation.DuplicateViolationException;
import com.ums.schedule.common.exception.validation.RequiredException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRequestKey {
    private String customerId;
    private String customerRequestId;

    public static CustomerRequestKey of(String customerId, String customerRequestId) {
        validateCustomerId(customerId);
        validateCustomerRequestKey(customerRequestId);
        return new CustomerRequestKey(customerId, customerRequestId);
    }


    private static void validateCustomerRequestKey(String customerRequestId) {
        if(!StringUtils.hasText(customerRequestId)) {
            throw RequiredException.fieldOf("customer request id");
        }
    }

    private static void validateCustomerId(String customerId) {
        if(!StringUtils.hasText(customerId)) {
            throw RequiredException.fieldOf("customer id");
        }
    }

    public void validateDuplicateKey(boolean exists) {
        if(exists) {
            throw DuplicateViolationException.fieldOf("customer key");
        }
    }
}
