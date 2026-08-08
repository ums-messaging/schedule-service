package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.adapter.api.request.email.request.EmailSecurityPolicyRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.application.ums.email.exception.EmailConvertTypeNotSupportedException;
import com.ums.schedule.application.ums.email.exception.SecurityMailProcessException;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.security.SecurityMailAssembler;
import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailCode;
import com.ums.schedule.common.code.email.EmailType;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.mapper.exception.EnumMapperNotFoundException;
import com.ums.schedule.domain.message.email.convert.ConvertMail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailMessagePolicyProvider {
    private final EnumMapperFactory mapperFactory;
    private final SecurityMailAssembler securityAssembler;

    public EmailPolicyResult provide(EmailSendCreateRequest request){
        SecurityMail securityMail = createSecurityMail(request);
        ConvertMail convertMail = resolveEmailConvertPolicy(request, securityMail);

        return EmailPolicyResult.of(securityMail, convertMail);
    }

    private ConvertMail resolveEmailConvertPolicy(EmailSendCreateRequest request, SecurityMail securityMail) {
        ConvertType convertType = resolve(request.convertType(), securityMail);
        return ConvertMail.of(convertType, request);
    }

    private SecurityMail createSecurityMail(EmailSendCreateRequest request) {
        EnumMapperValue emailType = mapperFactory.findEnumMapperValue(EmailCode.EMAIL_TYPE, request.mailType());
        if(EmailType.SECURITY == EmailType.valueOf(emailType.code())) {
            return Optional.ofNullable(request.securityPolicy())
                    .map(EmailSecurityPolicyRequest::toCommand)
                    .map(v -> securityAssembler.assemble(v))
                    .orElseThrow(SecurityMailProcessException::of);
        }
        return null;
    }

    public ConvertType resolve(String convertType, SecurityMail securityMail) {
        EnumMapperValue convertTypeValue = resolveConvertType(convertType, securityMail);
        return ConvertType.valueOf(convertTypeValue.code());
    }

    private EnumMapperValue resolveConvertType(String convertType, SecurityMail securityMail) {
        return Optional.ofNullable(convertType)
                .map(type -> mapperFactory.findEnumMapperValue(EmailCode.CONVERT_TYPE, type))
                .orElseGet(() -> resolveConvertTypeBySecurityPolicy(securityMail));
    }

    private EnumMapperValue resolveConvertTypeBySecurityPolicy(SecurityMail securityMail) {
        return Optional.ofNullable(securityMail)
                .map(policy -> EnumMapperValue.fromEnumMapperType(ConvertType.HTML))
                .orElse(EnumMapperValue.fromEnumMapperType(ConvertType.NONE));
    }
}
