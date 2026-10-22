package com.ums.schedule.email.application.message.provider;

import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.email.adaptor.in.rest.message.request.SecurityMailRequest;
import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.email.application.exception.SecurityMailProcessException;
import com.ums.schedule.email.application.message.security.SecurityMailAssembler;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.email.domain.common.MultiPartFile;
import com.ums.schedule.email.domain.message.ConvertMessage;
import com.ums.schedule.email.domain.message.security.SecurityMail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessagePolicyProvider {
    private final EnumMapperFactory mapperFactory;
    private final SecurityMailAssembler securityAssembler;

    public EmailPolicyResult provide(MessageCreateRequest request){
        MessageType messageType = getMessageType(request.messageType());
        EmailType emailType = getEmailType(request.mailType());

        SecurityMail securityMail = createSecurityMail(request);
        ConvertType convertType = resolveConvertType(request);
        ConvertMessage convertMessage = ConvertMessage.of(emailType, convertType, request);

        return EmailPolicyResult.of(emailType, messageType, convertMessage, securityMail);
    }

    private EmailType getEmailType(String type) {
        EnumMapperValue typeValue  = mapperFactory.findEnumMapperValue(EmailCode.EMAIL_TYPE, type);
        return EmailType.valueOf(typeValue.code());
    }

    private ConvertType resolveConvertType(MessageCreateRequest request) {
        return Optional.ofNullable(request.convertType())
                .filter(StringUtils::hasText)
                .map(v -> mapperFactory.findEnumMapperValue(EmailCode.CONVERT_TYPE, v))
                .map(v -> ConvertType.valueOf(v.code()))
                .orElseGet(() -> ConvertType.NONE);
    }

    private MessageType getMessageType(String messageType) {
        EnumMapperValue messageTypeValue = EnumMapperValue.fromEnumMapperType(MessageType.NONE);
        if(StringUtils.hasText(messageType)) {
            messageTypeValue =  mapperFactory.findEnumMapperValue(EmailCode.MESSAGE_TYPE, messageType);
        }
        return MessageType.valueOf(messageTypeValue.code());
    }

    private MultiPartFile resolveEmailConvertPolicy(ConvertType convertType, MessageCreateRequest request) {
        if(convertType == ConvertType.NONE) {
            return null;
        }
        return new MultiPartFile(request.fileKeyTemplate(), request.attachmentName(), request.downloadName());
    }

    private SecurityMail createSecurityMail(MessageCreateRequest request) {
        EnumMapperValue emailType = mapperFactory.findEnumMapperValue(EmailCode.EMAIL_TYPE, request.mailType());
        if(EmailType.SECURITY == EmailType.valueOf(emailType.code())) {
            return Optional.ofNullable(request.securityPolicy())
                    .map(SecurityMailRequest::toCommand)
                    .map(v -> securityAssembler.assemble(v))
                    .orElseThrow(SecurityMailProcessException::of);
        }
        return null;
    }

    private EnumMapperValue resolveConvertType(String convertType, EmailType emailType) {
        if(emailType == EmailType.SECURITY) {
            return StringUtils.hasText(convertType) && !convertType.equals(ConvertType.NONE.code())?
                    resolveConvertType(convertType) : defaultEnumMapperValue(ConvertType.HTML);
        }
        return StringUtils.hasText(convertType) ? resolveConvertType(convertType) :
                defaultEnumMapperValue(ConvertType.NONE) ;
    }

    private EnumMapperValue resolveConvertType(String convertType) {
        return mapperFactory.findEnumMapperValue(EmailCode.CONVERT_TYPE, convertType);
    }

    private EnumMapperValue defaultEnumMapperValue(ConvertType convertType) {
        return EnumMapperValue.fromEnumMapperType(convertType);
    }
}
