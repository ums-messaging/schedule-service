package com.ums.schedule.application.ums.email.generator.handler.model;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import freemarker.template.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public record EmailConvertContext(
        Path path,
        String template,
        String userPassword,
        PermissionMaskEnum permissionMask,
        EncryptionTypeEnum encryptionType
) {
    public static EmailConvertContext of(EmailTemplate template, String bodyTemplate, TargetMessageData targetData) throws IOException {
        Path path = Files.createTempFile(template.getConvertType().value(), targetData.targetKey());

        return Optional.ofNullable(template.getEmailType())
                .filter(v ->  v == EmailType.SECURITY)
                .map(v -> EmailConvertContext.of(path, template.getSecurityMail(), bodyTemplate, targetData))
                .orElseGet(() -> EmailConvertContext.of(path, bodyTemplate));
    }

    private static EmailConvertContext of(Path path, SecurityMail securityMail, String  template, TargetMessageData targetData) {
        return Optional.ofNullable(securityMail)
                .map(v -> new EmailConvertContext(
                        path,
                        template,
                        targetData.getString(v.passwordPolicy()),
                        PermissionMaskEnum.valueOf(v.permissionMask().code()),
                        EncryptionTypeEnum.valueOf(v.encryptionType().code())
                ))
                .orElseThrow();
    }

    private static EmailConvertContext of(Path path, String template) {
        return new EmailConvertContext(path,
                template,
                null,
                null,
                null
        );
    }

    public String fileName() {
        return path.getFileName().toString();
    }
}
