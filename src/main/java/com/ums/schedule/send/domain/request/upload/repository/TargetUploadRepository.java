package com.ums.schedule.send.domain.request.upload.repository;

import com.ums.schedule.send.domain.request.upload.TargetUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetUploadRepository extends JpaRepository<TargetUpload, Long> {
}
