package com.ums.schedule.application.sendrequest.message.email.pipeline;

import com.ums.schedule.domain.sendrequest.resource.email.EmailAttachment;
import com.ums.schedule.domain.sendrequest.resource.email.policy.SecurityPolicy;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class SecurityEncryptionPipeline implements MessageGenerationPipeline {
    private final PdfConversionPipeline handler;

    public File handle(EmailAttachment message, String html, SendTarget target) throws IOException {
        File file = handler.handle(message, html, target);

        if(message.getSecurityPolicy() != null) {
            SecurityPolicy securityPolicy = message.getSecurityPolicy();
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
