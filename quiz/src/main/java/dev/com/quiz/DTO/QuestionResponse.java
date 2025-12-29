package dev.com.quiz.DTO;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {
    private int id;
    private String question;
    private List<String> options;
}

