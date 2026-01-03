package dev.com.quiz.service;

import dev.com.quiz.DTO.UserResponse;
import dev.com.quiz.Utils.PasswordUtil;


import dev.com.quiz.models.User;
import dev.com.quiz.repository.UserRepo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<UserResponse> findAll() {

        List<User> users=userRepo.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user:users){
            try {
                UserResponse userResponse = new UserResponse();
                userResponse.setUsername(user.getUsername());
                userResponse.setScore(user.getScore());
                userResponse.setUserid(user.getId());
                userResponses.add(userResponse);
            } catch (Exception e) {
                throw new RuntimeException("Failed to Get All Users", e);
            }
        }
        return userResponses;
    }


    public Optional<UserResponse> getById(Integer id) {
        return userRepo.findById(id)
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setUserid(user.getId());
                    response.setUsername(user.getUsername());
                    response.setScore(user.getScore());
                    return response;
                });

    }


    public UserResponse update(Integer id, User user) {
        return userRepo.findById(id)
                .map(existingUser -> {
                    UserResponse response = new UserResponse();
                    response.setUserid(existingUser.getId());
                    response.setUsername(existingUser.getUsername());
                    response.setScore(existingUser.getScore());
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(PasswordUtil.hashPassword(user.getPassword()));
                    existingUser.setIsAdmin(user.getIsAdmin());
                    userRepo.save(existingUser);
                    return response;
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

