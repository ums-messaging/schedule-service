package com.ums.schedule.email.application.send.loader;

import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.email.application.message.generator.policy.model.AttachMessage;
import com.ums.schedule.email.application.send.mime.model.MimeAttachFile;
import com.ums.schedule.orchestrator.adaptor.out.storage.AwsS3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttachFileLoader {
    private final AwsS3Repository fileRepository;

    public List<MimeAttachFile> load(List<AttachMessage> attachMessages) throws IOException {
        List<MimeAttachFile> files = new ArrayList<>();
        for (AttachMessage attachMessage : attachMessages) {
            if(AttachmentType.DIRECT == attachMessage.attachmentType()) {
                Path tempFile = Files.createTempFile("attachment-" + attachMessage.id() + "-", ".tmp");
                fileRepository.download(attachMessage.fileKey(), tempFile);
                files.add(MimeAttachFile.of(attachMessage, tempFile));
            } else {
                files.add(MimeAttachFile.of(attachMessage));
            }
        }
        return files;
    }
}
