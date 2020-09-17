package org.delusion.engine;

import org.delusion.engine.renderer.texture.Texture2D;
import org.lwjgl.stb.STBImage;

import java.io.IOException;

public class Textures {

    public static Texture2D PLAYER;
    public static Texture2D MAIN_TS;

    public static void setup() throws IOException {
        STBImage.stbi_set_flip_vertically_on_load(true);

        MAIN_TS = new Texture2D("textures/tilesets/platformer0.png", new Texture2D.TextureSettings());
        PLAYER = new Texture2D("textures/entity/character/player.png", new Texture2D.TextureSettings());
    }
}
