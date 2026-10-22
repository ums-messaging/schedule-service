package com.ums.schedule.email.application.message.handler;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.ums.schedule.common.code.email.security.EncryptionTypeEnum;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PdfConvertHandler implements EmailConvertHandler {
    private final HtmlConvertHandler handler;

    @Override
    public boolean supports(ConvertType convertType, EncryptionTypeEnum encryptionType) {
        return convertType == ConvertType.PDF && encryptionType == EncryptionTypeEnum.NONE;
    }

    @Override
    public Path handle(MimeAttachFile attachFile, Map<String, Object> dataParam) throws Exception {
        Path pdfPath =  Files.createTempFile("email-", ".pdf");
        Path htmlPath = handler.handle(attachFile, dataParam);
        try (
                InputStream inputStream = Files.newInputStream(htmlPath);
                OutputStream outputStream = Files.newOutputStream(pdfPath);
        ) {
            String html = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } finally {
            Files.deleteIfExists(htmlPath);
        }
        return pdfPath;
    }
}