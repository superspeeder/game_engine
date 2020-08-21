package org.delusion.engine;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import org.delusion.engine.camera.Camera;
import org.delusion.engine.color.ComplexGradientSeries;
import org.delusion.engine.renderer.batch.SceneBatch;
import org.delusion.engine.renderer.color.Color;
import org.delusion.engine.renderer.Renderer;
import org.delusion.engine.renderer.color.GradientSeries;
import org.delusion.engine.renderer.scene.Quad;
import org.delusion.engine.renderer.shader.ShaderProgram;
import org.delusion.engine.utils.GlUtils;
import org.delusion.engine.window.Window;
import org.delusion.engine.window.WindowSettings;
import org.delusion.engine.window.WindowUtils;
import org.delusion.game.CameraMover;
import org.delusion.game.event.KeyCallback;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.nio.DoubleBuffer;

import static org.lwjgl.opengl.GL46.*;

import static org.lwjgl.glfw.GLFW.*;

public class TestApp {

    public static Camera camera;
    public static WindowSettings settings;
    public static Window window;
    public static Color BackgroundColor;
    public static SceneBatch staticBatch;
    public static SceneBatch dynamicBatch;
    public static ComplexGradientSeries gradientSeries;
    public static ShaderProgram program;

    public static void main(String[] args) {

        {
            long start = System.nanoTime();

            if (!glfwInit())
                throw new RuntimeException("GLFW couldn't be initialized");

            // Window Settings
            settings = new WindowSettings();

            settings.opengl_debug_context = true;

            long monitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidMode = glfwGetVideoMode(monitor);

            settings.window_width = vidMode.width();
            settings.window_height = vidMode.height();

            settings.profile(WindowSettings.GLProfile.ANY);

            // Window & Context Creation
            window = new Window(settings);
            window.makeContextCurrent();
            GL.createCapabilities();

            // Enable Debug Mode
            GlUtils.enableDebug();
            glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DEBUG_SEVERITY_NOTIFICATION,
                    new int[0], false);

            // Background Colors

            Renderer.enableAlphaBlending();
            BackgroundColor = new Color(1, 1, 1, 1);
            Renderer.setBackgroundColor(BackgroundColor, true);

            // Shader Program
            program = new ShaderProgram(
                    "src/main/resources/shaders/vertex0.glsl",
                    "src/main/resources/shaders/fragment0.glsl");

            // Scene

            try {
                Textures.setup();
            } catch (IOException e) {
                e.printStackTrace();
            }

            staticBatch = new SceneBatch(Textures.MAIN_TS);
            dynamicBatch = new SceneBatch(Textures.MAIN_TS);

            {
                staticBatch.batchQuad(new Quad(0, 128, 32, 32, Textures.MAIN_TS.uvs(0, 0, 32, 32)));
                staticBatch.batchQuad(new Quad(0, 0, 32, 32, Textures.MAIN_TS.uvs(0, 64, 32, 96)));
                staticBatch.batchQuad(new Quad(800, 128, 32, 32, Textures.MAIN_TS.uvs(64, 0, 96, 32)));
                staticBatch.batchQuad(new Quad(800, 0, 32, 32, Textures.MAIN_TS.uvs(64, 64, 96, 96)));

                staticBatch.tiledQuadBatch(new Vector2f(32, 128), new Vector2f(24, 1), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(32, 0, 64, 32));
                staticBatch.tiledQuadBatch(new Vector2f(32, 0), new Vector2f(24, 1), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(32, 64, 64, 96));

                staticBatch.tiledQuadBatch(new Vector2f(0, 32), new Vector2f(1, 3), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(0, 32, 32, 64));
                staticBatch.tiledQuadBatch(new Vector2f(800, 32), new Vector2f(1, 3), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(64, 32, 96, 64));

                staticBatch.tiledQuadBatch(new Vector2f(32, 32), new Vector2f(24, 3), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(32, 32, 64, 64));

                staticBatch.tiledQuadBatch(new Vector2f(0, -96), new Vector2f(1, 3), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(0, 96, 32, 128));
                staticBatch.tiledQuadBatch(new Vector2f(800, -96), new Vector2f(1, 3), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(64, 96, 96, 128));

                staticBatch.batchQuad(new Quad(0, -128, 32, 32, Textures.MAIN_TS.uvs(0, 128, 32, 160)));
                staticBatch.batchQuad(new Quad(800, -128, 32, 32, Textures.MAIN_TS.uvs(64, 128, 96, 160)));

                staticBatch.tiledQuadBatch(new Vector2f(32, -96), new Vector2f(24, 3), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(32, 96, 64, 128));

                staticBatch.tiledQuadBatch(new Vector2f(32, -128), new Vector2f(24, 1), new Vector2f(32, 32),
                        Textures.MAIN_TS.uvs(32, 128, 64, 160));
            } // Level creation


            staticBatch.update();
            dynamicBatch.update();

            // Camera
            camera = new Camera(window.getWidth(), window.getHeight());

            long end = System.nanoTime();
            System.out.println("Loading Took: " + (end - start) / 1e+9 + " seconds");
        } // Loading everything

        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        // Event Handlers
        window.setKeyCallback(new KeyCallback());

        // Gameloop

        while (!window.shouldClose()) {

            // Clear Background
            Renderer.clearBackground();

            // Camera
            if (CameraMover.dx != 0 || CameraMover.dy != 0)
                camera.translate(CameraMover.dx,CameraMover.dy);
            camera.update();

            // Shader Uniforms
            program.use();

            staticBatch.bindTex();
            program.uniformMat4("mvp", camera.getCombined());
            program.uniform1i("texture_", 0);

            window.getCursorPos(x,y);

            // Draw
            staticBatch.update();
            dynamicBatch.update();

            staticBatch.draw();
            dynamicBatch.draw();


            // Update Events
            WindowUtils.pollEvents();

            // Swap Draw Buffer
            window.swapBuffers();
        }

        glfwTerminate();

    }
}
