package com.kun.plugin.publicexamtraining.ui;

import com.intellij.util.ui.HtmlPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class RandomTrainHtmlPanel extends HtmlPanel {

    private final String html;

    public RandomTrainHtmlPanel(String html) {
        this.html = html;
    }


    @Override
    protected @NotNull @Nls String getBody() {
        return "";
    }



}
