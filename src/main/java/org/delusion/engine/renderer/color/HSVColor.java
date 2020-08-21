package org.delusion.engine.renderer.color;

public class HSVColor {

    private float h,s,v;

    public HSVColor(float h, float s, float v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }

    @Override
    public String toString() {
        return "HSVColor{" +
                "h=" + h +
                ", s=" + s +
                ", v=" + v +
                '}';
    }

    public Color toColor() {
        return Color.rgb(java.awt.Color.HSBtoRGB(h,s,v));
    }

    public float H() {
        return h;
    }

    public float S() {
        return s;
    }

    public float V() {
        return v;
    }
}
