package org.delusion.engine.renderer;


import org.delusion.engine.renderer.buffer.VertexArray;
import org.delusion.engine.renderer.buffer.VertexBuffer;
import org.delusion.engine.renderer.color.Color;
import org.delusion.engine.renderer.scene.Quad;

import java.nio.IntBuffer;
import java.util.Set;

import static org.lwjgl.opengl.GL46.*;

public class Renderer {
    private static boolean trackingBGcolor;
    private static Color bgColor;
    private static boolean wireframe = false;

    public static void setBackgroundColor(Color color, boolean listenAndStore) {
        if (bgColor != null && trackingBGcolor) {
            bgColor.clearTracker("Renderer._updateClearColor");
        }
        bgColor = null;
        trackingBGcolor = listenAndStore;
        if (!listenAndStore) {
            color = color.cpy();
        } else {
            color.addTracker("Renderer._updateClearColor", Renderer::_updateClearColor);
        }

        bgColor = color;

        _updateClearColor();

    }

    private static void _updateClearColor() {
        glClearColor(bgColor.R(),bgColor.G(),bgColor.B(),bgColor.A());
    }

    public static void clearBackground() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void drawArrays_lowlevel(int mode, int first, int count) {
        glDrawArrays(mode, first, count);
    }

    public static void drawArraysTris_lowlevel(int first, int count) {
        drawArrays_lowlevel(GL_TRIANGLES, first, count);
    }

    public static void drawVBO(int mode, VertexBuffer vbo) {
        drawArraysTris_lowlevel(0,vbo.verticies());
    }

    public static void drawVAOArray(int mode, VertexArray vao, int n) {

    }

    public static void wireframe(boolean b) {
        wireframe = b;
    }

    public static boolean isWireframe() {
        return wireframe;

    }

    public static class SetupOut {
        public VertexBuffer vbo;
        public VertexArray vao;

        SetupOut() {
        }
    }

    public static void enableAlphaBlending() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static SetupOut setupFromQuad(Quad texturedQuad) {
        SetupOut out = new SetupOut();
        out.vao = new VertexArray();
        out.vbo = new VertexBuffer(texturedQuad.getVertF(),6);
        out.vao.bind();
        out.vbo.bind();
        out.vao.pointer(0,4,GL_FLOAT,false,6 * Float.BYTES, 0);
        out.vao.pointer(1,2,GL_FLOAT,false,6 * Float.BYTES, 4 * Float.BYTES);
        out.vao.enableAttr(0);
        out.vao.enableAttr(1);
        return out;
    }

    public Color getBgColor() {
        if (trackingBGcolor) {
            return bgColor;
        } else {
            return bgColor.cpy();
        }
    }
}
