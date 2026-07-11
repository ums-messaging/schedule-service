package com.ums.schedule.application.ums.email.convert.strategy;

import com.ums.schedule.application.ums.email.convert.ConvertedAttachment;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertResult;
import com.ums.schedule.application.ums.email.convert.strategy.model.EmailConvertPolicyContext;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.domain.sendrequest.resource.email.code.ConvertTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class AttachmentEmailConvertPolicy implements EmailMessageConvertStrategy {
    @Override
    public boolean supports(EnumMapperValue mapperValue) {
        return !ConvertTypeEnum.NONE.equals(ConvertTypeEnum.valueOf(mapperValue.code()));
    }

    @Override
    public EmailConvertResult convert(EmailConvertPolicyContext command) {
        ConvertedAttachment convertToAttachment = ConvertedAttachment.of(command.convertType(), command.bodyKey());
        List<ConvertedAttachment> combinedAttachments = combineAttachment(command.attachments(), convertToAttachment);;
        return EmailConvertResult.of(command.coverKey(), combinedAttachments);
    }

    private List<ConvertedAttachment> combineAttachment(List<ConvertedAttachment> attachments, ConvertedAttachment newAttachment) {
        return Stream.concat(attachments.stream(), Stream.ofNullable(newAttachment))
                .toList();
    }
}
