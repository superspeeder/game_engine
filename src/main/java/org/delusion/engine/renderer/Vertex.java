package org.delusion.engine.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Vertex {
    public Vector4f xyzw;
    public Vector2f uv;

    public Vertex(Vector4f xyzw, Vector2f uv) {
        this.xyzw = xyzw;
        this.uv = uv;
    }
}
