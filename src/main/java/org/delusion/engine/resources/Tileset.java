package org.delusion.engine.resources;

import org.delusion.engine.renderer.texture.Texture2D;
import org.joml.Vector4f;

import java.io.IOException;

// Author andy
// Created 8:42 PM
public class Tileset {

    private Texture2D texture2D;
    private int tile_size;

    private int tiles_per_row;
    private int rows;
    private float tile_u,tile_v;

    public Tileset(int tile_size, String path) throws IOException {
        this.tile_size = tile_size;
        this.texture2D = new Texture2D(path, new Texture2D.TextureSettings());
        tiles_per_row = texture2D.getWidth() / tile_size;
        rows = (int)Math.ceil(texture2D.getHeight() / (float)tile_size);

        tile_u = texture2D.getWidth()/(float)tile_size;
        tile_v = texture2D.getHeight()/(float)tile_size;
    }

    public Tileset(int tile_size, Texture2D mainTs) {
        this.tile_size = tile_size;
        this.texture2D = mainTs;

        tiles_per_row = texture2D.getWidth() / tile_size;
        rows = (int)Math.ceil(texture2D.getHeight() / (float)tile_size);

        tile_u = texture2D.getWidth()/(float)tile_size;
        tile_v = texture2D.getHeight()/(float)tile_size;
    }

    public Vector4f getTileUV(int id) {
        int row = id / tiles_per_row;
        int col = id % tiles_per_row;
        if (id > tiles_per_row * rows) {
            throw new IllegalArgumentException("ID is greater than max ids");
        }

        return texture2D.uvs(col*tile_size,row*tile_size,(col+1)*tile_size,(row+1)*tile_size);
    }

    public Texture2D getTexture() {
        return texture2D;
    }

    public int getTS() {
        return tile_size;
    }
}
