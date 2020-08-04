package org.delusion.engine;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class TestApp {
    public static void main(String[] args) {

        if (!glfwInit())
            throw new RuntimeException("GLFW couldn't be initialized");

        long window = glfwCreateWindow(800,800,"Fuck", NULL, NULL);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glClearColor(1,1,1,1);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            glfwPollEvents();
            glfwSwapBuffers(window);
        }

        glfwTerminate();

    }
}
