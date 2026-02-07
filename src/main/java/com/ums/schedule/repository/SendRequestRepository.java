package com.ums.schedule.repository;

import com.ums.schedule.domain.SendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestRepository extends JpaRepository<SendRequest, Long> {
}
