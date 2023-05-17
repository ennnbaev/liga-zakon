package com.example.ligazakon.repo;

import com.example.ligazakon.model.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends ElasticsearchRepository<Question, Long> {
    List<Question> findTopNByOrderByQuestionTextDesc(@Param("n") int n);
}
