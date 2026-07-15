package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.common.code.target.SendTargetStatusEnum;
import com.ums.schedule.domain.request.target.SendTargetRepository;
import com.ums.schedule.domain.request.target.SendTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SendTargetService {
    private final SendTargetRepository repository;

    public SendTargetSaveResult saveTargetList(List<SendTarget> targetList) {
        try {
            repository.saveAll(targetList);
        } catch (Exception e) {
            throw e;
        }
        return SendTargetSaveResult.of(targetList);
    }

    public SendTargetSaveResult saveTarget(List<SendTarget> targetList) {
        Map<SendTargetStatusEnum, List<SendTarget>> targetResultMap = targetList.stream()
                .map(target -> {
                    try {
                        return repository.saveAndFlush(target);
                    } catch (DataIntegrityViolationException e) {
                        return target.onError(e.getMessage());
                    }
                })
                .collect(Collectors.groupingBy(target -> target.getState().currentStatusCode()));

        return SendTargetSaveResult.of(targetResultMap);
    }
}
