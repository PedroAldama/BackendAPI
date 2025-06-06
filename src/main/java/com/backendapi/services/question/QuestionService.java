package com.backendapi.services.question;

public interface QuestionService {
    String generateQuestion();
    String answerQuestion(String answer);
    String getAnswer();
}
