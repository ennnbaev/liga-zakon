package com.example.ligazakon.util;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

@Component
public class CalculatorSimilarity {
    private static final int MIN_LENGTH = 3;

    public long calculateSimilarity(String query, String questionText) {
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
}
