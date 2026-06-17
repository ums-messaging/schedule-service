package com.ums.schedule.domain.sendrequest.target.upload.exception;

import com.ums.schedule.common.exception.PolicyViolationException;

public class InvalidTargetTotalCountMismatchException extends PolicyViolationException {
    protected InvalidTargetTotalCountMismatchException(String statistics) {
        super("Target count is mismatch. %s ".formatted(statistics));
    }

    public static InvalidTargetTotalCountMismatchException of(Long totalCount, Long successCount, Long failCount) {
        return new InvalidTargetTotalCountMismatchException("(total count : %d, success count : %d, fail count : %d)".formatted(totalCount, successCount, failCount));
    }
}
