package com.ums.schedule.attachment.domain;

import com.ums.schedule.attachment.application.command.SecurityPolicyCommand;
import com.ums.schedule.attachment.code.AttachmentEnumMapper;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.message.domain.email.EmailSendMessage;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Attachment {
    private ConvertTypeEnum convertType;
    private SecurityPolicy securityPolicy;
    private AttachmentPolicy attachmentPolicy;
    private FileMetaData fileMetaData;
    private SendMessage sendMessage;

    public static Attachment of(EnumMapperValue convertType) {
        Attachment attachment = new Attachment();
        attachment.resolveConvertType(convertType);
        return attachment;
    }

    private void resolveConvertType(EnumMapperValue convertType) {
        this.convertType = ConvertTypeEnum.valueOf(convertType.value());
    }

    public Attachment defineSecurityPolicy(SecurityPolicyCommand command, Map<AttachmentEnumMapper, EnumMapperValue> enumMapperMap) {
        this.securityPolicy = SecurityPolicy.of(command.passwordPolicy(), command.passwordFormat(), enumMapperMap);
        return this;
    }

    public Attachment defineAttachmentPolicy(AttachmentPolicy policy) {
        this.attachmentPolicy = policy;
        return this;
    }

    public Attachment defineFileMetadata(FileMetaData fileMetaData) {
        this.fileMetaData = fileMetaData;
        return this;
    }

    public Attachment applySendMessage(EmailSendMessage sendMessage) {
        sendMessage.getAttachments().add(this);
        this.sendMessage = sendMessage;
        return this;
    }
}
