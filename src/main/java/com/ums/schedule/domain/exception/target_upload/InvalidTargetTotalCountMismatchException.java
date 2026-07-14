package com.ums.schedule.domain.exception.target_upload;

import com.ums.schedule.domain.exception.target.SendTargetPolicyViolationException;

public class InvalidTargetTotalCountMismatchException extends SendTargetPolicyViolationException {
    protected InvalidTargetTotalCountMismatchException(String statistics) {
        super("Target count is mismatch. %s ".formatted(statistics));
    }

    public static InvalidTargetTotalCountMismatchException of(Long totalCount, Long successCount, Long failCount) {
        return new InvalidTargetTotalCountMismatchException("(total count : %d, success count : %d, fail count : %d)".formatted(totalCount, successCount, failCount));
    }
}
