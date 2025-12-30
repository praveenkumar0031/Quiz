package dev.com.quiz.DTO;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginRequest {
    private String username;
    private String password;
}

