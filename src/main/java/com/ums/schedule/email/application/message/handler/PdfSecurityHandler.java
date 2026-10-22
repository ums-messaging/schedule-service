package com.ums.schedule.email.application.message.handler;

import com.ums.schedule.config.properties.SecurityMailProperties;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.security.PermissionMaskEnum;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PdfSecurityHandler implements EmailConvertHandler {
    private final PdfConvertHandler handler;
    private final SecurityMailProperties properties;

    @Override
    public boolean supports(ConvertType convertType, EncryptionTypeEnum encryptionType) {
        return convertType == ConvertType.PDF && encryptionType != EncryptionTypeEnum.NONE;
    }

    @Override
    public Path handle(MimeAttachFile attachFile, Map<String, Object> targetData) throws Exception {
        AccessPermission ap = new AccessPermission();

        PermissionMaskEnum permission = attachFile.message().permissionMask();
        ap.setCanModify(permission.canModify());
        ap.setCanPrint(permission.canPrint());

        String passwordType = attachFile.message().passwordType().code();
        StandardProtectionPolicy policy = new StandardProtectionPolicy(
                properties.getOwnerPassword(),   // 소유자 비밀번호
                String.valueOf(targetData.get(passwordType)), ap);

        EncryptionTypeEnum encryption = attachFile.message().encryptionType();
        policy.setEncryptionKeyLength(encryption.length()); // 128 or 256
        policy.setPermissions(ap);

        Path encryptPath = Files.createTempFile("email-", ".pdf");
        Path pdfPath = handler.handle(attachFile, targetData);

        try ( InputStream is = Files.newInputStream(pdfPath);
              PDDocument document = PDDocument.load(is) ) {
            document.protect(policy);
            document.save(encryptPath.toFile());
        } finally {
            Files.deleteIfExists(pdfPath);
        }
        return encryptPath;
    }
}
