package org.delusion.engine.window;

import static org.lwjgl.glfw.GLFW.*;

public class WindowUtils {

    public static void pollEvents() {
        glfwPollEvents();
    }
}
