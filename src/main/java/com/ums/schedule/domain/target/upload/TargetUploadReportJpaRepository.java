package com.ums.schedule.domain.target.upload;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TargetUploadReportJpaRepository extends JpaRepository<TargetUploadReport, UUID> {
}
