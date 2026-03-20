package com.ums.schedule.attachment.exception;

public abstract class SecurityPolicyRequiredException extends RequiredException {
    protected SecurityPolicyRequiredException(String message) {
        super(message);
    }
}
