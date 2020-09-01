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

    public Vector4f getTileUV(int id) {
        int row = id / tiles_per_row;
        int col = id % tiles_per_row;
        if (id > tiles_per_row * rows) {
            throw new IllegalArgumentException("ID is greater than max ids");
        }

        float v = row*tile_v;
        float u = col*tile_u;
        return new Vector4f(u, v, u+tile_u, v+tile_v);
    }
}
