package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.result.SendTargetUploadResult;
import com.ums.schedule.application.sendrequest.target.result.TargetUploadResultList;
import com.ums.schedule.application.target.exception.SendTargetUploadExcecption;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.TargetMessageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TargetMessageCreateService {
    private final TargetMessageJpaRepository repository;

    @Transactional
    public List<TargetMessage> saveTargetList(List<TargetMessage> targetList) {
        try {
            repository.saveAll(targetList);
        } catch (Exception e) {
            throw e;
        }
        return targetList;
    }

    @Transactional
    public List<TargetMessage> saveTarget(List<TargetMessage> targetMessages) {
        List<TargetMessage> results = targetMessages.stream()
                .map(targetMessage -> {
                    try {
                        repository.saveAndFlush(targetMessage);
                    } catch (DataIntegrityViolationException e) {
                        throw e;
                    } catch (BusinessException e) {
                        targetMessage.onError(SendTargetResultCode.TARGET_UPLOAD_FAIL, e.getErrorMessage());
                    }
                    return targetMessage;
                }).toList();

        return results;
    }
}
