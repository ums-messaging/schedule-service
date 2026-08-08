package com.ums.schedule.domain.message.email.convert;

import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.attachment.model.EmailResourcePayload;
import com.ums.schedule.application.ums.email.generator.AttachmentMetadata;
import com.ums.schedule.common.code.email.ConvertType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConvertMail {
    @Column(name = "convert_type", nullable = false)
    private ConvertType convertType;
    private String fileKeyTemplate;
    private String attachmentName;
    private String downloadName;

    public static ConvertMail of(ConvertType convertType, EmailSendCreateRequest request) {
        return new ConvertMail(
                convertType,
                request.fileKeyTemplate(),
                request.attachmentName(),
                request.downloadName()
        );
    }

    public static ConvertMail of() {
        return new ConvertMail(
                ConvertType.NONE,
                null,
                null,
                null
        );
    }
}
