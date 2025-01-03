package com.kun.plugin.publicexamtraining.ui.jcef.mapping;

import com.google.gson.Gson;
import com.kun.plugin.publicexamtraining.data.DataService;
import com.kun.plugin.publicexamtraining.data.dao.entity.QuestionEntity;
import com.kun.plugin.publicexamtraining.ui.jcef.RequestMapping;
import org.apache.commons.lang3.math.NumberUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import java.util.List;

@RequestMapping("loadQuestionList")
public class LoadQuestionRouterHandler extends CefMessageRouterHandlerAdapter {

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
                           boolean persistent, CefQueryCallback callback) {
        try {
            int paperId = NumberUtils.toInt(request, 0);
            List<QuestionEntity> questionEntityList = DataService.queryAllQuestions(paperId);
            callback.success(new Gson().toJson(questionEntityList));
            return true;
        } catch (Exception e) {
            //log output
            callback.failure(500, "执行失败");
        }
        return false;
    }

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long queryId) {
        super.onQueryCanceled(browser, frame, queryId);
    }


}
