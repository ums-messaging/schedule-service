package com.ums.schedule.attachment.exception;

public class PasswordPolicyRequiredException extends SecurityPolicyRequiredException {

    private PasswordPolicyRequiredException(String message) {
        super(message);
    }

    public static PasswordPolicyRequiredException ofPasswordPolicy() {
        return new PasswordPolicyRequiredException("password policy ");
    }
}
