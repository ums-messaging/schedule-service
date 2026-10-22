package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailSendRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetRequestEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.EmailSendRequestJpaRepository;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.SendTargetRequestJpaRepository;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.query.SendTargetRequestQueryRepository;
import com.ums.schedule.email.application.target.query.model.SendTargetGroupQuery;
import com.ums.schedule.email.common.code.EmailSendStatusCode;
import com.ums.schedule.email.domain.target.SendTargetRequest;
import com.ums.schedule.email.domain.target.SendTargetRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class SendTargetRequestJpaAdapter implements SendTargetRequestRepository {
    private final EmailSendRequestJpaRepository requestRepository;
    private final SendTargetRequestJpaRepository repository;
    private final SendTargetRequestQueryRepository queryRepository;

    @Override
    public List<SendTargetRequest> saveAll(Long requestId, EmailSendStatusCode status, List<SendTargetRequest> targets) {
        EmailSendRequestEntity findSendRequest = requestRepository.findByIdAndStatus(requestId, status.name()).orElseThrow();
        List<SendTargetRequestEntity> entities = targets.stream()
                .map(v -> {
                    SendTargetRequestEntity entity = v.toEntity();
                    entity.assignSendRequest(findSendRequest);
                    return entity;
                })
                .toList();
        List<SendTargetRequestEntity> saved = repository.saveAll(entities);
        return saved.stream()
                .map(SendTargetRequestEntity::toDomain)
                .toList();
    }

    @Override
    public void save(SendTargetRequest request) {
    }

    @Override
    public Optional<SendTargetRequest> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Map<Integer, List<SendTargetRequest>> findListAndGrouping(SendTargetGroupQuery query) {
        List<SendTargetRequestEntity> list = queryRepository.findListBySendRequestIdAndGroupId(query);
        return groupingList(list);
    }
    private Map<Integer, List<SendTargetRequest>> groupingList(List<SendTargetRequestEntity> targetList) {
        return IntStream.range(0, targetList.size())
                .boxed()
                .collect(Collectors.groupingBy(
                        idx -> idx / 20,
                        Collectors.mapping(
                                idx -> targetList.get(idx).toDomain(),
                                Collectors.toList()))
                );
    }
}
