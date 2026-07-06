package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.domain.sendrequest.target.code.SendTargetStatusEnum;
import com.ums.schedule.domain.sendrequest.target.SendTargetRepository;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
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

    public List<SendTarget> saveTargetList(List<SendTarget> targetList) {
        try {
            repository.saveAll(targetList);
        } catch (Exception e) {
            throw e;
        }
        return targetList;
    }

    public Map<SendTargetStatusEnum, List<SendTarget>> saveTarget(List<SendTarget> targetList) {
        return targetList.stream()
                .map(target -> {
                    try {
                        return repository.saveAndFlush(target);
                    } catch (DataIntegrityViolationException e) {
                        return target.onError(e.getMessage());
                    }
                })
                .collect(Collectors.groupingBy(SendTarget::getStatus));
    }
}
