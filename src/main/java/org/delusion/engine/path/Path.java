package org.delusion.engine.path;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Path {
    private boolean repeats;
    private List<PathPoint> pathPoints = new ArrayList<>();
    private int end = 0;

    private float position;
    private Vector3f location;

    public static class PathPoint {
        private final float x;
        private final float y;
        private final float z;

        public PathPoint(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }

    public boolean getRepeats() {
        return repeats;
    }

    public void addStep(float x, float y, float z) {
        end++;
        pathPoints.add(new PathPoint(x, y, z));
    }

    public void step(float stepamt) {
        position += stepamt;
        if (position > end && repeats) {
            position -= end;
        }
        int pointVal = (int) Math.floor(position);
//        System.out.println("pointVal = " + pointVal + ", " + (pathPoints.size() - 1));
        if (pathPoints.size() - 1 > pointVal) {
            PathPoint point = pathPoints.get(pointVal);
            PathPoint next = pathPoints.get(pointVal + 1);

            float partial = position - pointVal;
//            System.out.println("partial = " + partial);

            location = lerpPoints(point, next, partial);

        } else if (repeats) {
            PathPoint point = pathPoints.get(pointVal);
            PathPoint next = pathPoints.get(0);

            float partial = position - pointVal;

            location = lerpPoints(point, next, partial);
        }
    }

    public Vector3f getLocation() {
        return location;
    }

    private Vector3f lerpPoints(PathPoint point, PathPoint next, float partial) {
        float x1 = point.x;
        float y1 = point.y;
        float z1 = point.z;
        float x2 = next.x;
        float y2 = next.y;
        float z2 = next.z;

        return new Vector3f(x1 + (x2 - x1) * partial,y1 + (y2 - y1) * partial,z1 + (z2 - z1) * partial);
    }
}
