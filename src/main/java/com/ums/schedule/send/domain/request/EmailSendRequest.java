package com.ums.schedule.send.domain.request;

import com.ums.schedule.attachment.domain.AttachmentPolicy;
import com.ums.schedule.attachment.domain.SecurityPolicy;
import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.code.ContentTypeEnum;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.code.TargetColumnEnum;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.template.domain.email.EmailTitle;
import com.ums.schedule.template.domain.code.ConvertTypeEnum;
import com.ums.schedule.template.domain.email.EmailTemplate;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSendRequest extends SendRequest {
    private EmailBody body;
    private String mailFrom;
    private String mailFromName;

    public static EmailSendRequest of(EmailBody body) {
        EmailSendRequest request = new EmailSendRequest();
        request.setEmailBody(body);
        return request;
    }

    private void setEmailBody(EmailBody body) {
        this.body = body;
    }

    @Override
    protected String resolvedContactByChannel(Map<TargetColumnEnum, String> targetData) {
        return targetData.get(TargetColumnEnum.TARGET_EMAIL);
    }

    @Override
    protected ContentTypeEnum resolveContentType() {
        ConvertTypeEnum convertType = body.getConvertType();
        switch (convertType) {
            case HTML -> {
                return ContentTypeEnum.HTML;
            }
            case PDF -> {
                return ContentTypeEnum.PDF;
            }
        }
        return ContentTypeEnum.PLAIN;
    }
}
