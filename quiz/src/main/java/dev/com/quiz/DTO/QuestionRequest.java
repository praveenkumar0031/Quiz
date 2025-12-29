package dev.com.quiz.DTO;

import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    private String question;
    private List<String> options;
    private int answerIndex;
}

