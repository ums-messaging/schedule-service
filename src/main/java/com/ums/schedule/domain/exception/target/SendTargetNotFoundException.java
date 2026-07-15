package com.ums.schedule.domain.exception.target;

import com.ums.schedule.domain.exception.ResourceNotFoundException;

public class SendTargetNotFoundException extends ResourceNotFoundException {
    protected SendTargetNotFoundException(String resource) {
        super(resource);
    }

    public static SendTargetNotFoundException listOf(Long uploadId) {
        return new SendTargetNotFoundException(String.format("[%d] Target List ", uploadId));
    }

    public static SendTargetNotFoundException of(Long id) {
        return new SendTargetNotFoundException(String.format("[%d] Send Target", id));
    }
}
