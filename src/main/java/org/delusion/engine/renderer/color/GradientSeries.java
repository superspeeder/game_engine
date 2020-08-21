package org.delusion.engine.renderer.color;

public abstract class GradientSeries {

    protected boolean movingPositive = true;
    protected boolean reverseAtEnd;
    protected boolean infinite;
    protected float position;

    public void setReverseAtEnd(boolean rae) {
        if (infinite) throw new IllegalStateException("Cannot reverse at end and be infinite");
        reverseAtEnd = rae;
    }

    public void setInfinite(boolean inf) {
        if (reverseAtEnd) throw new IllegalStateException("Cannot be infinite and reverse at end");
        infinite = inf;
    }

    public abstract Color getCurrent();
    public void step(float stepAmount) {
        position += stepAmount * (movingPositive ? +1 : -1);
        if (position >= 1.0f) {
            if (reverseAtEnd && movingPositive) {
                movingPositive = false;
                position = 1.0f;
            } else if (infinite) {
                position -= Math.floor(position);
            } else {
                position = 1.0f;
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

    @Override
    public String toString() {
        return "GradientSeries{" +
                "movingPositive=" + movingPositive +
                ", reverseAtEnd=" + reverseAtEnd +
                ", infinite=" + infinite +
                ", position=" + position +
                '}';
    }

    public abstract Color getAt(float gpos);

    public static class RGBA8888Base extends GradientSeries {
        @Override
        public String toString() {
            return "GradientSeries.RGBA8888Base{" +
                    "movingPositive=" + movingPositive +
                    ", reverseAtEnd=" + reverseAtEnd +
                    ", infinite=" + infinite +
                    ", position=" + position +
                    ", start=" + start +
                    ", end=" + end +
                    ", diff=" + diff +
                    '}';
        }

        @Override
        public Color getAt(float gpos) {
            return new Color(start + (long)(diff * gpos));
        }

        private final long start;
        private final long end;
        private final long diff;

        public RGBA8888Base(long start, long end) {
            this.start = start;
            this.end = end;
            diff = end - start;
            position = 0;
        }

        @Override
        public Color getCurrent() {
            return new Color(start + (long)(diff * position));
        }

        public Color getCurrent(long i) {
            return new Color(start + (long)(diff * position) + i);
        }
    }

    public static class LerpColorBase extends GradientSeries {


        private final Color start;
        private final Color end;

        public LerpColorBase(Color start, Color end) {

            this.start = start;
            this.end = end;
            position = 0;
        }

        @Override
        public String toString() {
            return "LerpColorBase{" +
                    "movingPositive=" + movingPositive +
                    ", reverseAtEnd=" + reverseAtEnd +
                    ", infinite=" + infinite +
                    ", position=" + position +
                    ",\n    start=" + start +
                    ",\n    end=" + end +
                    '}';
        }

        @Override
        public Color getAt(float gpos) {
            return lerp(start,end,gpos);
        }

        @Override
        public Color getCurrent() {
            return lerp(start,end,position);
        }

        private static Color lerp(Color start, Color end, float position) {
            float r = start.R() + (end.R() - start.R()) * position;
            float g = start.G() + (end.G() - start.G()) * position;
            float b = start.B() + (end.B() - start.B()) * position;
            float a = start.A() + (end.A() - start.A()) * position;
            return new Color(r,g,b,a);
        }
    }

    public static class LerpHSVColorBase extends GradientSeries {


        private final HSVColor start;
        private final HSVColor end;

        public LerpHSVColorBase(HSVColor start, HSVColor end) {

            this.start = start;
            this.end = end;
            position = 0;
        }

        @Override
        public String toString() {
            return "LerpHSVColorBase{" +
                    "movingPositive=" + movingPositive +
                    ", reverseAtEnd=" + reverseAtEnd +
                    ", infinite=" + infinite +
                    ", position=" + position +
                    ",\n    start=" + start +
                    ",\n    end=" + end +
                    '}';
        }

        @Override
        public Color getCurrent() {
            return lerp(start,end,position).toColor();
        }

        @Override
        public Color getAt(float gpos) {
            return lerp(start,end,gpos).toColor();
        }

        private static HSVColor lerp(HSVColor start, HSVColor end, float position) {
            float h = start.H() + (end.H() - start.H()) * position;
            float s = start.S() + (end.S() - start.S()) * position;
            float v = start.V() + (end.V() - start.V()) * position;
            return new HSVColor(h,s,v);
        }
    }
}
