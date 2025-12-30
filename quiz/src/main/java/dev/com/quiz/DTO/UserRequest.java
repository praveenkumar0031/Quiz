package dev.com.quiz.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class UserRequest {
    private Integer id;
    private String username;
    private String password;
    private boolean isAdmin;
}
@Data
class UserResponse{
    private String username;
}

