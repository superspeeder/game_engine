package org.delusion.engine.renderer.batch;

import jdk.swing.interop.SwingInterOpUtils;
import org.delusion.engine.renderer.Vertex;
import org.delusion.engine.renderer.scene.Quad;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class BatchChunk {

    public static final int MAX_QUADS = 50;
    public static final int MAX_TRIS = MAX_QUADS * 2;
    public static final int MAX_VERTICES = MAX_TRIS * 3;

    public static final int VERTEX_SIZE = 6;

    public List<Quad> objects = new ArrayList<>(MAX_QUADS);
    public List<Float> vertices = new ArrayList<>(VERTEX_SIZE * MAX_VERTICES);

    private boolean dirty = true;
    private int objectsOnLastUpdate = 0;
    private SceneBatch batch;

    public BatchChunk(SceneBatch batch) {
        this.batch = batch;
    }

    public void removeVertex(int id) {
        int startingIndex = id * VERTEX_SIZE;
        for (int i = startingIndex ; i < startingIndex + VERTEX_SIZE; i++) {
            vertices.remove(i);
        }
    }

    public float[] getArray() {
        float[] array = new float[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            array[i] = vertices.get(i);
        }
        return array;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
        batch.markDirty();
    }

    public void clean() {
        // run update

        if (objectsOnLastUpdate == objects.size()) {
            // Only changes, can just edit verticies and be good
            int i = 0;
            for (Quad q : objects) {
                if (q.isDirty()) {
                    Matrix4f model = q.modelMat();
                    Vertex[] qVert = q.getVert();
                    int index = VERTEX_SIZE * i * qVert.length;
                    for (Vertex v : qVert) {
//                        System.out.println("positionVertex = " + positionVertex);
                        Vector4f transformed = model.transform(v.xyzw);
//                        System.out.println("transformed = " + transformed);
//                        System.out.println("v.uv = " + v.uv);
//                        System.out.println("v.xyzw = " + v.xyzw);
                        placeVertex(index, transformed,v.uv);

                        index += VERTEX_SIZE;
                    }
                }

                i++;
            }

        } else {
            // things were added, vertices need to be extended
            int i = 0;
            for (Quad q : objects) {
                if (q.isDirty()) {
                    Matrix4f model = q.modelMat();
                    Vertex[] qVert = q.getVert();
                    int index = VERTEX_SIZE * i * qVert.length;
                    if (index >= vertices.size()) {
//                        System.out.println("Append " + q);
                        for (Vertex v : qVert) {
//                            System.out.println("positionVertex = " + positionVertex);
                            Vector4f transformed = model.transform(new Vector4f(v.xyzw));
//                            System.out.println("transformed = " + transformed);
//                            System.out.println("v.uv = " + v.uv);
//                            System.out.println("v.xyzw = " + v.xyzw);
                            appendVertex(index, transformed, q, v.uv);

                            index += VERTEX_SIZE;
                        }
                    } else {
                        for (Vertex v : qVert) {
                            Vector4f transformed = model.transform(new Vector4f(v.xyzw));
                            placeVertex(index, transformed, v.uv);

                            index += VERTEX_SIZE;
                        }
                    }
                }
                i++;
            }

        }


        dirty = false;
    }

    private void appendVertex(int index, Vector4f transformed, Quad q, Vector2f uv) {
        vertices.add(transformed.x);
        vertices.add(transformed.y);
        vertices.add(transformed.z);
        vertices.add(transformed.w);
        vertices.add(uv.x);
        vertices.add(uv.y);

        // add other data from q

    }

    private void placeVertex(int index, Vector4f transformed, Vector2f uv) {
        vertices.set(index, transformed.x);
        vertices.set(index+1, transformed.y);
        vertices.set(index+2, transformed.z);
        vertices.set(index+3, transformed.w);
        vertices.set(index+4, uv.x);
        vertices.set(index+5, uv.y);
    }

    public boolean isFull() {
        return objects.size() == MAX_QUADS;
    }

    public void batch(Quad quad) {
        objects.add(quad);
        markDirty();
    }
}
