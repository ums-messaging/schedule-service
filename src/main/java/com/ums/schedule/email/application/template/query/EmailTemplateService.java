package com.ums.schedule.email.application.template.query;

import com.ums.schedule.email.domain.template.EmailTemplate;
import com.ums.schedule.email.domain.template.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailTemplateService {
    private final EmailTemplateRepository repository;

    public EmailTemplate findTemplate(String id) {
        return repository.findById(id).orElseThrow();
    }
}
