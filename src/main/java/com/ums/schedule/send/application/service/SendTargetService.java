package com.ums.schedule.send.application.service;

import com.ums.schedule.message.application.assembler.EmailMessageAssembler;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.send.EmailSendJob;
import com.ums.schedule.send.application.assembler.SendTargetAssembler;
import com.ums.schedule.send.application.assembler.TargetUploadService;
import com.ums.schedule.send.application.model.command.SendTargetCreateCommand;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.application.model.dto.TargetUploadDto;
import com.ums.schedule.send.domain.request.EmailBody;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.request.upload.TargetUpload;
import com.ums.schedule.send.domain.request.upload.repository.TargetUploadRepository;
import com.ums.schedule.send.domain.request.upload.status.TargetUploadStatus;
import com.ums.schedule.send.domain.target.EmailSendTarget;
import com.ums.schedule.send.domain.target.exception.SendTargetException;
import com.ums.schedule.send.domain.target.repository.SendTargetRepository;
import com.ums.schedule.template.domain.email.EmailTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendTargetService {
    private final SendTargetRepository repository;
    private final SendTargetAssembler targetAssembler;

    public void saveTargetList(EmailSendJob job, List<SendTargetDto> targetDtos) {
        TargetUpload targetUpload = job.targetUpload();
        TargetUploadStatus status = job.targetUpload().getUploadStatus();
        List<EmailSendTarget> targetList;
        try {
            status = targetUpload.parsing();
            targetList = targetDtos.stream()
                    .map(dto -> targetAssembler.assemble(job, dto))
                    .map(target -> repository.save(target))
                    .toList();
        } catch (Exception e) {
            throw e;
        }
    }

    public void saveTarget(EmailSendJob job, List<SendTargetDto> targetDtos) {
        TargetUpload targetUpload = job.targetUpload();
        TargetUploadStatus status = job.targetUpload().getUploadStatus();
        List<EmailSendTarget> targetList;
            status = targetUpload.parsing();

        for (SendTargetDto dto : targetDtos) {
            try {
                EmailSendTarget target = targetAssembler.assemble(job, dto);
                repository.saveAndFlush(target);
            } catch (SendTargetException | DataIntegrityViolationException e) {
                // 실패 데이터 처리
            }
        }
    }
}
