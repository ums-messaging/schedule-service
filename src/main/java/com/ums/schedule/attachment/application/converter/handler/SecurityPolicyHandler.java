package com.ums.schedule.attachment.application.converter.handler;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.attachment.code.EncryptionTypeEnum;
import com.ums.schedule.attachment.code.PasswordHashEnum;
import com.ums.schedule.attachment.code.PermissionMaskEnum;
import com.ums.schedule.common.code.EnumMapper;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperType;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.send.code.TargetColumnEnum;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.*;

@Component
@RequiredArgsConstructor
public class SecurityPolicyHandler implements EmailBodyConvertHandler {
    private final EmailBodyConvertHandler handler;
    private final EnumMapperFactory factory;

    public File handle(AttachmentDto attachment, EmailBody body, EmailSendTarget target) throws IOException {
        File file = handler.handle(attachment, body, target);
        if(body.getSecurityPolicy() != null) {
            ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
            InputStream inputStream = new FileInputStream(file);
            PDDocument document = PDDocument.load(inputStream);

            AccessPermission ap = new AccessPermission();
            ap.setCanModify(true);
            ap.setCanPrint(true);

            StandardProtectionPolicy policy =
                    new StandardProtectionPolicy(
                            "owner-password",   // 소유자 비밀번호
                            (String) target.getDataParam().get(TargetColumnEnum.TARGET_BIRTHDAY),    // 사용자 비밀번호
                            ap);

            policy.setEncryptionKeyLength(128); // 128 or 256
            policy.setPermissions(ap);

            document.protect(policy);
            document.save(encryptedOut);
            document.close();
        }
        return file;
    }

    private Map<AttachmentEnumMapper, EnumMapperValue> toEnumMapperValue(SecurityPolicyCommand secuCmd) {
        return Map.of(
                ENCRYPTION_TYPE, resolveEnumMapperValue(ENCRYPTION_TYPE, secuCmd.encryptionType(), EncryptionTypeEnum.ASE256),
                PASSWORD_HASH, resolveEnumMapperValue(PASSWORD_HASH, secuCmd.passwordHash(), PasswordHashEnum.SHA256),
                PERMISSION_MASK, resolveEnumMapperValue(PERMISSION_MASK, secuCmd.permissionMask(), PermissionMaskEnum.NONE)
        );
    }

    private EnumMapperValue resolveEnumMapperValue(EnumMapper key, String code, EnumMapperType defaultValue) {
        return Optional.ofNullable(code)
                .filter(cd -> StringUtils.hasText(cd))
                .map(cd -> factory.findEnumMapperValue(key, cd))
                .orElse(EnumMapperValue.fromEnumMapperType(defaultValue));
    }



}
