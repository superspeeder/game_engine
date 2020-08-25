package org.delusion.engine.window;

import static org.lwjgl.glfw.GLFW.*;

public class WindowUtils {

    public static void pollEvents() {
        glfwPollEvents();
    }

    public static void terminate() {
        glfwTerminate();
    }

    public static void init() {
        if (!glfwInit())
            throw new RuntimeException("GLFW couldn't be initialized");
    }
}
