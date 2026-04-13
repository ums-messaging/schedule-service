package com.ums.schedule.application.channel.email.converter;

import com.ums.schedule.application.channel.email.dto.AttachmentDto;
import com.ums.schedule.application.channel.email.converter.handler.EmailBodyConvertHandler;
import com.ums.schedule.adapter.storage.AwsS3FileMetadataResponse;
import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.attachment.FileMetaData;
import com.ums.schedule.adapter.storage.AwsS3Repository;
import com.ums.schedule.domain.channel.email.message.EmailBody;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailBodyConverter {
    private final EmailBodyConvertHandler handler;
    private final AwsS3Repository repository;

    public List<Attachment> createAttachment(EmailBody body, List<AttachmentDto> templates, SendTarget target)  {
        List<Attachment> attachmentList = templates.stream()
                .map(dto -> create(body, target, dto))
                .toList();
        return attachmentList;
    }

    private Attachment convertEmailBody(EmailBody body, SendTarget target, AttachmentDto dto) {
        Attachment attachment = Attachment.of(dto, target);
        String uploadKey = uploadFile(dto, body, target);
        FileMetaData fileMetadata = getFileMetadata(dto.getObjectKey(), uploadKey);
        attachment.defineFileMetadata(fileMetadata);
        return attachment;
    }

    private Attachment create(EmailBody body, SendTarget target, AttachmentDto dto) {
        return dto.isConvert() ? convertEmailBody(body, target, dto) :
                Attachment.of(dto, target);
    }

    // ObjectKey
    private String uploadFile(AttachmentDto dto, EmailBody body, SendTarget target) {
        try {
            String uploadKey = "";
            File file = handler.handle(dto, body, target);
            return repository.upload(file, uploadKey);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileMetaData getFileMetadata(String fileKey, String uploadKey) {
        AwsS3FileMetadataResponse response = repository.getFileMetadata(uploadKey);
        return FileMetaData.of(response,fileKey, uploadKey);
    }
}
