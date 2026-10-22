package com.ums.schedule.email.adaptor.out.persistence.jpa.repository;

import com.ums.schedule.email.adaptor.out.persistence.jpa.entity.EmailAttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailAttachmentJpaRepository extends JpaRepository<EmailAttachmentEntity, UUID> {
}
