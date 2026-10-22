package com.ums.schedule.email.application.target;

import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import com.ums.schedule.email.common.code.EmailSendStatusCode;
import com.ums.schedule.email.common.code.SendTargetErrorTypeCode;
import com.ums.schedule.email.domain.event.SendTargetResultList;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.orchestrator.application.upload.exception.SendTargetUploadExcecption;
import com.ums.schedule.email.application.target.query.model.TargetDuplicatedQuery;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.SendTargetRequestQueryRepository;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.SendTargetRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendTargetService {
    private final SendTargetRequestRepository repository;
    private final SendTargetRequestQueryRepository queryRepository;

    @Transactional
    public List<SendTargetRequest> saveTargetList(Long requestId, List<SendTargetRequest> targetList) {
        try {
            repository.saveAll(requestId, EmailSendStatusCode.SENDING, targetList);
            return targetList;
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw SendTargetUploadExcecption.of(e);
        }
    }

    @Transactional
    public SendTargetResultList saveTarget(Long sendRequestId, SendTargetGroupedList groupList, String resultMessage) {
        TargetDuplicatedQuery query = TargetDuplicatedQuery.of(sendRequestId, groupList.targets());
        List<SendTargetRequest> queryResults = queryRepository.findByTargetKeysAndContacts(query);
        if(queryResults.size() > 0) {
            List<SendTargetRequest> saveTargets = groupList.targets().stream()
                    .filter(v -> !isDuplicated(v, queryResults))
                    .toList();
            List<SendTargetError> errorTargets = groupList.targets().stream()
                    .filter(v -> isDuplicated(v, queryResults))
                    .map(v -> SendTargetError.of(v, SendTargetErrorTypeCode.DUPLICATED_KEY, null))
                    .toList();
            List<SendTargetRequest> saved = repository.saveAll(sendRequestId, EmailSendStatusCode.SENDING, saveTargets);
            return SendTargetResultList.of(sendRequestId, groupList.group(), saved, errorTargets);
        } else {
            List<SendTargetError> errors = groupList.targets().stream()
                    .map(v -> SendTargetError.of(v, SendTargetErrorTypeCode.UPLOAD_ERROR, resultMessage))
                    .toList();
            return SendTargetResultList.of(sendRequestId, groupList.group(), List.of() ,errors);
        }
    }

    private boolean isDuplicated(SendTargetRequest target, List<SendTargetRequest> results) {
        List<Boolean> exists = results.stream()
                .map(v -> isDuplicated(target, v))
                .toList();
        return exists.size() > 0;
    }

    private boolean isDuplicated(SendTargetRequest target, SendTargetRequest result) {
        boolean exits = existTargetKey(target, result) || existsEmail(target, result);
        return exits;
    }

    private boolean existsEmail(SendTargetRequest target, SendTargetRequest result) {
        return target.getReceiver().getEmail().equals(result.getReceiver().getEmail());
    }

    private boolean existTargetKey(SendTargetRequest target, SendTargetRequest result) {
        return target.getReceiver().getTargetKey().equals(result.getReceiver().getTargetKey());
    }
}
