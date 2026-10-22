package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetErrorEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.SendTargetErrorJpaRepository;
import com.ums.schedule.email.domain.target.SendTargetError;
import com.ums.schedule.email.domain.target.SendTargetErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SendTargetErrorJpaAdapter implements SendTargetErrorRepository {
    private final SendTargetErrorJpaRepository repository;

    @Override
    public void saveAll(List<SendTargetError> errorList) {
        List<SendTargetErrorEntity> entityList = errorList.stream().map(SendTargetError::toEntity)
                .toList();
        repository.saveAllAndFlush(entityList);
    }

    @Override
    public int countRequestFailCount(Long sendRequestId) {
        return (int) repository.countBySendRequestId(sendRequestId);
    }
}