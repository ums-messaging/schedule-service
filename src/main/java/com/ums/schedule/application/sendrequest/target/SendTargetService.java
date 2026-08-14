package com.ums.schedule.application.sendrequest.target;

import com.ums.schedule.application.sendrequest.target.result.SendTargetSaveResult;
import com.ums.schedule.application.target.exception.SendTargetUploadExcecption;
import com.ums.schedule.application.ums.common.target.context.SendTargetCreateContext;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.domain.target.SendTargetRepository;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
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

    public List<SendTarget> saveTargetList(TargetUploadReport targetUploadReport, List<SendTargetCreateContext> context) {
        List<SendTarget> targetList = toEntityList(targetUploadReport, context);
        try {
            repository.saveAll(targetList);
        } catch (Exception e) {
            throw e;
        }
        return targetList;
    }

    private List<SendTarget> toEntityList(TargetUploadReport targetUploadReport, List<SendTargetCreateContext> context) {
        return context
                .stream().map(v -> SendTarget.of(targetUploadReport, v))
                .toList();
    }

    public List<SendTarget> saveTarget(TargetUploadReport targetUploadReport,
                                           List<SendTargetCreateContext> contexts) {
        List<SendTarget> targetList = toEntityList(targetUploadReport, contexts);
        return targetList.stream()
                .map(target -> {
                    try {
                        return repository.saveAndFlush(target);
                    } catch (DataIntegrityViolationException e) {
                        throw SendTargetUploadExcecption.of(target.getTargetKey(), e);
                    } catch (BusinessException e) {
                        return target.onError(e.getErrorMessage());
                    }
                }).toList();
    }
}
