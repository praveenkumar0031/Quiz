package dev.com.quiz.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerResult {
    @NotBlank
    private Integer questionId;
    private boolean correct;
    private Integer answer;
    public AnswerResult(@NotBlank Integer questionId, boolean correct,Integer answer) {
        this.questionId = questionId;
        this.correct = correct;
        this.answer = answer;
    }
}
