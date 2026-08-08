package com.ums.schedule.application.ums.email.generator.handler;

import com.ums.schedule.application.ums.email.config.SecurityMailProperties;
import com.ums.schedule.application.ums.email.generator.handler.model.EmailConvertContext;
import com.ums.schedule.application.ums.email.exception.SecurityMailNotConfiguredException;
import com.ums.schedule.common.code.api.SecurityMailErrorCode;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.common.code.email.ConvertType;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;

@Component
@RequiredArgsConstructor
public class PdfSecurityHandler implements EmailConvertHandler {
    private final PdfConvertHandler handler;
    private final SecurityMailProperties properties;

    @Override
    public boolean supports(ConvertType convertType, EmailType emailType) {
        return convertType == ConvertType.PDF && emailType == EmailType.SECURITY;
    }

    @Override
    public File handle(EmailConvertContext context) throws IOException {
        File file = handler.handle(context);
        InputStream inputStream = new FileInputStream(file);
        PDDocument document = PDDocument.load(inputStream);
        AccessPermission ap = new AccessPermission();

        PermissionMaskEnum permission = context.permissionMask();
        ap.setCanModify(permission.canModify());
        ap.setCanPrint(permission.canPrint());

        if(!StringUtils.hasText(properties.getOwnerPassword())) {
            throw SecurityMailNotConfiguredException.of(SecurityMailErrorCode.OWNER_PW_CONFIGURED_LOAD_FAILS);
        }
        StandardProtectionPolicy policy = new StandardProtectionPolicy(
                properties.getOwnerPassword(),   // 소유자 비밀번호
                context.userPassword(), ap);

        EncryptionTypeEnum encryption = context.encryptionType();
        policy.setEncryptionKeyLength(encryption.length()); // 128 or 256
        policy.setPermissions(ap);

        document.protect(policy);
        document.save(file);
        document.close();
        return file;
    }
}
