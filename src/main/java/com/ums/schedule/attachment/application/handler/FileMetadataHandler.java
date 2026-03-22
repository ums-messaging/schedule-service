package com.ums.schedule.attachment.application.handler;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.STORAGE_TYPE;

@Component
@RequiredArgsConstructor
public class FileMetadataHandler implements AttachmentHandler {
    private final EnumMapperFactory factory;
    private final @Qualifier("sectionTypeHandler") AttachmentHandler handler;

    @Override
    public Attachment handle(EmailMessageCommand command, EmailContentResponse body) {
        Attachment attachment = handler.handle(command, body);
        EnumMapperValue storageType = factory.findEnumMapperValue(STORAGE_TYPE, body.storageType());
        FileMetaData metaData = FileMetaData.fromResponse(body)
                .resolveStorageType(storageType);
        return attachment.defineFileMetadata(metaData);
    }
}
