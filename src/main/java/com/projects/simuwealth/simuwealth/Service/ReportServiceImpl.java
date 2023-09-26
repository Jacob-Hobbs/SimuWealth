package com.projects.simuwealth.simuwealth.Service;

import com.projects.simuwealth.simuwealth.Entity.Report;
import com.projects.simuwealth.simuwealth.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Override
    public List<Report> getAllReports() {

        return reportRepository.findAll();
    }

    @Override
    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    @Override
    public void deleteReportById(int reportId) {
        reportRepository.delete(reportRepository.getReferenceById(reportId));
    }


}
