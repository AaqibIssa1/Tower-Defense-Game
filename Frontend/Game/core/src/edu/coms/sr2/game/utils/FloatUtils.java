package edu.coms.sr2.game.utils;

public class FloatUtils {
    public static final float EPSILON = 0.0001f;
    public static boolean areEqual(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }
    public static boolean areEqual(float a, float b) {
        return areEqual(a, b, EPSILON);
    }
}
