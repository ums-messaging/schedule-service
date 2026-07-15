package com.ums.schedule.domain.message;

import com.ums.schedule.domain.request.SendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SendMessageJpaRepository extends JpaRepository<SendMessage, UUID> {
    Optional<SendMessage> findBySendRequest(SendRequest sendRequest);
}
