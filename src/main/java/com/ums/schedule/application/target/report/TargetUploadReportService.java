package com.ums.schedule.application.target.report;

import com.ums.schedule.domain.target.SendTargetRepository;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TargetUploadReportService {

    @Transactional
    public void reportingAndOnCompleted(TargetUploadReport targetUpload) {
    }
}
