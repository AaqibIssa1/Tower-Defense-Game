package edu.coms.sr2.game.utils;

public class HtmlBuilder {
    private String str;
    public HtmlBuilder(String source) {
        this.str = source;
    }

    public String build() {
        return str;
    }

    public static String addColor(String str, String colorHex) {
        return "<font color='" + (colorHex.startsWith("#") ? "" : "#") + colorHex + "'>" + str + "</font>";
    }

    public HtmlBuilder addColor(String colorHex) {
        str = addColor(str, colorHex);
        return this;
    }

    public static String addBold(String str) {
        return "<b>" + str + "</b>";
    }

    public HtmlBuilder addBold() {
        str = addBold(str);
        return this;
    }

    public static String addItalics(String str) {
        return "<i>" + str + "</i>";
    }

    public HtmlBuilder addItalics() {
        str = addItalics(str);
        return this;
    }
}
