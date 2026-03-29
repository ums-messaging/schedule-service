package com.ums.schedule.repository;

import com.ums.schedule.send.domain.report.SendRequestReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendRequestReportRepository extends JpaRepository<SendRequestReport, Long> {
}
