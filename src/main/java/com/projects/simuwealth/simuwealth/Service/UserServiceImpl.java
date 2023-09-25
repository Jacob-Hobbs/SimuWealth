package com.projects.simuwealth.simuwealth.Service;

import com.projects.simuwealth.simuwealth.Entity.User;
import com.projects.simuwealth.simuwealth.Repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isEmailAlreadyInUse(String email) {
        User existingUser = userRepository.findByEmail(email);
        return existingUser != null;
    }

    @Override
    public void updatePasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user != null) {

            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }

    @Override
    public void updateUser(User currentUser) {
        userRepository.save(currentUser);
    }

    @Override
    public void addToWatchlist(User user, List<String> watchlist) {
        user.setWatchlist(watchlist);
        userRepository.save(user);
    }

}
