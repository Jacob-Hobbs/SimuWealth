package com.projects.simuwealth.simuwealth.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "report")
public class Report {

    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int report_Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "totalFunds")
    private double totalFunds;

    @Column(name = "totalReturn")
    private double totalReturn;

    @Column(name = "percentReturn")
    private String percentReturn;

    @Column(name = "reportDate")
    private LocalDateTime reportDate;

    public Report() {
    }

    public Report(int report_Id, User user, double totalFunds, double totalReturn, String percentReturn, LocalDateTime reportDate) {
        this.report_Id = report_Id;
        this.user = user;
        this.totalFunds = totalFunds;
        this.totalReturn = totalReturn;
        this.percentReturn = percentReturn;
        this.reportDate = reportDate;
    }

    public int getReport_Id() {
        return report_Id;
    }

    public void setReport_Id(int report_Id) {
        this.report_Id = report_Id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalFunds() {
        return totalFunds;
    }

    public void setTotalFunds(double totalFunds) {
        this.totalFunds = totalFunds;
    }

    public double getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(double totalReturn) {
        this.totalReturn = totalReturn;
    }

    public String getPercentReturn() {
        return percentReturn;
    }

    public void setPercentReturn(String percentReturn) {
        this.percentReturn = percentReturn;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    public String toString() {
        return "Report{" +
                "report_Id=" + report_Id +
                ", user=" + user +
                ", totalFunds=" + totalFunds +
                ", totalReturn=" + totalReturn +
                ", percentReturn='" + percentReturn + '\'' +
                ", reportDate=" + reportDate +
                '}';
    }
}
