package com.ums.schedule.domain.request.report;

import com.ums.schedule.domain.request.report.SendRequestReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestReportRepository extends JpaRepository<SendRequestReport, Long> {
}
