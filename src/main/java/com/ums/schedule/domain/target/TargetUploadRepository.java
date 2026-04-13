package com.ums.schedule.domain.target;

import com.ums.schedule.domain.target.upload.TargetUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetUploadRepository extends JpaRepository<TargetUpload, Long> {
}
