package com.ums.schedule.send.domain.target.repository;

import com.ums.schedule.send.domain.target.EmailSendTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendTargetRepository extends JpaRepository<EmailSendTarget, Long> {
}
