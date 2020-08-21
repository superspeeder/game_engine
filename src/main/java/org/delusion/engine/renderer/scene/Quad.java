package org.delusion.engine.renderer.scene;

import org.delusion.engine.renderer.Vertex;
import org.delusion.engine.renderer.batch.BatchChunk;
import org.joml.*;

public class Quad {

    public static final Vector4f[] POS_VERT = {
            new Vector4f(0, 0, 0, 1),
            new Vector4f(1, 0, 0, 1),
            new Vector4f(1, 1, 0, 1),

            new Vector4f(0, 0, 0, 1),
            new Vector4f(1, 1, 0, 1),
            new Vector4f(0, 1, 0, 1)
    };
    public Vertex[] vert;
    private BatchChunk chunk;
    private boolean dirty = true;
    private Quaternionf rotation;
    private Vector3f scale;
    private Vector3f translation;
    private boolean extraDataDirty = false;

    private float u1,u2,v1,v2;

    public Quad(Vector3f translation, Vector3f scale, Vector4f uvs) {
        this.scale = scale;
        this.translation = translation;
        this.rotation = new Quaternionf();

        u1 = uvs.x;
        u2 = uvs.z;
        v1 = uvs.y;
        v2 = uvs.w;
    }

    public static Vector4f uvs(int width, int height, int x, int y, int w, int h) {
        float nx = (float)x / (float)(width);
        float ny = (float)y / (float)(height);
        float nx2 = nx + (float)w / (float)(width);
        float ny2 = ny + (float)h / (float)(height);

        return new Vector4f(nx,ny,nx2,ny2);
    }

    public Quad(Vector3f translation, Vector3f scale, Quaternionf rotation) {
        this.scale = scale;
        this.translation = translation;
        this.rotation = rotation;
    }

    public Quad(int x, int y, int w, int h, Vector4f uvs) {
        this(new Vector3f(x,y,1),new Vector3f(w,h,1), uvs);
    }

    public void setChunk(BatchChunk chunk) {
        this.chunk = chunk;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
        chunk.markDirty();
    }

    public void clean() {
        dirty = false;
    }

    public Matrix4f modelMat() {
        return new Matrix4f().rotate(rotation).translate(translation).scale(scale);
    }

    public boolean isExtraDataDirty() {
        return extraDataDirty;
    }

    public void cleanExtraData() {
        extraDataDirty = false;
    }

    public void markExtraDataDirty() {
        extraDataDirty = true;
    }

    @Override
    public String toString() {
        return "Quad{" +
                "rotation=" + rotation +
                ", scale=" + scale +
                ", translation=" + translation +
                '}';
    }

    public void move(int x, int y, int z) {
        markDirty();
        translation.set(translation.x + x,translation.y + y, translation.z + z);
    }

    public Vector2f[] getUVs() {
        return new Vector2f[0];
    }

    public Vertex[] getVert() {
        return new Vertex[] {
                new Vertex(new Vector4f(0, 0, 0, 1), new Vector2f(u1,v1)),
                new Vertex(new Vector4f(1, 0, 0, 1), new Vector2f(u2,v1)),
                new Vertex(new Vector4f(1, 1, 0, 1), new Vector2f(u2,v2)),
                new Vertex(new Vector4f(0, 0, 0, 1), new Vector2f(u1,v1)),
                new Vertex(new Vector4f(1, 1, 0, 1), new Vector2f(u2,v2)),
                new Vertex(new Vector4f(0, 1, 0, 1), new Vector2f(u1,v2))
        };
    }

    public float[] getVertF() {
        Vertex[] v = getVert();
        float[] f = new float[v.length * 6];
        int i = 0;
        for (Vertex vertex : v) {
            f[i++] = vertex.xyzw.x;
            f[i++] = vertex.xyzw.y;
            f[i++] = vertex.xyzw.z;
            f[i++] = vertex.xyzw.w;

            f[i++] = vertex.uv.x;
            f[i++] = vertex.uv.y;
        }
        return f;
    }
}
