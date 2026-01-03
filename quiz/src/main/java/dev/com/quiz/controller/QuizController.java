package dev.com.quiz.controller;

import dev.com.quiz.DTO.*;

import dev.com.quiz.models.Question;

import dev.com.quiz.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
    @GetMapping("/questions")
    public List<QuestionResponse> getAll() throws Exception {
        return quizService.getAll();
    }

    @GetMapping("/question")
    public Optional<QuestionResponse> getQuestionById(@RequestParam int no){
        return quizService.getById(no);
    }
    @PostMapping("/question/add")
    public ResponseEntity<?> add(@RequestBody QuestionRequest request) {
        return quizService.addQuestion(request);
    }
    @PostMapping("/question/addAll")
    public ResponseEntity<?> addBulk(@RequestBody List<QuestionRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return ResponseEntity.badRequest().body("Question list cannot be empty");
        }

        quizService.addMultipleQuestions(requests);
        return ResponseEntity.ok("Questions added successfully");
    }
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(@Valid @RequestBody AnswerRequest request) {

        boolean correct = quizService.checkAnswer(request);

        return ResponseEntity.ok(Map.of(
                "questionId", request.getQuestionId(),
                "correct", correct
        ));
    }

    @PostMapping("/submit/batch")
    public ResponseEntity<Map<String, Object>> submitBatch(@RequestBody QuizSubmissionRequest request) {

        List<AnswerResult> results = quizService.checkMultipleAnswers(request);
        int score = quizService.calculateScore(results);

        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("score", score);


        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reset/{id}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Integer id) {
        quizService.deleteQuestion(id);
        return ResponseEntity.ok(Map.of("message", "Question deleted successfully"));
    }
    @DeleteMapping("/reset/all")
    public ResponseEntity<Map<String,String>> deleteAll() {
        quizService.deleteAllQuestions();
        return ResponseEntity.ok(Map.of("message", "All questions deleted"));
    }

}
