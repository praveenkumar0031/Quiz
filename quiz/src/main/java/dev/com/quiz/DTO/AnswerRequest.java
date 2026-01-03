package dev.com.quiz.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerRequest {
    @NotBlank
    private Integer questionId;
    private Integer selectedIndex;
}
