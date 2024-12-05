package com.kun.plugin.publicexamtraining.data.spider;

import com.google.common.collect.Lists;
import com.kun.plugin.publicexamtraining.common.Constants;
import com.kun.plugin.publicexamtraining.data.dao.H2DbManager;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.data.model.Question;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

public class OpenQuestionPipeline implements Pipeline {
    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<Question> list = resultItems.get(Constants.QUESTION_PARAM);
        if (list == null || list.isEmpty()) {
            return;
        }
        List<QuestionEntity> questionEntityList = Lists.transform(list, question -> {
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setPaperId(question.getPaperId());
            questionEntity.setSource(question.getSource());
            questionEntity.setQuestionType(question.getQuestionType());
            questionEntity.setQuestionStem(question.getQuestionStem());
            questionEntity.setAnswerSelect(question.getAnswerSelect());
            questionEntity.setAnswer(question.getAnswer());
            questionEntity.setAnswerAnalysis(question.getAnswerAnalysis());
            return questionEntity;
        });
        H2DbManager.insertQuestionBatch(questionEntityList);

    }
}
