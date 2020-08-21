package org.delusion.engine.color;

import org.delusion.engine.renderer.color.Color;
import org.delusion.engine.renderer.color.GradientSeries;

import java.util.Arrays;
import java.util.List;

public class ComplexGradientSeries {
    private final List<GradientSeries> series;
    private final float end_pos;
    private float position;

    public ComplexGradientSeries(GradientSeries... gradientSeries) {
        series = Arrays.asList(gradientSeries);
        end_pos = series.size();
        position = 0;
    }

    protected boolean movingPositive = true;
    protected boolean reverseAtEnd;
    protected boolean infinite;

    public void setReverseAtEnd(boolean rae) {
        if (infinite) throw new IllegalStateException("Cannot reverse at end and be infinite");
        reverseAtEnd = rae;
    }

    public void setInfinite(boolean inf) {
        if (reverseAtEnd) throw new IllegalStateException("Cannot be infinite and reverse at end");
        infinite = inf;
    }

    @Override
    public String toString() {
        return "ComplexGradientSeries{" +
                "end_pos=" + end_pos +
                ", position=" + position +
                ", movingPositive=" + movingPositive +
                ", reverseAtEnd=" + reverseAtEnd +
                ", infinite=" + infinite +
                '}';
    }

    public Color getCurrent() {
        int gradN = (int) Math.floor(position);
        GradientSeries grad;
        if (gradN == end_pos) {
            grad = series.get(gradN-1);
        } else {
            grad = series.get(gradN);
        }
        float gpos = position - gradN;
        return grad.getAt(gpos);

    }


    public void step(float stepAmount) {
        position += stepAmount * (movingPositive ? +1 : -1);
        if (position >= end_pos) {
            if (reverseAtEnd && movingPositive) {
                movingPositive = false;
                position = end_pos;
            } else if (infinite) {
                position -= Math.floor(position);
            } else {
                position = end_pos;
            }
        } else if (position <= 0.0f) {
            if (reverseAtEnd && !movingPositive) {
                movingPositive = true;
                position = 0.0f;
            } else if (infinite) {
                position -= Math.floor(position);
            } else {
                position = 0.0f;
            }
        }
    }
}
