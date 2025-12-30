package dev.com.quiz.service;

import dev.com.quiz.Utils.PasswordUtil;

import dev.com.quiz.models.User;
import dev.com.quiz.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {

    private final UserRepo userRepo;


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;

    }


    public User save(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public Optional<User> getById(Integer id) {
        return userRepo.findById(id);
    }


    public User update(Integer id, User user) {
        return userRepo.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setIsAdmin(user.getIsAdmin());
                    return userRepo.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }


    public void delete(Integer id) {
        userRepo.deleteById(id);
    }


    public boolean isAdmin(Integer id) {
        return userRepo.findById(id)
                .map(User::getIsAdmin)
                .orElse(false);
    }

    public String getPasswordFromDB(String username) {
        return userRepo.findByUsername(username)
                .map(User::getPassword)
                .orElseThrow(() -> new RuntimeException("User not found with name " +username));
    }
}

