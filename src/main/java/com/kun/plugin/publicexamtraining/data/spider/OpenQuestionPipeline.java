package com.kun.plugin.publicexamtraining.data.spider;

import com.google.common.collect.Lists;
import com.kun.plugin.publicexamtraining.common.DataConstants;
import com.kun.plugin.publicexamtraining.data.dao.H2DbManager;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.data.model.Question;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OpenQuestionPipeline implements Pipeline {
    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<Question> questionList = resultItems.get(DataConstants.QUESTION_PARAM);
        List<Question> answerList = resultItems.get(DataConstants.ANSWER_PARAM);
        List<Question> explainList = resultItems.get(DataConstants.EXPLAIN_PARAM);
        if (questionList == null || questionList.isEmpty()) {
            return;
        }
        Map<String, String> answerMap = Optional.ofNullable(answerList).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(Question::getNumber, QuestionEntity::getAnswer));
        Map<String, String> explainMap = Optional.ofNullable(explainList).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(Question::getNumber, QuestionEntity::getAnswerAnalysis));
        List<QuestionEntity> questionEntityList = Lists.transform(questionList, question -> {
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setPaperId(question.getPaperId());
            questionEntity.setSource(question.getSource());
            questionEntity.setQuestionType(question.getQuestionType());
            questionEntity.setQuestionStem(question.getQuestionStem());
            questionEntity.setAnswerSelect(question.getAnswerSelect());
            questionEntity.setAnswer(answerMap.get(question.getNumber()));
            questionEntity.setAnswerAnalysis(explainMap.get(question.getNumber()));
            return questionEntity;
        });
        H2DbManager.insertQuestionBatch(questionEntityList);

    }
}
