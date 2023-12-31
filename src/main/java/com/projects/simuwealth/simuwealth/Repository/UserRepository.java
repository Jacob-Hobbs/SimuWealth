package com.projects.simuwealth.simuwealth.Repository;

import com.projects.simuwealth.simuwealth.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
}
