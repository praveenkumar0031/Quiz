package dev.com.quiz.repository;

import dev.com.quiz.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByScore(Integer score);

}
