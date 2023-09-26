package com.projects.simuwealth.simuwealth.Repository;

import com.projects.simuwealth.simuwealth.Entity.Report;
import com.projects.simuwealth.simuwealth.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
