package com.manateam.glengine3.utils;

public class MathUtils {
    public static float sq(float a) {
        return a * a;
    }

    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    public static float pow(float a, float b) {
        return (float) Math.pow(a, b);
    }

    public static float getDirection(float px, float py, float tx, float ty) {
        //returns direction to point in radinas. Calculated from north direction (0, top of screen) along the hour line move direction
        float a;
        a = (degrees((atan((py - ty) / (px - tx)))) + 180);
        if (tx <= px) {
            a += 180;
        }
        a += 180;
        a %= 360;
        return (radians(a));
    }

    public static float degrees(float a) {
        return ((float) Math.toDegrees(a));
    }

    public static float radians(float a) {
        return ((float) Math.toRadians(a));
    }

    public static float atan(float a) {
        return ((float) Math.atan(a));
    }

    public static float atan2(float a, float b) {
        return ((float) Math.atan2(a, b));
    }

    public static float sin(float a) {
        return ((float) Math.sin(a));
    }

    public static float cos(float a) {
        return ((float) Math.cos(a));
    }

    public static float abs(float a) {
        return ((float) Math.abs(a));
    }

    public static float min(float a, float b) {
        if (a < b) {
            return a;
        }
        return b;
    }

    public static float max(float a, float b) {
        return Math.max(a, b);
    }

    public static float max(float a, float b, float c, float d) {
        return (max(max(a, b), max(c, d)));
    }

    public static float tg(float a) {
        return ((float) Math.tan(a));
    }
}
