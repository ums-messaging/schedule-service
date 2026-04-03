package com.ums.schedule.send.domain.target.repository;

import com.ums.schedule.send.domain.target.SendTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendTargetRepository extends JpaRepository<SendTarget, Long> {
}
