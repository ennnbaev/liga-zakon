package com.example.ligazakon.service;

import com.example.ligazakon.dto.QuestionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {
    List<QuestionDto> getTopQuestions(int limit);

    List<QuestionDto> getSimilarQuestionsOrCreateIfNotExist(String query, int count) throws InterruptedException;
}
