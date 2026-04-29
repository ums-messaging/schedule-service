package com.ums.schedule.application.target;

import com.ums.schedule.adapter.api.target.SendTargetUploadRequest;
import com.ums.schedule.adapter.api.template.email.EmailTemplateClient;
import com.ums.schedule.adapter.api.template.email.EmailTemplateResponse;
import com.ums.schedule.application.ChannelFactory;
import com.ums.schedule.application.channel.email.converter.EmailBodyConverter;
import com.ums.schedule.application.channel.email.template.EmailTemplateService;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.channel.email.EmailSendRequestJpaRepository;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailFactory implements ChannelFactory<EmailTemplate> {
    private final EmailSendRequestJpaRepository repository;
    private final EmailTemplateService templateService;
    private final EmailBodyConverter converter;
    private final EmailTemplateClient templateClient;

    @Override
    public List<SendTarget> makeMessage(EmailTemplate template, List<SendTargetDto> targetList) {
        return targetList.stream()
                .map(dto -> SendTarget.of(dto, template))
                .map(target -> {
                    converter.createAttachment(template, target);
                    return target;
                })
                .toList();
    }

    @Override
    public EmailTemplate getTemplate(Long requestId, String templateKey) {
        EmailSendRequest request = repository.findById(requestId).orElseThrow();
        EmailTemplateResponse template = templateClient.getTemplate(templateKey);
        return templateService.assemble(request.getBody(), template);
    }

}
