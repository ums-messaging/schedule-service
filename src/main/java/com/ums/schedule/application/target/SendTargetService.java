package com.ums.schedule.application.target;

import com.ums.schedule.domain.target.upload.TargetUploadRepository;
import com.ums.schedule.domain.target.exeption.SendTargetException;
import com.ums.schedule.domain.target.SendTargetRepository;
import com.ums.schedule.domain.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendTargetService {
    private final TargetUploadRepository targetUploadRepository;
    private final SendTargetRepository repository;

    public void saveTargetList(Long uploadId, List<SendTarget> targetList) {
        try {
            targetList.stream()
                    .map(target -> repository.save(target))
                    .toList();
        } catch (Exception e) {
            throw e;
        }
    }

    public void saveTarget(Long uploadId, List<SendTarget> targetList) {
        for (SendTarget target : targetList) {
            try {
                repository.saveAndFlush(target);
            } catch (SendTargetException | DataIntegrityViolationException e) {
                // 실패 데이터 처리
            }
        }
    }
}
