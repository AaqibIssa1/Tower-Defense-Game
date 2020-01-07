package edu.coms.sr2.game.utils;


public class StringUtils {
    public static String toSnakeCase(String string) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < string.length(); ++i)
        {
            char c = string.charAt(i);
            if(Character.isUpperCase(c))
                builder.append('_').append(Character.toLowerCase(c));
            else
                builder.append(c);
        }
        return builder.toString();
    }
}
