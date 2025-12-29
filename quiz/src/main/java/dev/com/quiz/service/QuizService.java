package dev.com.quiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.com.quiz.DTO.*;
import dev.com.quiz.models.Question;
import dev.com.quiz.repository.QesRepo;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;@Service
public class QuizService {

    private final QesRepo quizDb;
    private final ObjectMapper mapper = new ObjectMapper();

    public QuizService(QesRepo quizDb) {
        this.quizDb = quizDb;
    }

    public Optional<Question> getById(Integer id) {
        return quizDb.findById(id);
    }

    public Question addQuestion(QuestionRequest request) {
        try {
            Question q = new Question();
            q.setQuestion(request.getQuestion());
            q.setOptions(
                    mapper.writeValueAsString(request.getOptions())
            );
            q.setAnswerIndex(request.getAnswerIndex());

            return quizDb.save(q);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save question", e);
        }
    }

    public List<QuestionResponse> getAll() {

        return quizDb.findAll().stream().map(q -> {
            QuestionResponse dto = new QuestionResponse();
            dto.setId(q.getId());
            dto.setQuestion(q.getQuestion());

            try {
                dto.setOptions(
                        mapper.readValue(
                                q.getOptions(),
                                new TypeReference<List<String>>() {}
                        )
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse options JSON", e);
            }

            return dto;
        }).toList();
    }
    public boolean checkAnswer(AnswerRequest request) {
        Question question = quizDb.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));


        return question.getAnswerIndex() == request.getSelectedIndex();
    }

    public List<AnswerResult> checkMultipleAnswers(QuizSubmissionRequest request) {
        List<AnswerResult> results = new ArrayList<>();

        for (AnswerRequest ans : request.getAnswers()) {
            if (ans.getQuestionId() == null) {
                throw new RuntimeException("Question ID cannot be null");
            }

            Question q = quizDb.findById(ans.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found: " + ans.getQuestionId()));

            boolean correct = q.getAnswerIndex() == ans.getSelectedIndex();
            results.add(new AnswerResult(ans.getQuestionId(), correct));
        }

        return results;
    }

    public void deleteQuestion(Integer questionId) {
        if (!quizDb.existsById(questionId)) {
            throw new RuntimeException("Question not found with ID: " + questionId);
        }
        quizDb.deleteById(questionId);
    }
    public void deleteAllQuestions() {
        quizDb.deleteAll();
    }


    public int calculateScore(List<AnswerResult> results) {
        return (int) results.stream().filter(AnswerResult::isCorrect).count();
    }
}

