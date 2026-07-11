package com.ums.schedule.domain.message.exception;


public abstract class SecurityMailViolationPolicyException extends AttachmentException{
    protected SecurityMailViolationPolicyException(String message) {
        super(message);
    }

}
