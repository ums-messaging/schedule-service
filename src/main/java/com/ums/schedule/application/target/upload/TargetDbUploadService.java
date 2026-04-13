package com.ums.schedule.application.target.upload;

import com.ums.schedule.code.EnumMapperValue;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.application.target.SendTargetService;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.exeption.SendTargetException;
import com.ums.schedule.domain.channel.email.message.EmailMessage;
import com.ums.schedule.domain.channel.email.EmailSendRequest;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.application.channel.email.template.EmailTemplateService;
import com.ums.schedule.domain.channel.email.message.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TargetDbUploadService implements TargetUploadService {
    private final EmailTemplateService templateService;
    private final SendTargetService targetService;

    @Override
    public TargetUpload create(SendRequest request, List<SendTargetDto> dtos) {
        return create(request, dtos);
    }

    public TargetUpload create(EmailSendRequest request, List<SendTargetDto> dtos) {
        TargetUpload targetUpload = TargetUpload.of(TargetUploadTypeEnum.FILE, request);
        EmailTemplate template = templateService.assemble(request.getTemplateKey(), request.getBody());
        EmailMessage job = EmailMessage.of(targetUpload, template, request);

        try {
            targetService.saveTargetList(job, dtos);
        } catch (SendTargetException | DataIntegrityViolationException e) {
            targetService.saveTarget(job, dtos);
        } finally {
            targetUpload.completed();
        }
        return targetUpload;
    }

    @Override
    public boolean supports(EnumMapperValue mapperValue ) {
        return TargetUploadTypeEnum.valueOf(mapperValue.code()) == TargetUploadTypeEnum.JSON;
    }
}
