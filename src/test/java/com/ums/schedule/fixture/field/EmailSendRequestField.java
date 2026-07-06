package com.ums.schedule.fixture.field;

public enum EmailSendRequestField {
    MAIL_FROM("mailFrom"),
    MAIL_FROM_NAME("mailFromName"),
    EMAIL_TEMPLATE_KEY("emailTemplateKey"),
    EMAIL_BODY("body"),
    CONVERT_TYPE("convertType"),
    SECURITY_POLICY("securityPolicy"),
    ENCRYPTION_TYPE("encryptionType"),
    PASSWORD_HASH("passwordHash"),
    PERMISSION_MASK("permissionMask"),
    PASSWORD_POLICY("passwordPolicy"),
    PASSWORD_FORMAT("passwordFormat"),
    ;

    ;

    String field;

    EmailSendRequestField(String field) {
        this.field = field;
    }

    public String value() {
        return this.field;
    }
}
