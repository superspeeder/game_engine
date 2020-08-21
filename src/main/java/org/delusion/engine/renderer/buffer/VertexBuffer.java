package org.delusion.engine.renderer.buffer;

import static org.lwjgl.opengl.GL46.*;


public class VertexBuffer {
    private int vbo;
    private int vertexSize, vertexCount;

    public VertexBuffer(float[] data, int usage, int vertexSize) {
        vbo = glGenBuffers();
        bind();
        setData(data, usage);
        unbind();

        this.vertexSize = vertexSize;
        this.vertexCount = data.length / vertexSize;
    }

    public VertexBuffer(float[] data, int vertexSize) {
        this(data, GL_STATIC_DRAW, vertexSize);
    }

    public VertexBuffer(int mode, int vertexSize) {
        // no data or size;
        vbo = glGenBuffers();
        bind();
        unbind();

        this.vertexSize = vertexSize;
        this.vertexCount = 0;
    }

    public void setData(float[] data, int usage) {
        this.vertexCount = data.length / vertexSize;

        glBufferData(GL_ARRAY_BUFFER,data,usage);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
    }

    public int verticies() {
        return vertexCount;
    }

    public int size() {
        return vertexCount * vertexSize;
    }

    public void updateAllData(float[] data) {
        bind();
        glBufferSubData(GL_ARRAY_BUFFER,0,data);
        unbind();
    }
}
