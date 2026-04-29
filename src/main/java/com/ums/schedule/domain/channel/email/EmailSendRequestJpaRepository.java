package com.ums.schedule.domain.channel.email;

import com.ums.schedule.domain.request.SendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendRequestJpaRepository extends JpaRepository<EmailSendRequest, Long> {

}
