package com.ums.schedule.application.channel.email.converter.handler;

import com.ums.schedule.domain.channel.email.security.SecurityPolicy;
import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class SecurityPolicyHandler implements EmailBodyConvertHandler {
    private final EmailBodyConvertHandler handler;

    public File handle(AttachmentDto attachment, EmailBody body, SendTarget target) throws IOException {
        File file = handler.handle(attachment, body, target);
        if(body.hasSecurityPolicy()) {
            SecurityPolicy securityPolicy = body.getSecurityPolicy();

            ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
            InputStream inputStream = new FileInputStream(file);
            PDDocument document = PDDocument.load(inputStream);

            AccessPermission ap = new AccessPermission();
            ap.setCanModify(securityPolicy.hasModifyAuth());
            ap.setCanPrint(securityPolicy.hasPrintAuth());

            StandardProtectionPolicy policy =
                    new StandardProtectionPolicy(
                            "owner-password",   // 소유자 비밀번호
                            securityPolicy.getTargetPassword(target),    // 사용자 비밀번호
                            ap);

            policy.setEncryptionKeyLength(securityPolicy.getEncryptionLength()); // 128 or 256
            policy.setPermissions(ap);

            document.protect(policy);
            document.save(encryptedOut);
            document.close();
        }
        return file;
    }
}
