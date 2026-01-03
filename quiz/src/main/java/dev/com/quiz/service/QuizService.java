package dev.com.quiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.com.quiz.DTO.*;
import dev.com.quiz.models.Question;
import dev.com.quiz.models.User;
import dev.com.quiz.repository.QesRepo;
import dev.com.quiz.repository.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class QuizService {

    private final QesRepo quizDb;
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserRepo userDb;
    public QuizService(QesRepo quizDb, UserRepo userDb) {
        this.quizDb = quizDb;
        this.userDb = userDb;
    }

    public Optional<QuestionResponse> getById(Integer id) {
        return quizDb.findById(id).map(qes -> {
            QuestionResponse dto = new QuestionResponse();
            dto.setId(qes.getId());
            dto.setQuestion(qes.getQuestion());

            try {
                dto.setOptions(
                        mapper.readValue(
                                qes .getOptions(),
                                new TypeReference<List<String>>() {}
                        )
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse options JSON", e);
            }

            return dto;
        });
    }

    public ResponseEntity<?> addQuestion(QuestionRequest request) {
        try {
            User user = userDb.findById(request.getUserId())
                    .orElseThrow(() ->
                            new RuntimeException("User not found: " + request.getUserId()));
            Question q = new Question();
            if(user.getIsAdmin()) {
                q.setQuestion(request.getQuestion());
                q.setOptions(
                        mapper.writeValueAsString(request.getOptions())
                );
                q.setAnswer(request.getAnswer());
                quizDb.save(q);
                return ResponseEntity.ok("Question added");
            }
            return ResponseEntity.ok("Required Access");

        } catch (Exception e) {
            throw new RuntimeException("Failed to save question", e);
        }
    }

    public List<Question> addMultipleQuestions(List<QuestionRequest> requests) {


        List<Question> questions = new ArrayList<>();

        for (QuestionRequest request : requests) {
            try {
                Question q = new Question();
                q.setQuestion(request.getQuestion());
                q.setOptions(mapper.writeValueAsString(request.getOptions()));
                q.setAnswer(request.getAnswer());


                questions.add(q);

            } catch (Exception e) {
                throw new RuntimeException("Failed to parse options JSON", e);
            }
        }

        return quizDb.saveAll(questions);
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


        return question.getAnswer() == request.getSelectedIndex();
    }
    public List<AnswerResult> checkMultipleAnswers(QuizSubmissionRequest request) {

        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new RuntimeException("Answer list cannot be empty");
        }

        User user = userDb.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + request.getUserId()));

        List<AnswerResult> results = new ArrayList<>();
        int score = 0;

        for (AnswerRequest ans : request.getAnswers()) {

            if (ans.getQuestionId() == null) {
                throw new RuntimeException("Question ID cannot be null");
            }

            if (ans.getSelectedIndex() == null) {
                throw new RuntimeException("Selected option cannot be null");
            }

            Question q = quizDb.findById(ans.getQuestionId())
                    .orElseThrow(() ->
                            new RuntimeException("Question not found: " + ans.getQuestionId()));

            boolean correct = q.getAnswer().equals(ans.getSelectedIndex());

            if (correct) {
                score++;
            }

            results.add(
                    new AnswerResult(
                            ans.getQuestionId(),
                            correct,
                            q.getAnswer()
                    )
            );
        }


        user.setScore(user.getScore() + score);
        userDb.save(user);

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

