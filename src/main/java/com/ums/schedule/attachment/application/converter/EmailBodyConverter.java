package com.ums.schedule.attachment.application.converter;

import com.ums.schedule.attachment.application.converter.handler.EmailBodyConvertHandler;
import com.ums.schedule.attachment.application.model.AttachmentDto;
import com.ums.schedule.attachment.application.response.AwsS3FileMetadataResponse;
import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.attachment.infrastructure.AwsS3Repository;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.target.EmailSendTarget;
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

    public List<Attachment> createAttachment(EmailBody body, List<AttachmentDto> templates, EmailSendTarget target)  {
        List<Attachment> attachmentList = templates.stream()
                .map(dto -> create(body, target, dto))
                .toList();
        return attachmentList;
    }

    private Attachment convertEmailBody(EmailBody body, EmailSendTarget target, AttachmentDto dto) {
        Attachment attachment = Attachment.of(dto, target);
        String uploadKey = uploadFile(dto, body, target);
        FileMetaData fileMetadata = getFileMetadata(dto.getObjectKey(), uploadKey);
        attachment.defineFileMetadata(fileMetadata);
        return attachment;
    }

    private Attachment create(EmailBody body, EmailSendTarget target, AttachmentDto dto) {
        return dto.isConvert() ? convertEmailBody(body, target, dto) :
                Attachment.of(dto, target);
    }

    // ObjectKey
    private String uploadFile(AttachmentDto dto, EmailBody body, EmailSendTarget target) {
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
