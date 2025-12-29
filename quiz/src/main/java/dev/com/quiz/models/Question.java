package dev.com.quiz.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "questions")
@Data

public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name="question")
    @Getter()
    private String question;

    @Column(columnDefinition = "json")
    private String options;

    private int answerIndex;
    public Question() {
    }


}
