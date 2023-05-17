package com.example.ligazakon.service.impl;

import com.example.ligazakon.dto.QuestionDto;
import com.example.ligazakon.mapper.QuestionMapper;
import org.apache.commons.text.similarity.LevenshteinDistance;

import com.example.ligazakon.model.Question;
import com.example.ligazakon.repo.QuestionRepository;
import com.example.ligazakon.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ExecutorService executorService;
    private static final int MIN_LENGTH = 3;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper, QuestionRepository questionRepository, ElasticsearchOperations elasticsearchOperations) {
        this.questionMapper = questionMapper;
        this.questionRepository = questionRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        int numThreads = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    @Override
    public List<QuestionDto> getTopQuestions(int limit) {
        return questionMapper.toDtoList(questionRepository.findTopNByOrderByQuestionTextDesc(limit));
    }

    @Override
    public List<QuestionDto> getSimilarQuestions(String query, int count) throws InterruptedException {
        final var prefixQuery = NativeQuery.builder()
                .withQuery(q -> q.prefix(prefix -> prefix.field(Question.QUESTION_FIELD + "." + Question.QUESTION_KEYWORD)
                        .value(query.split(" ")[0])))
                .getQuery();

        final var mostLikeThisQuery = NativeQuery.builder()
                .withQuery(q -> q.moreLikeThis(mlt -> mlt.fields(Question.QUESTION_FIELD)
                        .like(like -> like.text(query))))
                .getQuery();

        final var boolQuery = NativeQuery.builder()
                .withQuery(q -> q.bool(bool -> bool.must(List.of(prefixQuery, mostLikeThisQuery))))
                .build();
        SearchHits<Question> searchHits = elasticsearchOperations.search(boolQuery, Question.class);
        List<Question> similarQuestions = new ArrayList<>();
        searchHits.forEach(hit -> similarQuestions.add(hit.getContent()));
        if (similarQuestions.isEmpty()) {
            Question newQuestion = new Question();
            newQuestion.setQuestionText(query);
            questionRepository.save(newQuestion);
            return List.of(questionMapper.toDto(newQuestion));
        }
        List<Callable<Long>> similarityTasks = similarQuestions.stream()
                .map(q -> (Callable<Long>) () -> calculateSimilarity(query, q.getQuestionText()))
                .toList();
        List<Future<Long>> similarityResults = executorService.invokeAll(similarityTasks);
        return questionMapper.toDtoList(similarQuestions.stream()
                .sorted(Comparator.comparingDouble(q -> getSimilarityResult(similarityResults, q)))
                .limit(count)
                .toList());
    }

    private long calculateSimilarity(String query, String questionText) {
        String[] queryWords = query.split(" ");
        String[] questionWords = questionText.split(" ");
        int matchingWords = 0;
        for (String queryWord : queryWords) {
            if (queryWord.length() > MIN_LENGTH) {
                for (String questionWord : questionWords) {
                    if (questionWord.length() > MIN_LENGTH && isLevenshteinSimilar(queryWord, questionWord)) {
                        matchingWords++;
                        break;
                    }
                }
            }
        }
        return matchingWords / queryWords.length;
    }

    private boolean isLevenshteinSimilar(String word1, String word2) {
        int maxDistance = word1.length() / MIN_LENGTH;
        return LevenshteinDistance.getDefaultInstance().apply(word1, word2) < maxDistance;
    }

    private long getSimilarityResult(List<Future<Long>> similarityResults, Question question) {
        try {
            for (Future<Long> result : similarityResults) {
                if (question.getId().equals(result.get())) {
                    return result.get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
