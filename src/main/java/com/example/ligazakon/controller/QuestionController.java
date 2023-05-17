package com.example.ligazakon.controller;

import com.example.ligazakon.dto.QuestionDto;

import com.example.ligazakon.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/questions")
public record QuestionController(QuestionService questionService) {

    @GetMapping("/top/{count}")
    @Operation(summary = "Get top longest questions")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionDto> getTopQuestions(@PathVariable int count) {
        return questionService.getTopQuestions(count);
    }

    @GetMapping("/similar")
    @Operation(summary = "Get simillar question if not found create")
    @ApiResponse(responseCode = "200", description = "OK")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionDto> getSimilarQuestions(@Size(min = 1) @RequestParam String query, @RequestParam int count) throws InterruptedException {
        return questionService.getSimilarQuestions(query, count);
    }
}