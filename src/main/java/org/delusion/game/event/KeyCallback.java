package org.delusion.game.event;

import org.delusion.engine.TestApp;
import org.delusion.game.CameraMover;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyCallback extends GLFWKeyCallback {
    private boolean left, right, up, down;

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_W || key == GLFW_KEY_UP) {
            if (action == GLFW_PRESS) {
                up = true;
                TestApp.playerCharacter.jump();
//                up = true;
//                CameraMover.dy = -CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                up = false;
//                CameraMover.dy = 0;
            }
        }
        if (key == GLFW_KEY_S || key == GLFW_KEY_DOWN) {
            if (action == GLFW_PRESS) {
                down = true;
//                CameraMover.dy = CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                down = false;
//                CameraMover.dy = 0;
            }
        }

        if (key == GLFW_KEY_D || key == GLFW_KEY_RIGHT) {
            if (action == GLFW_PRESS) {
                right = true;
                CameraMover.dx = -CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                right = false;
                CameraMover.dx = left ? CameraMover.speed : 0;
            }
        }
        if (key == GLFW_KEY_A || key == GLFW_KEY_LEFT) {
            if (action == GLFW_PRESS) {
                left = true;
                CameraMover.dx = CameraMover.speed;
            } else if (action == GLFW_RELEASE) {
                left = false;
                CameraMover.dx = right ? -CameraMover.speed : 0;
            }
        }
    }
}
