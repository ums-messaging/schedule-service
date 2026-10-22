package com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.adapter;

import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.entity.ScheduleEntity;
import com.ums.schedule.orchestrator.adaptor.out.persistance.jpa.repository.ScheduleJpaRepository;
import com.ums.schedule.orchestrator.domain.schedule.Schedule;
import com.ums.schedule.orchestrator.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleJpaRepositoryAdapter implements ScheduleRepository {
    private final ScheduleJpaRepository repository;

    @Override
    public Optional<Schedule> findById(Long id) {
        return repository.findById(id).map(ScheduleEntity::toDomain);
    }
}
