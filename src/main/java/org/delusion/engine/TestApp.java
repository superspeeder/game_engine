package org.delusion.engine;

import org.delusion.engine.camera.Camera;
import org.delusion.engine.color.ComplexGradientSeries;
import org.delusion.engine.resources.Tileset;
import org.delusion.engine.scene.Scene;
import org.delusion.game.player.Player;
import org.delusion.engine.renderer.batch.SceneBatch;
import org.delusion.engine.renderer.buffer.VertexArray;
import org.delusion.engine.renderer.color.Color;
import org.delusion.engine.renderer.Renderer;
import org.delusion.engine.renderer.scene.Quad;
import org.delusion.engine.renderer.shader.ShaderProgram;
import org.delusion.engine.utils.GlUtils;
import org.delusion.engine.window.Window;
import org.delusion.engine.window.WindowSettings;
import org.delusion.engine.window.WindowUtils;
import org.delusion.game.CameraMover;
import org.delusion.game.event.KeyCallback;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class TestApp {

    public static Camera camera;
    public static WindowSettings settings;
    public static Window window;
    public static Color BackgroundColor;
    public static SceneBatch staticBatch;
    public static SceneBatch dynamicBatch;
    public static ComplexGradientSeries gradientSeries;
    public static ShaderProgram program;
    public static VertexArray spriteVAO;

    public static long start;
    public static Player playerCharacter;
    public static Scene scene;
    private static Tileset tileset;

    public static void main(String[] args) {

        {

//            try {
//                System.in.read();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            startLoadTimer();

            WindowUtils.init();

            // Window Settings
            settings = new WindowSettings();
            settings.debugMode(true);
            settings.makeFullscreenSize();
            settings.samples = 4;
            settings.profile(WindowSettings.GLProfile.CORE);

            // Window & Context Creation
            window = new Window(settings);
            window.makeContextCurrent();
            window.swapInterval(1);

            // Enable Debug Mode
//            GlUtils.enableDebug();

            // Background Colors
            BackgroundColor = new Color(1, 1, 1, 1);

            Renderer.enableAlphaBlending();
            Renderer.enableMultisample();
            Renderer.setBackgroundColor(BackgroundColor, true);

            // Shader Program
            program = new ShaderProgram(
                    "shaders/vertex0.glsl",
                    "shaders/fragment0.glsl");

            // Scene
            loadSceneResources();

            staticBatch = new SceneBatch(tileset);
            dynamicBatch = new SceneBatch(tileset);

            buildScene();
            loadEntities();

//            camera = Camera.pixels_unit_scale(window,0.667f);
            camera = Camera.pixels_unit_scale(window,1.f);

            playerCharacter = new Player(new Vector4f(0,0,1,1), camera.getCenter());
            playerCharacter.buildVBO();

            spriteVAO = new VertexArray();
            spriteVAO.bind();
            playerCharacter.bindVBO();
            spriteVAO.pointer(0,4,GL_FLOAT,false,6 * Float.BYTES,0);
            spriteVAO.enableAttr(0);
            spriteVAO.pointer(1,2,GL_FLOAT,false,6 * Float.BYTES,4 * Float.BYTES);
            spriteVAO.enableAttr(1);
            spriteVAO.unbind();
            playerCharacter.unbindVBO();


            // Camera
            glfwPollEvents();

            System.out.println("Loading Took: " + secondsSinceStart() + " seconds");

        }

//        Renderer.wireframe(true);



        scene.printCollisionTiles();

        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

         // Event Handlers
        window.setKeyCallback(new KeyCallback());

        // Gameloop
        while (!window.shouldClose()) {

            // Get Cursor Position
            window.getCursorPos(x,y);

            // Camera
            if (CameraMover.dx != 0 || CameraMover.dy != 0) {

                playerCharacter.move(new Vector2f((float)(CameraMover.dx * window.getDelta()), 0).mul(-1));
                camera.markDirty();
            }

            playerCharacter.update(scene, camera);




            // Clear Background

            Renderer.clearBackground();



            // Draw
            drawScene();
            drawPlayer();

            // Update Window
            window.update();
        }

        WindowUtils.terminate();
        System.out.println("Game ran for " + secondsSinceStart() + " seconds.");

    }

    private static void drawPlayer() {
        glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

        spriteVAO.bind();
        program.use();
        playerCharacter.bindTex(0);
        program.uniformMat4("mvp", camera.getProjection().mul(playerCharacter.getModelMatrix()));
        program.uniform1i("outline", 0);
        program.uniform1i("texture_", 0);
        program.uniform1i("flipX",playerCharacter.direction);
        playerCharacter.render();
        program.uniform1i("flipX",1);
        spriteVAO.unbind();
    }

    private static void drawScene() {
        program.use();

        staticBatch.bindTex();
        program.uniformMat4("mvp", camera.getCombined());
        program.uniform1i("texture_", 0);
        program.uniform1i("outline", 0);

        // Draw
        glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
        SceneBatch.updateAndDraw(staticBatch,dynamicBatch);
        if (Renderer.isWireframe()) {
            program.uniform1i("outline", 1);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            staticBatch.draw();
            dynamicBatch.draw();
        }

    }

    private static void loadEntities() {

    }

    private static void startLoadTimer() {
        start = System.nanoTime();
    }


    private static long nanosSinceStart() {
        return (System.nanoTime() - start);
    }

    private static double secondsSinceStart() {
        return nanosSinceStart() / (float)1e+9;
    }

    private static void loadSceneResources() {
        try {
            Textures.setup();
            tileset = new Tileset(32, Textures.MAIN_TS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void buildScene() {
        scene = new Scene(32,32);

        // Main Floor
//        staticBatch.batchTile(new Vector2f(0,4), 0);
//        staticBatch.batchTile(new Vector2f(0, -4), 64);
//        staticBatch.batchTile(new Vector2f(60, 4), 2);
//        staticBatch.batchTile(new Vector2f(60, -4), 66);
//
//        staticBatch.tiledTileBatch(new Vector2f(1,4),new Vector2f(59,1), 1);
//        staticBatch.tiledTileBatch(new Vector2f(1,-4),new Vector2f(59,1), 65);
//
//        staticBatch.tiledTileBatch(new Vector2f(0,-3), new Vector2f(1,7), 32);
//        staticBatch.tiledTileBatch(new Vector2f(60,-3), new Vector2f(1,7), 34);
//
//
//        staticBatch.tiledTileBatch(new Vector2f(1,-3), new Vector2f(59,7), 33);
//
//
//        scene.addCollidersRect(new Vector2i(0,-4), new Vector2i(60,4));

        scene.setLayer(0,staticBatch);
        scene.loadInto(0,"level0/level0_static.csv",0,0);
        scene.addCollisionMaskFromRenderContent("level0/level0_static.csv",0,0);
        staticBatch.update();
        dynamicBatch.update();
    } // Level creation
}
