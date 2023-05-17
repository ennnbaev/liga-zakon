package com.example.ligazakon.mapper;

import com.example.ligazakon.dto.QuestionDto;
import com.example.ligazakon.model.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionDto toDto(Question question);

    List<QuestionDto> toDtoList(List<Question> questionList);
}
