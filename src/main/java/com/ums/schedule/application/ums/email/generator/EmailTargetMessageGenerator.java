package com.ums.schedule.application.ums.email.generator;

import com.ums.schedule.application.target.reader.model.TargetUploadRowResult;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.context.SendTargetGroupedList;
import com.ums.schedule.application.ums.common.target.result.SendTargetResult;

import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.resolver.EmailConvertResolver;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTargetMessageGenerator {
    private final EmailConvertResolver resolver;

    public EmailTargetMessage generate(EmailGeneratorContext context, SendTargetResult result) {
        EmailTargetMessage targetMessage = EmailTargetMessage.of(context, result);
        try {
            if(result.resultCode() == SendTargetResultCode.SUCCESS) {
                EmailConvertPolicy policy = resolver.resolve(context.template(), result.targetData());
                targetMessage.renderTemplate(context.template(), policy);
            }
        } catch (BusinessException e) {
            targetMessage.onError(SendTargetResultCode.MESSAGE_GENERATE_FAIL, e.getErrorMessage());
        }
        return targetMessage;
    }
}
