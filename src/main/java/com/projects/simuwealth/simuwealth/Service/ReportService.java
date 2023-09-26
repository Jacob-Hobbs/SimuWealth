package com.projects.simuwealth.simuwealth.Service;

import com.projects.simuwealth.simuwealth.Entity.Report;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ReportService {
    List<Report> getAllReports();

    void saveReport(Report report);

    void deleteReportById(int reportId);
}
