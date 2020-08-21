package org.delusion.game.event;

import org.delusion.game.CameraMover;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyCallback extends GLFWKeyCallback {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_W || key == GLFW_KEY_UP) {
            if (action == GLFW_PRESS) {
                CameraMover.dy = -CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                CameraMover.dy = 0;
            }
        }
        if (key == GLFW_KEY_S || key == GLFW_KEY_DOWN) {
            if (action == GLFW_PRESS) {
                CameraMover.dy = CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                CameraMover.dy = 0;
            }
        }

        if (key == GLFW_KEY_D || key == GLFW_KEY_RIGHT) {
            if (action == GLFW_PRESS) {
                CameraMover.dx = -CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                CameraMover.dx = 0;
            }
        }
        if (key == GLFW_KEY_A || key == GLFW_KEY_LEFT) {
            if (action == GLFW_PRESS) {
                CameraMover.dx = CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                CameraMover.dx = 0;
            }
        }

    }
}