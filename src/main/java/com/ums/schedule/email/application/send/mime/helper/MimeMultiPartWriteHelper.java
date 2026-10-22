package com.ums.schedule.email.application.send.mime.helper;

import com.ums.schedule.email.application.exception.EmailMessageConvertException;
import com.ums.schedule.email.application.message.handler.EmailConvertHandler;
import com.ums.schedule.email.application.message.generator.policy.model.AttachMessage;
import com.ums.schedule.email.application.send.mime.model.MimeMessage;
import com.ums.schedule.email.application.send.mime.writer.MimeWriter;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component(value = "mimeAttachmentWriter")
public class MimeMultiPartWriteHelper extends MimeMessageWriteHelper {
    private final AwsS3Repository repository;
    private final List<EmailConvertHandler> handlers;

    public MimeMultiPartWriteHelper(@Qualifier("mimeImageWriter") MimeWriteHelper writer,
                                    AwsS3Repository repository,
                                    List<EmailConvertHandler> handlers) {
        super(writer);
        this.repository = repository;
        this.handlers = handlers;
    }

    @Override
    public Writer write(MimeWriter writer, SendTargetRequest target) throws IOException, TemplateException {
        MimeMessage mimeMessage = writer.mimeMessage();
        if(mimeMessage.attachments().size() > 0) {
            mimeMessage.setMultipartHeader(writer);
            super.write(writer, target);

            for(MimeAttachFile multiPart : mimeMessage.attachments()) {
                AttachMessage message = multiPart.message();
                Path path = getFilePath(target, multiPart, message);
                try (InputStream is = getInputStream(message, path)) {
                    multiPart.setMultipartHeader(writer, target.getMessageVariable());
                    multiPart.setAttachmentAndDownloadName(writer, target.getMessageVariable());
                    writer.write(is);
                }
            }
            mimeMessage.setMultipartFooter(writer);
            return writer.writer();
        } else {
            return super.write(writer, target);
        }
    }

    private InputStream getInputStream(AttachMessage message, Path path) {
        return Optional.ofNullable(path)
                .map(f -> {
                    try {
                        return Files.newInputStream(f);
                    } catch (IOException e) {
                        throw EmailMessageConvertException.of(e);
                    }
                })
                .orElseGet(() -> repository.getFileContent(message.fileKey()));
    }

    private Path getFilePath(SendTargetRequest target, MimeAttachFile multiPart, AttachMessage message) {
        return handlers.stream()
                .filter(h -> h.supports(message.convertType(), message.encryptionType()))
                .findFirst()
                .map(h -> {
                    try {
                        return h.handle(multiPart, target.getMessageVariable());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .orElseGet(() -> multiPart.file());
    }
}
