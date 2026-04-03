package com.ums.schedule.send.application.service;

import com.ums.schedule.send.application.model.dto.SendTargetDto;
import com.ums.schedule.send.code.TargetColumnEnum;
import com.ums.schedule.send.domain.reporing.SendReport;
import com.ums.schedule.send.domain.request.SendRequest;
import com.ums.schedule.send.domain.target.EmailAddress;
import com.ums.schedule.send.domain.target.SendTarget;
import com.ums.schedule.send.domain.target.TargetAddress;
import com.ums.schedule.send.domain.target.exception.SendTargetException;
import com.ums.schedule.send.domain.target.repository.SendTargetRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SendTargetService {
    private final SendTargetService targetService;
    private final SendTargetRepository repository;

    @Transactional(readOnly = false)
    public List<SendTarget> saveList(SendRequest request, List<SendTargetDto> dtos) {
        Long successCount = 0L;
        Long failCount = 0L;
        List<SendTarget> targetList = dtos.stream()
                .map(dto -> SendTarget.of(dto, EmailAddress.of(dto.resolveTargetData().get(TargetColumnEnum.TARGET_EMAIL))))
                .map(target -> target.applySendRequest(request))
                .toList();
        try {
            List<SendTarget> sendTargets = repository.saveAllAndFlush(targetList);
            successCount = (long) sendTargets.size();
            return new ArrayList<>();
        } catch (DataIntegrityViolationException e) {

            return targetList;
        }
    }
}
