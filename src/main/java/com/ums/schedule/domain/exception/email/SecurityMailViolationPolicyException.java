package com.ums.schedule.domain.exception.email;


public abstract class SecurityMailViolationPolicyException extends AttachmentException{
    protected SecurityMailViolationPolicyException(String message) {
        super(message);
    }

}
