package com.ums.schedule.email.adaptor.out.persistence.jpa.adapter;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.SendTargetGroupEventEntity;
import com.ums.schedule.email.adaptor.out.persistence.jpa.repository.SendTargetGroupEventJpaRepository;
import com.ums.schedule.email.domain.event.SendTargetGroupEvent;
import com.ums.schedule.email.domain.event.SendTargetGroupEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SendTargetGroupEventJpaAdapter implements SendTargetGroupEventRepository {
    private final SendTargetGroupEventJpaRepository repository;

    @Override
    public void saveAll(List<SendTargetGroupEvent> eventList) {
        // 엔티티 변경 후 저장
    }

    @Override
    public void save(SendTargetGroupEvent event) {
        SendTargetGroupEventEntity entity = event.toEntity();
        repository.save(entity);
    }
}
