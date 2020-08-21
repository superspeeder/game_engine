package org.delusion.engine.renderer.buffer;

import static org.lwjgl.opengl.GL46.*;

public class VertexArray {

    public static final int FLOAT = GL_FLOAT;
    private final int vao;

    private boolean bound = false;

    public VertexArray() {
        vao = glGenVertexArrays();
    }

    public void pointer(int index,int size,int type,boolean normalized,int stride,int pointer) {
        boolean wasbound = bound;
        bind();

        glVertexAttribPointer(index,size,type,normalized,stride,pointer);

        if (!wasbound) {
            unbind();
        }
    }

    public void bind() {
        if (!bound) {
            bound = true;
            glBindVertexArray(vao);
        }
    }

    public void unbind() {
        if (bound) {
            bound = false;
            glBindVertexArray(0);
        }
    }

    public void enableAttr(int index) {
        boolean wasbound = bound;
        bind();
        glEnableVertexAttribArray(index);
        if (!wasbound) {
            unbind();
        }
    }
}
