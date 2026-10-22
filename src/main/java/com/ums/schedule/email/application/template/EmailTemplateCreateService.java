package com.ums.schedule.email.application.template;

import com.ums.schedule.email.adaptor.in.rest.template.request.EmailTemplateCreateRequest;
import com.ums.schedule.email.application.template.command.EmailTemplateCreateCommand;
import com.ums.schedule.email.application.template.query.TemplateFileService;
import com.ums.schedule.email.application.template.query.model.EmailTemplateFileResult;
import com.ums.schedule.email.domain.template.EmailTemplate;
import com.ums.schedule.email.domain.template.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailTemplateCreateService {
    private final TemplateFileService queryService;
    private final EmailTemplateRepository repository;

    @Transactional
    public EmailTemplate create(String customerId, EmailTemplateCreateRequest request) {
        EmailTemplateFileResult result = queryService.findTemplate(customerId, request.templateKey());
        EmailTemplateCreateCommand command = EmailTemplateCreateCommand.of(customerId, request, result.templateContentMap(), result.imageDir());
        EmailTemplate template = EmailTemplate.of(command);
        return repository.save(template);
    }
}
