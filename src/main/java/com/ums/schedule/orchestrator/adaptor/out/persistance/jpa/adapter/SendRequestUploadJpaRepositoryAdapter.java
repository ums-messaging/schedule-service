package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.adapter;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.SendRequestUploadEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.SendRequestUploadJpaRepository;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.SendRequestUploadQueryRepository;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUpload;
import com.ums.schedule.orchestrator.domain.upload.SendRequestUploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
@Slf4j
@RequiredArgsConstructor
public class SendRequestUploadJpaRepositoryAdapter implements SendRequestUploadRepository {
    private final SendRequestUploadJpaRepository repository;

    @Override
    public Optional<SendRequestUpload> findById(Long id) {
        return repository.findById(id)
                .map(SendRequestUploadEntity::toDomain);
    }

    @Override
    public SendRequestUpload save(SendRequestUpload upload) {
        SendRequestUploadEntity entity = upload.toEntity();
        SendRequestUploadEntity saved = repository.save(entity);
        return saved.toDomain();
    }

    @Override
    public SendRequestUpload findByCustomerIdAndObjectKey(String customerId, String objectkey) {
        SendRequestUploadEntity entity = repository.findByCustomerIdAndObjectKeyEndsWith(customerId, objectkey).orElseThrow();
        return entity.toDomain();
    }
}
