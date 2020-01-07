package edu.coms.sr2.game.interfacing.views;

import edu.coms.sr2.game.utils.HtmlBuilder;

public interface TextView extends View {
    TextView setText(String text);
    String getText();
    TextView setTextFromHTML(String html);
    default TextView setTextFromHTML(HtmlBuilder htmlBuilder) {
        return setTextFromHTML(htmlBuilder.build());
    }
}
