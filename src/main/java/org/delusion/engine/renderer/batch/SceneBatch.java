package org.delusion.engine.renderer.batch;

import org.delusion.engine.renderer.Renderer;
import org.delusion.engine.renderer.buffer.VertexArray;
import org.delusion.engine.renderer.buffer.VertexBuffer;
import org.delusion.engine.renderer.scene.Quad;
import org.delusion.engine.renderer.texture.Texture2D;
import org.delusion.engine.resources.Tileset;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

public class SceneBatch {

    private final Modes mode;
    private Tileset tileset;
    private Texture2D texture;

    private List<BatchChunk> chunks = new ArrayList<>();

    private VertexBuffer vbo;

    private boolean dirty = true;
    private int last_verticies_len = 0;
    private VertexArray vao;

    public SceneBatch(Texture2D texture) {
        this.texture = texture;
        vbo = new VertexBuffer(GL_DYNAMIC_DRAW, 4);
        vao = new VertexArray();
        vao.bind();
        vbo.bind();
        vao.pointer(0,4,GL_FLOAT,false,6 * Float.BYTES,0);
        vao.enableAttr(0);
        vao.pointer(1,2,GL_FLOAT,false,6 * Float.BYTES,4 * Float.BYTES);
        vao.enableAttr(1);
        vao.unbind();
        vbo.unbind();

        mode = Modes.TEXTURE;
    }

    public SceneBatch(Tileset tileset) {
        mode = Modes.TILESET;
        this.tileset = tileset;
        this.texture = tileset.getTexture();

        vbo = new VertexBuffer(GL_DYNAMIC_DRAW, 4);
        vao = new VertexArray();
        vao.bind();
        vbo.bind();
        vao.pointer(0,4,GL_FLOAT,false,6 * Float.BYTES,0);
        vao.enableAttr(0);
        vao.pointer(1,2,GL_FLOAT,false,6 * Float.BYTES,4 * Float.BYTES);
        vao.enableAttr(1);
        vao.unbind();
        vbo.unbind();
    }

    public static void updateAndDraw(SceneBatch... batches) {
        for (SceneBatch sb : batches) {
            sb.update();
        }
        for (SceneBatch sb : batches) {
            sb.draw();
        }
    }

    public void update() {
        if (dirty) {

            int i = 0;
            int totalSize = 0;
            float[] verticies = new float[0];

            for (BatchChunk chunk : new ArrayList<BatchChunk>(chunks)) {
                if (chunk.isDirty()) {
                    chunk.clean();
                }

                if (chunk.vertices.isEmpty()) {
                    chunks.remove(chunk);
                    continue;
                }
                totalSize += chunk.vertices.size();

                verticies = concatV(verticies, chunk.vertices);
                i++;
            }

            if (verticies.length != last_verticies_len) {
                vbo.bind();
                vbo.setData(verticies,GL_DYNAMIC_DRAW);
                vbo.unbind();
            } else {
                vbo.bind();
                vbo.updateAllData(verticies);
                vbo.unbind();
            }


            dirty = false;
            last_verticies_len = verticies.length;
        }
    }

    private float[] concatV(float[] verticies, List<Float> verticies2) {
        int i = 0;

        float[] out = new float[verticies.length + verticies2.size()];
        System.arraycopy(verticies,0,out,0,verticies.length);

        for (Float f : verticies2) {
            out[verticies.length + i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return out;
    }

    public void batchQuad(Quad quad) {
        dirty = true;

        for (BatchChunk chunk : chunks) {
            if (!chunk.isFull()) {
                quad.setChunk(chunk);
                chunk.batch(quad);
                return;
            }
        }
        BatchChunk chunk = new BatchChunk(this);
        quad.setChunk(chunk);
        chunk.batch(quad);
        chunks.add(chunk);
    }

    public void batchTile(Vector2f pos, int id) {
        Quad quad = new Quad(new Vector3f(pos.x * tileset.getTS(),pos.y*tileset.getTS(),1.0f),new Vector3f(tileset.getTS(),tileset.getTS(),1.0f),tileset.getTileUV(id));
        batchQuad(quad);
    }

    public void bindTex() {
        texture.bind(0);
    }

    public void draw() {
        vao.bind();
        vbo.bind();
        Renderer.drawVBO(GL_TRIANGLES, vbo);
        vao.unbind();
        vbo.unbind();
    }

    public void markDirty() {
        dirty = true;
    }

    public void tiledQuadBatch(Vector2f pos, Vector2f tiles, Vector2f tilesize, Vector4f uvs) {

        for (int x = 0; x < tiles.x; x++) {
            for (int y = 0; y < tiles.y; y++) {
                batchQuad(new Quad(
                        new Vector3f(x * tilesize.x + pos.x * tilesize.x, y * tilesize.y + pos.y * tilesize.y, 1),
                        new Vector3f(tilesize.x, tilesize.y, 1),
                        uvs
                ));
            }
        }
    }

    public void tiledTileBatch(Vector2f pos, Vector2f tiles, int id) {
        tiledQuadBatch(pos,tiles,new Vector2f(tileset.getTS(),tileset.getTS()), tileset.getTileUV(id));

    }
}
