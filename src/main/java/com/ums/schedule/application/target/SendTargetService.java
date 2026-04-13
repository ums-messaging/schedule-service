package com.ums.schedule.application.target;

import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.status.TargetUploadStatus;
import com.ums.schedule.domain.target.exeption.SendTargetException;
import com.ums.schedule.domain.target.SendTargetRepository;
import com.ums.schedule.domain.channel.email.message.EmailMessage;
import com.ums.schedule.application.target.assembler.SendTargetAssembler;
import com.ums.schedule.application.target.dto.SendTargetDto;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendTargetService {
    private final SendTargetRepository repository;
    private final SendTargetAssembler targetAssembler;

    public void saveTargetList(EmailMessage meesage, List<SendTargetDto> targetDtos) {
        TargetUpload targetUpload = meesage.targetUpload();
        TargetUploadStatus status = meesage.targetUpload().getUploadStatus();
        List<SendTarget> targetList = targetUpload.getTargetList();
        try {
            status = targetUpload.parsing();
            targetList = targetDtos.stream()
                    .map(dto -> targetAssembler.assemble(meesage, dto))
                    .map(target -> repository.save(target))
                    .toList();
        } catch (Exception e) {
            throw e;
        }
    }

    public void saveTarget(EmailMessage job, List<SendTargetDto> targetDtos) {
        TargetUpload targetUpload = job.targetUpload();
        TargetUploadStatus status = job.targetUpload().getUploadStatus();
        List<SendTarget> targetList = targetUpload.getTargetList();
            status = targetUpload.parsing();

        for (SendTargetDto dto : targetDtos) {
            try {
                SendTarget target = targetAssembler.assemble(job, dto);
                repository.saveAndFlush(target);
            } catch (SendTargetException | DataIntegrityViolationException e) {
                // 실패 데이터 처리
            }
        }
    }
}
