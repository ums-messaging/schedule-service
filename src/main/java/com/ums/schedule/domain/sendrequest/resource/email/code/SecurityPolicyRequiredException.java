package com.ums.schedule.domain.sendrequest.resource.email.code;

public abstract class SecurityPolicyRequiredException extends AttachmentException {
    protected SecurityPolicyRequiredException(String message) {
        super(message);
    }
}
