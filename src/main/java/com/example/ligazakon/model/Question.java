package com.example.ligazakon.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import lombok.Data;

@Document(indexName = "question")
@Data
public class Question {
    public static final String QUESTION_KEYWORD = "keyword";
    public static final String QUESTION_FIELD = "questionText";
    @Id
    private Long id;
    @MultiField(
            mainField = @Field(type = FieldType.Text, name = QUESTION_FIELD),
            otherFields = @InnerField(suffix = QUESTION_KEYWORD, type = FieldType.Keyword)
    )
    private String questionText;
}
