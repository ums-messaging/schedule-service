package com.ums.schedule.application.ums.email.generator;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.target.exception.SendTargetRowException;
import com.ums.schedule.application.target.exception.TargetUploadReportNotFoundException;
import com.ums.schedule.application.target.reader.model.TargetRowResult;
import com.ums.schedule.application.target.uploader.model.EmailGeneratorContext;
import com.ums.schedule.application.ums.common.target.result.TargetMessageResult;
import com.ums.schedule.application.ums.common.template.loader.model.EmailTemplate;
import com.ums.schedule.application.ums.email.generator.model.EmailTargetMessageCreateContext;
import com.ums.schedule.application.ums.email.generator.model.RenderedTemplate;
import com.ums.schedule.application.ums.email.generator.policy.model.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.generator.resolver.EmailConvertResolver;
import com.ums.schedule.common.code.target.SendTargetRowStatus;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.message.EmailTargetMessage;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.domain.target.upload.TargetUploadReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTargetMessageGenerator {
    private final EmailConvertResolver resolver;

    public TargetMessageResult generate(Integer partitionNo, EmailGeneratorContext context, TargetRowResult rowResult) {
        TargetMessageData targetData = rowResult.targetMessage();
        try {
            if(rowResult.status() == SendTargetRowStatus.FAIL) {
                throw SendTargetRowException.of(rowResult.rowNo(), rowResult.reason());
            }
            EmailTemplate template = context.template();
            RenderedTemplate renderedTemplate = RenderedTemplate.of(template, targetData);
            EmailConvertPolicy convertPolicy = resolver.resolve(renderedTemplate, targetData);

            EmailTargetMessageCreateContext messageContext = EmailTargetMessageCreateContext
                    .of(context.sendMessage(), template, convertPolicy);

            TargetMessage targetMessage = EmailTargetMessage.of(messageContext, targetData);

            return TargetMessageResult.of(partitionNo, rowResult, targetMessage);
        } catch (BusinessException e) {
            return TargetMessageResult.of(partitionNo, rowResult, e.getErrorMessage());
        }
    }
}
