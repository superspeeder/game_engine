package org.delusion.engine.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private Matrix4f projection, combined = new Matrix4f();
    private Vector2f position;
    private boolean dirty;

    public Camera(int width, int height) {
        projection = new Matrix4f().ortho2D(0,width,0,height);
        position = new Vector2f(0,0);
        markDirty();
    }

    public void setPosition(Vector2f position) {
        position.set(position);
        markDirty();
    }

    public void translate(Vector2f translation) {
        position.set(position.x+translation.x,position.y+translation.y);
        markDirty();
    }

    public void setPosition(float x, float y) {
        position.set(x,y);
        markDirty();
    }

    public void translate(float x, float y) {
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
}
