package com.projects.simuwealth.simuwealth.Repository;

import com.projects.simuwealth.simuwealth.Entity.Stock;
import com.projects.simuwealth.simuwealth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findByUser(User currentUser);
}
