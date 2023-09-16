package com.projects.simuwealth.simuwealth.Service;

import com.projects.simuwealth.simuwealth.Entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public void registerUser(User user);

    boolean isEmailAlreadyInUse(String email);

    void updatePasswordByEmail(String email, String newPassword);
}
