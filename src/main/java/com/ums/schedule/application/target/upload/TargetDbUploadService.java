package com.ums.schedule.application.target.upload;

import com.ums.schedule.application.target.SendTargetService;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.exeption.SendTargetException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TargetDbUploadService{
    private final SendTargetService targetService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TargetUpload create(Long uploadId, List<SendTarget> targetList) {

        try {
            targetService.saveTargetList(uploadId, targetList);
        } catch (SendTargetException | DataIntegrityViolationException e) {
            targetService.saveTarget(uploadId, targetList);
        }
        return null;
    }


}
