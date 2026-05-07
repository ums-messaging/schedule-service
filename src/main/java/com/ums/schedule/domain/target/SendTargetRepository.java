package com.ums.schedule.domain.target;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SendTargetRepository extends JpaRepository<SendTarget, UUID> {
}
