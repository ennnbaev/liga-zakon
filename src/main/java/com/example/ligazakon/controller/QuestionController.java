package com.example.ligazakon.controller;

import com.example.ligazakon.dto.QuestionDto;

import com.example.ligazakon.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/top/{count}")
    @Operation(summary = "Get top longest questions")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionDto> getTopQuestions(@PathVariable int count) {
        return questionService.getTopQuestions(count);
    }

    @PostMapping("/similar")
    @Operation(summary = "Get simillar question if not found create")
    @ApiResponse(responseCode = "200", description = "OK")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionDto> getSimilarQuestionsOrCreateIfNotExist(@Size(min = 1) @RequestParam String query, @RequestParam int count) throws InterruptedException {
        return questionService.getSimilarQuestionsOrCreateIfNotExist(query, count);
    }
}