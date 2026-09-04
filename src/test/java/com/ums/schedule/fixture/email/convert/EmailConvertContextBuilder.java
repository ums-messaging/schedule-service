package com.ums.schedule.fixture.email.convert;

import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;

import java.nio.file.Path;

public class EmailConvertContextBuilder {

    private Path path;
    private String template;
    private String userPassword;
    private PermissionMaskEnum permissionMask;
    private EncryptionTypeEnum encryptionType;

    public static EmailConvertContextBuilder builder() {
        return new EmailConvertContextBuilder();
    }

    public EmailConvertContextBuilder path(Path path) {
        this.path = path;
        return this;
    }

    public EmailConvertContextBuilder template(String template) {
        this.template = template;
        return this;
    }

    public EmailConvertContextBuilder userPassword(String userPassword) {
        this.userPassword = userPassword;
        return this;
    }

    public EmailConvertContextBuilder permissionMask(PermissionMaskEnum permissionMask) {
        this.permissionMask = permissionMask;
        return this;
    }

    public EmailConvertContextBuilder encryptionType(EncryptionTypeEnum encryptionType) {
        this.encryptionType = encryptionType;
        return this;
    }

    public EmailConvertContext build() {
        return new EmailConvertContext(
                path,
                template,
                userPassword,
                permissionMask,
                encryptionType
        );
    }
}
