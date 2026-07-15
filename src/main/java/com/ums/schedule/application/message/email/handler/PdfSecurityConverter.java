package com.ums.schedule.application.message.email.handler;

import com.ums.schedule.application.message.email.model.AttachmentPipelineCommand;
import com.ums.schedule.application.message.email.result.TemplateConversionResult;
import com.ums.schedule.application.exception.email.EmailMessageConvertException;
import com.ums.schedule.application.exception.email.security.OwnerPasswordNotConfiguredException;
import com.ums.schedule.application.ums.email.convert.handler.AttachmentConverter;
import com.ums.schedule.application.ums.email.convert.handler.PdfMessageConverter;
import com.ums.schedule.config.properties.SecurityPolicyProperties;
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
public class PdfSecurityConverter implements AttachmentConverter {
    private final PdfMessageConverter handler;
    private final SecurityPolicyProperties properties;

    public TemplateConversionResult handle(AttachmentPipelineCommand command) throws EmailMessageConvertException {
        TemplateConversionResult result = handler.handle(command);

        try (InputStream inputStream = new FileInputStream(result.tempFile())) {
            PDDocument document = PDDocument.load(inputStream);

            AccessPermission ap = new AccessPermission();
            ap.setCanModify(command.canModify());
            ap.setCanPrint(command.canPrint());

            if(!StringUtils.hasText(properties.getOwnerPassword())) {
                throw OwnerPasswordNotConfiguredException.of();
            }

            StandardProtectionPolicy policy =
                    new StandardProtectionPolicy(
                            properties.getOwnerPassword(),   // 소유자 비밀번호
                            command.userPassword(),    // 사용자 비밀번호
                            ap);

            policy.setEncryptionKeyLength(command.encryptionLength()); // 128 or 256
            policy.setPermissions(ap);

            document.protect(policy);
            document.save(result.tempFile());
            document.close();
        } catch (IOException e) {
            throw EmailMessageConvertException.of(e);
        }

        return result;
    }

    @Override
    public boolean supports(ConvertType convertType, boolean isSecurity) {
        return convertType == ConvertType.PDF && isSecurity == true;
    }
}
