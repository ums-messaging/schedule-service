package com.ums.schedule.send.application.assembler;

import com.ums.schedule.common.code.EnumMapperValue;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.repository.SendRequestRepository;
import com.ums.schedule.send.EmailSendJob;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.application.model.dto.TargetUploadDto;
import com.ums.schedule.send.application.service.EmailSendRequestService;
import com.ums.schedule.send.application.service.SendRequestService;
import com.ums.schedule.send.application.service.SendTargetService;
import com.ums.schedule.send.code.TargetUploadTypeEnum;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.request.EmailSendRequest;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.send.domain.target.exception.SendTargetException;
import com.ums.schedule.template.application.assembler.EmailTemplateService;
import com.ums.schedule.template.domain.email.EmailTemplate;
import io.netty.channel.ChannelFactory;
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
        EmailSendJob job = EmailSendJob.of(targetUpload, template, request);

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
