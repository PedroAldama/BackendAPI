package com.backendapi.controllers;

import com.backendapi.services.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public String getQuestion(){
        return questionService.generateQuestion();
    }

    @PostMapping("/answer")
    public String answerQuestion(@RequestParam String answer){
        return questionService.answerQuestion(answer);
    }
    @GetMapping("/answer/get")
    public String getAnswer(){
        return questionService.getAnswer();
    }
}
