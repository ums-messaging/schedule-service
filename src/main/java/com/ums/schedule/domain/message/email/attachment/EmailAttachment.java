package com.ums.schedule.domain.message.email.attachment;

import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.common.code.api.AttachmentErrorCode;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.common.code.email.AttachmentType;
import com.ums.schedule.domain.message.email.exception.AttachmentPolicyViolationException;
import com.ums.schedule.domain.request.converter.UuidBinaryConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(
        name = "email_attachment",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="uq_message_file_key",
                        columnNames = {"message_id", "file_key"}
                ),
        }
)
public class EmailAttachment {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    @UuidGenerator
    @GeneratedValue
    @Convert(converter = UuidBinaryConverter.class)
    private UUID id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AttachmentType type;

    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;
    @Column(name = "download_name", nullable = false)
    private String downloadName;

    @Column(name = "file_key", nullable = false)
    private String fileKey;
    private Long fileSize;

    @JoinColumn(name = "message_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EmailSendMessage sendMessage;

    public static EmailAttachment of(EmailSendMessage sendMessage, AttachmentContext context) {
        EmailAttachment message = new EmailAttachment();
        message.assignSendMessage(sendMessage);
        message.assignFileTypeAndKey(context.type(), context.key());
        message.assignFileSize(context.type(), context.fileSize());
        message.createAttachmentPolicy(context.attachmentName(), context.downloadName());
        return message;
    }

    private void assignFileTypeAndKey(AttachmentType type, String fileKey) {
        Objects.requireNonNull(type, "file_type");
        Objects.requireNonNull(fileKey, "file_key");
        if(validateFileTypeAndKey(type, fileKey)) {
            this.type = type;
            this.fileKey = fileKey;
            return;
        }
        throw AttachmentPolicyViolationException.of(AttachmentErrorCode.INVALID_FILE_KEY_TEMPLATE);
    }

    private boolean validateFileTypeAndKey(AttachmentType type, String fileKey) {
        if (type == AttachmentType.TEMPLATE) {
            Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
            Matcher matcher = pattern.matcher(fileKey);
            while(matcher.find()) {
                return true;
            }
            return false;
        }
        return true;
    }

    private void assignSendMessage(EmailSendMessage sendMessage) {
        Objects.requireNonNull(sendMessage, "email_send_message is not null");
        this.sendMessage = sendMessage;
        sendMessage.addAttachments(this);
    }

    private void assignFileSize(AttachmentType type, Long fileSize) {
        if(type == AttachmentType.DIRECT) {
            this.fileSize = Optional.ofNullable(fileSize)
                    .filter(size -> size > 0L)
                    .orElseThrow(() ->
                            AttachmentPolicyViolationException.of(AttachmentErrorCode.FILE_SIZE_EMPTY)
                    );
        }
    }

    private void createAttachmentPolicy(String attachmentName, String downloadName) {
        assignAttachmentName(attachmentName);
        assignDownloadName(downloadName);
    }

    private void assignDownloadName(String downloadName) {
        this.downloadName = Objects.requireNonNull(downloadName, "download_name");
    }

    private void assignAttachmentName(String attachmentName) {
        this.attachmentName = Objects.requireNonNull(attachmentName, "attachment_name");
    }

    public String fileKey() {
        return this.type == AttachmentType.DIRECT ? fileKey : null;
    }

    public String fileKeyTemplate() {
        return this.type == AttachmentType.TEMPLATE ? fileKey : null;
    }
}

