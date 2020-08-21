package org.delusion.engine.renderer.texture;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL46.*;

public class Texture2D {
    private TextureSettings settings;
    private ByteBuffer data;
    private int width, height, channels;
    private int textureHandle;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Vector4f uvs(int x1, int y1_, int x2, int y2_) {
        float y1 = height - y1_;
        float y2 = height - y2_;

        return new Vector4f(
                (float)x1 / (float)width, y2 / (float)height,
                (float)x2 / (float)width, y1 / (float)height);
    }

    public static class TextureSettings {
        public int wrapMode = GL_REPEAT;
        public int filter_mode_min = GL_NEAREST;
        public int filter_mode_mag = GL_NEAREST;

        public float[] borderColor = null;


    }

    public Texture2D(String s, TextureSettings settings) throws IOException {
        this.settings = settings;
        byte[] data = Texture2D.class.getClassLoader().getResourceAsStream(s).readAllBytes();

        loadFrom(data);
    }

    private void loadFrom(byte[] data) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
        buffer.put(data);
        buffer.rewind();

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels_in_file = BufferUtils.createIntBuffer(1);

        this.data = STBImage.stbi_load_from_memory(buffer,width,height,channels_in_file, 4);

        this.width = width.get();
        this.height = height.get();
        this.channels = 4;
        textureHandle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureHandle);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, settings.wrapMode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, settings.wrapMode);

        if (settings.borderColor != null) {
            glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, settings.borderColor);
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, settings.filter_mode_min);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, settings.filter_mode_mag);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, this.data);
        System.out.println("this.data = " + this.data);
        
        STBImage.stbi_image_free(this.data);
        glBindTexture(GL_TEXTURE_2D,0);
    }

    public void bind(int unit) {
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(GL_TEXTURE_2D, textureHandle);
    }
}
