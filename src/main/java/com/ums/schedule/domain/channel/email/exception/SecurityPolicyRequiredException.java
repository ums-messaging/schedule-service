package com.ums.schedule.domain.channel.email.exception;

public abstract class SecurityPolicyRequiredException extends RequiredException {
    protected SecurityPolicyRequiredException(String message) {
        super(message);
    }
}
