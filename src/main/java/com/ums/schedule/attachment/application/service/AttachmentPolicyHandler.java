package com.ums.schedule.attachment.application.service;

import com.ums.schedule.attachment.domain.Attachment;
import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.FileMetaData;
import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.template.application.response.email.EmailContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.ums.schedule.attachment.code.AttachmentEnumMapper.STORAGE_TYPE;

@Component
@RequiredArgsConstructor
public class AttachmentPolicyHandler implements AttachmentHandler {
    private final AttachmentHandler securityPolicyHandler;
    private final EnumMapperFactory factory;

    @Override
    public Attachment handle(EmailMessageCommand command, EmailContentResponse body) {
        Attachment attachment = this.securityPolicyHandler.handle(command, body);

        AttachmentPolicy policy = AttachmentPolicy.of(body.attachmentName(), body.downloadName());
        EnumMapperValue storageType = factory.findEnumMapperValue(STORAGE_TYPE, body.storageType());
        FileMetaData metaData = FileMetaData.fromResponse(body)
                .resolveStorageType(storageType);

        return Optional.ofNullable(policy)
                .map(p -> attachment.defineAttachmentPolicy(policy))
                .map(attach -> attachment.defineFileMetadata(metaData))
                .orElse(null);
    }

}