package com.kun.plugin.publicexamtraining.util;

import java.util.Set;
import java.util.stream.Collectors;

public class TextMatcher {

    // 计算Levenshtein距离
    public static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1));
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    // 计算匹配值
    public static double calculateMatchScore(String text, Set<String> words) {
        if (text == null || text.isEmpty() || words == null || words.isEmpty()) {
            return 0.0;
        }
        //文本去空格
        text = text.replaceAll("\\s+", "");
        text = text.toLowerCase();
        words = words.stream().map(String::toLowerCase).map(String::trim).collect(Collectors.toSet());

        double totalScore = 0;
        for (String word : words) {
            double wordScore = calculateWordScore(text, word);
            totalScore += wordScore;
        }

        return totalScore / words.size();
    }

    private static double calculateWordScore(String text, String word) {
        if (text.contains(word)) {
            return 1.0; // 如果词完全匹配，直接返回1
        }

        double maxScore = 0;
        int windowSize = Math.min(word.length() * 2, text.length());

        for (int i = 0; i <= text.length() - windowSize; i++) {
            String subText = text.substring(i, i + windowSize);
            int distance = levenshteinDistance(word, subText);
            double score = 1 - (double) distance / Math.max(word.length(), subText.length());
            maxScore = Math.max(maxScore, score);
        }

        return maxScore;
    }


}

