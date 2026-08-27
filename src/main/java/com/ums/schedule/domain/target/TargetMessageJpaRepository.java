package com.ums.schedule.domain.target;

import com.ums.schedule.domain.target.TargetMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TargetMessageJpaRepository extends JpaRepository<TargetMessage, Long> {
}
