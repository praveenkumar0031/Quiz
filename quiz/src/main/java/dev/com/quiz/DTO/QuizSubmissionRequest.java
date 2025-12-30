package dev.com.quiz.DTO;

import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionRequest {
    private List<AnswerRequest> answers;
    private Integer userId;

}

