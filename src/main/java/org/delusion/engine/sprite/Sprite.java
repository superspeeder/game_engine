package org.delusion.engine.sprite;

import org.delusion.engine.renderer.Renderer;
import org.delusion.engine.renderer.buffer.VertexBuffer;
import org.delusion.engine.renderer.scene.Quad;
import org.delusion.engine.renderer.texture.Texture2D;
import org.joml.Matrix4f;
import org.joml.Rectanglef;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

// Author andy
// Created 11:29 AM

/**
 * A non-batchable renderable quad meant to be used for entities and player characters. Much more dynamic at the cost of draw calls.
 * Uses its own texture to allow for animated entities without packing multiple together.
 */
public abstract class Sprite {

    private final VertexBuffer vbo;
    protected Vector2f position = new Vector2f();

    public Sprite(Vector2f pos) {
        position.set(pos);

        vbo = new VertexBuffer(GL_STATIC_DRAW,6);

    }

    public abstract Quad getRenderQuad();
    public abstract Texture2D getTexture();
    public abstract Matrix4f getModelMatrix();
    public abstract Vector2f getSize();

    public Rectanglef getBoundingBox() {
        return new Rectanglef(getPosition(),getPosition().add(getSize()));
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    public void setPosition(float x, float y) {
        position.set(x,y);
    }

    public void setPosition(Vector2f vector2f) {
        position.set(vector2f);
    }

    public void buildVBO() {
        float[] data = getRenderQuad().getVertF();
        vbo.bind();
        vbo.setData(data,GL_STATIC_DRAW);
        vbo.unbind();
    }

    public void render() {
        vbo.bind();
        Renderer.drawVBO(GL_TRIANGLES,vbo);
        vbo.unbind();
    }

    public void unbindVBO() {
        vbo.unbind();
    }

    public void bindVBO() {
        vbo.bind();
    }
}
