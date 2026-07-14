package com.ums.schedule.application.ums.email.message.provider;

import com.ums.schedule.adapter.api.request.email.EmailSendCreateRequest;
import com.ums.schedule.application.exception.email.security.SecurityMailProcessException;
import com.ums.schedule.application.exception.template.TemplateNotFoundException;
import com.ums.schedule.application.ums.email.attachment.model.AttachmentContext;
import com.ums.schedule.application.ums.email.convert.EmailConvertPolicy;
import com.ums.schedule.application.ums.email.convert.resolver.EmailConvertResolver;
import com.ums.schedule.application.ums.email.convert.resolver.model.EmailConvertResolveCommand;
import com.ums.schedule.application.ums.email.security.SecurityMail;
import com.ums.schedule.application.ums.email.security.SecurityMailAssembler;
import com.ums.schedule.application.ums.email.security.model.SecurityMailCommand;
import com.ums.schedule.application.ums.email.template.query.EmailTemplateQueryService;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateDetailQuery;
import com.ums.schedule.application.ums.email.template.query.model.EmailTemplateResult;
import com.ums.schedule.common.code.email.EmailTemplateSectionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailMessagePolicyProvider {
    private final EmailTemplateQueryService templateService;
    private final EmailConvertResolver resolver;
    private final SecurityMailAssembler securityAssembler;

    public EmailMessageContext provide(EmailTemplateDetailQuery query, EmailSendCreateRequest request){
        EmailTemplateResult template = getEmailTemplateResult(query, request);
        SecurityMail securityMail = createSecurityMail(request);
        EmailConvertPolicy policy = resolveEmailConvertPolicy(request, template, securityMail);

        return EmailMessageContext.of(template, securityMail, policy);
    }

    private EmailConvertPolicy resolveEmailConvertPolicy(EmailSendCreateRequest request, EmailTemplateResult template, SecurityMail securityMail) {
        AttachmentContext body = Optional.ofNullable(template.bodyTemplate())
                .orElseThrow(() -> TemplateNotFoundException.of(EmailTemplateSectionEnum.BODY));
        EmailConvertResolveCommand command = EmailConvertResolveCommand.of(request.convertType(), body, template);
        EmailConvertPolicy policy = resolver.resolve(command, securityMail);
        return policy;
    }

    private SecurityMail createSecurityMail(EmailSendCreateRequest request) {
        if(request.securityPolicy() != null) {
            SecurityMailCommand command = request.securityPolicy().toCommand();
            return Optional.ofNullable(securityAssembler.assemble(command))
                    .orElseThrow(SecurityMailProcessException::of);
        }
        return null;
    }

    private EmailTemplateResult getEmailTemplateResult(EmailTemplateDetailQuery query, EmailSendCreateRequest request) {
        EmailTemplateResult template = templateService.findTemplate(query);
        return template;
    }
}
