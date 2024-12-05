package com.kun.plugin.publicexamtraining.util;

import java.util.Set;

public class QuestionTypeHelper {

    public static String getQuestionType(String text) {
        String resultType = "";
        if (text == null || text.isEmpty()) {
            return resultType;
        }
        double maxScore = 0.35;
        for (QuestionType questionType : QuestionType.values()) {
            double score = TextMatcher.calculateMatchScore(text, Set.of(questionType.getName()));
            if (score > maxScore) {
                maxScore = score;
                resultType = questionType.getName();
            }
        }

        return resultType;

    }

    public enum  QuestionType {
        AXIOM("常识判断"),
        UNDERSTANDING("言语理解与表达"),
        QUANTITY("数量关系"),
        REASONING("判断推理"),
        DATA("资料分析");
        private final String name;
        QuestionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


}
