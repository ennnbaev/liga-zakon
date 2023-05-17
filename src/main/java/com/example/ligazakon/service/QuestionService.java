package com.example.ligazakon.service;

import com.example.ligazakon.dto.QuestionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    List<QuestionDto> getTopQuestions(int limit);

    List<QuestionDto> getSimilarQuestions(String query, int count) throws InterruptedException;
}
