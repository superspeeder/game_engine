package org.delusion.engine.camera;

import org.delusion.engine.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private Matrix4f projection, combined = new Matrix4f();
    private Vector2f position;
    private boolean dirty;
    private int width;
    private int height;
    private float scale;

    public Camera(int width, int height, float scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        projection = new Matrix4f().ortho2D(0,width,0,height);
        position = new Vector2f(0,0);
        markDirty();
    }

    public static Camera pixels_unit(Window window) {
        return new Camera(window.getWidth(), window.getHeight(), 1.0f);
    }

    public static Camera pixels_unit_scale(Window window, float scale) {
        return new Camera((int)(window.getWidth() * scale), (int)(window.getHeight() * scale), scale);
    }

    public Vector2f unproject(Vector2f vector2f) {
        Vector2f out = new Vector2f(vector2f).mul(scale).sub(position);
//        System.out.println(vector2f.x + ", " + vector2f.y + " => " + out.x + ", " + out.y + "   ::   " + position.x + ", " + position.y + "  *  " + scale);
        return out;
    }

    public void setPosition(Vector2f position) {
        this.position.set(position);
        markDirty();
    }

    public void translate(Vector2f translation) {
        position.set(position.x+translation.x,position.y+translation.y);
//        System.out.println(position);
        markDirty();
    }

    public void setPosition(float x, float y) {
        position.set(x,y);
//        System.out.println(position);
        markDirty();
    }

    public void translate(double x, double y) {
        position.set(position.x+x,position.y+y);
        markDirty();
    }

    public void update() {
        if (isDirty()) {
            projection.mul(
                    new Matrix4f()
                        .translate(position.x,position.y,0),
                    combined
            );


            dirty = false;
        }
    }

    public Matrix4f getCombined() {
        return new Matrix4f(combined);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void translate(int dx, int dy, double delta) {
        translate(dx * delta,dy * delta);
    }

    public Matrix4f getProjection() {
        return new Matrix4f(projection);
    }

    public Vector2f getCenter() {
        return new Vector2f(width/2f,height/2f);
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }
}
