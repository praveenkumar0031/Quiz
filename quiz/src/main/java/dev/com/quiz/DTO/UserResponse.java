package dev.com.quiz.DTO;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


@Data
public class UserResponse{
    private Integer userid;
    private String username;
    private Integer score;

}