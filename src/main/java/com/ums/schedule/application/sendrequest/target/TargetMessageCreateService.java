package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.query.TargetDuplicatedQuery;
import com.ums.schedule.application.target.exception.SendTargetUploadExcecption;
import com.ums.schedule.common.code.target.SendTargetResultCode;
import com.ums.schedule.domain.target.TargetMessage;
import com.ums.schedule.domain.target.TargetMessageJpaRepository;
import com.ums.schedule.adapter.persistence.TargetMessageQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TargetMessageCreateService {
    private final TargetMessageJpaRepository repository;
    private final TargetMessageQueryRepository queryRepository;

    @Transactional
    public List<TargetMessage> saveTargetList(List<TargetMessage> targetList) {
        try {
            repository.saveAllAndFlush(targetList);
            return targetList;
        } catch (Exception e) {
            log.error("target.batch.upload.error = {}", e.getMessage());
            throw SendTargetUploadExcecption.of(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<TargetMessage> saveTarget(UUID uploadId, List<TargetMessage> targetMessages) {
        TargetDuplicatedQuery query = TargetDuplicatedQuery.of(uploadId, targetMessages);
        List<TargetMessage> findDuplicatedList = queryRepository.findByTargetKeysAndContacts(query);
        List<TargetMessage> results = targetMessages.stream()
                .map(targetMessage -> {
                    try {
                        findDuplicatedList.stream()
                                .filter(l -> hasDuplicatedKey(targetMessage, l))
                                .findFirst()
                                .map(l -> targetMessage.changeTargetMessageKey(l.getTargetKey(), l.getContact()));
                        repository.saveAndFlush(targetMessage);
                    } catch (DataIntegrityViolationException e) {
                        log.error("save.error = {}", e.getMessage());
                        targetMessage.onError(SendTargetResultCode.TARGET_UPLOAD_FAIL, e.getMessage());
                    }
                    return targetMessage;
                }).toList();
        return results;
    }

    private boolean hasDuplicatedKey(TargetMessage targetMessage, TargetMessage l) {
        return targetMessage.getTargetKey().equals(l.getTargetKey()) || targetMessage.getContact().equals(l.getContact());
    }
}
