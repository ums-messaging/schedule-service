package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.adapter;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.ScheduleEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestUploadEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.*;
import com.ums.schedule.orchestrator.application.requests.result.SendRequestSaveResult;
import com.ums.schedule.orchestrator.domain.event.SendRequest;
import com.ums.schedule.orchestrator.domain.event.SendRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class SendRequestJpaRepositoryAdapter implements SendRequestRepository {
    private final SendRequestUploadJpaRepository uploadRepository;
    private final ScheduleJpaRepository scheduleRepository;
    private final SendRequestEventJpaRepository repository;

    @Override
    public Optional<SendRequest> findById(Long id) {
        return repository.findById(id)
                .map(SendRequestEntity::toDomain);
    }

    @Override
    public boolean existsByCustomerIdAndCustomerKey(String customerId, String customerKey) {
        return repository.existsByCustomerIdAndCustomerKey(customerId, customerKey);
    }

    @Override
    public SendRequestSaveResult save(SendRequest domain, String objectKey) {
        SendRequestEntity entity = domain.toEntity();

        ScheduleEntity schedule = scheduleRepository.findById(domain.getScheduleId()).orElseThrow();
        SendRequestUploadEntity requestUpload = uploadRepository.findByCustomerIdAndObjectKeyEndsWith(domain.customerId(), objectKey).orElseThrow();

        entity.assignSchedule(schedule);
        entity.assignSendRequestUpload(requestUpload);

        SendRequestEntity saved = repository.save(entity);
        return Optional.ofNullable(saved)
                .map(SendRequestEntity::toDomain)
                .map(v -> SendRequestSaveResult.of(schedule.toDomain(), v))
                .orElseThrow();
    }

    @Override
    public void updateStatus(SendRequest sendRequest) {
        SendRequestEntity entity = repository.findById(sendRequest.getId()).orElseThrow();
        entity.updateStatus(sendRequest);
        repository.save(entity);
    }
}
