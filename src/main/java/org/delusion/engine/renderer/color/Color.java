package org.delusion.engine.renderer.color;

import java.util.HashMap;
import java.util.Map;

public class Color {
    private float r,g,b,a;
    private long rgba8888;

    public Color(long rgba8888) {
        this.rgba8888 = rgba8888;
        r = ((rgba8888 >> 24) & 0xFF) / (float)0xFF;
        g = ((rgba8888 >> 16) & 0xFF) / (float)0xFF;
        b = ((rgba8888 >>  8) & 0xFF) / (float)0xFF;
        a = ((rgba8888      ) & 0xFF) / (float)0xFF;
    }

    public static Color rgb(int rgb) {
        return new Color((rgb << 8) + 0xFF);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                ", rgba8888=" + rgba8888 +
                '}';
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        short red = (short) (r * 0xFF);
        short green = (short) (g * 0xFF);
        short blue = (short) (b * 0xFF);
        short alpha = (short) (a * 0xFF);

        rgba8888 = red;
        rgba8888 = (rgba8888 << 8) + green;
        rgba8888 = (rgba8888 << 8) + blue;
        rgba8888 = (rgba8888 << 8) + alpha;
    }

    public Color(short red, short green, short blue, short alpha) {

        this.r = red / (float)0xff;
        this.g = green / (float)0xff;
        this.b = blue / (float)0xff;
        this.a = alpha / (float)0xff;

        rgba8888 = red;
        rgba8888 = (rgba8888 << 8) + green;
        rgba8888 = (rgba8888 << 8) + blue;
        rgba8888 = (rgba8888 << 8) + alpha;
    }

    public Color cpy() {
        return new Color(rgba8888);
    }

    public void set(long rgba8888) {
        this.rgba8888 = rgba8888;
        r = ((rgba8888 >> 24) & 0xFF) / (float)0xFF;
        g = ((rgba8888 >> 16) & 0xFF) / (float)0xFF;
        b = ((rgba8888 >>  8) & 0xFF) / (float)0xFF;
        a = ((rgba8888      ) & 0xFF) / (float)0xFF;
        update();
    }

    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        short red = (short) (r * 0xFF);
        short green = (short) (g * 0xFF);
        short blue = (short) (b * 0xFF);
        short alpha = (short) (a * 0xFF);

        rgba8888 = red;
        rgba8888 = (rgba8888 >> 8) + green;
        rgba8888 = (rgba8888 >> 8) + blue;
        rgba8888 = (rgba8888 >> 8) + alpha;

        update();
    }

    public void set(short red, short green, short blue, short alpha) {

        this.r = red / (float)0xff;
        this.g = green / (float)0xff;
        this.b = blue / (float)0xff;
        this.a = alpha / (float)0xff;

        rgba8888 = red;
        rgba8888 = (rgba8888 >> 8) + green;
        rgba8888 = (rgba8888 >> 8) + blue;
        rgba8888 = (rgba8888 >> 8) + alpha;
        update();
    }

    public void set(Color other) {
        r = other.r;
        g = other.g;
        b = other.b;
        a = other.a;
        rgba8888 = other.rgba8888;
        update();
    }

    private void update() {
        trackers.values().forEach(ColorTracker::onUpdate);
    }

    private Map<String, ColorTracker> trackers = new HashMap<>();

    public void addTracker(String name, ColorTracker tracker) {
        trackers.put(name,tracker);
    }

    public void clearTracker(String s) {
        trackers.remove(s);
    }

    public float R() {
        return r;
    }

    public float G() {
        return g;
    }

    public float B() {
        return b;
    }

    public float A() {
        return a;
    }

    public long RGBA8888() {
        return rgba8888;
    }
}
