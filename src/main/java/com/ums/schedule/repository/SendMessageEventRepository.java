package com.ums.schedule.repository;

import com.ums.schedule.domain.SendMessageEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMessageEventRepository extends JpaRepository<SendMessageEvent, Long> {

}
