package org.delusion.engine.window;

import org.lwjgl.glfw.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long window;

    public Window(WindowSettings settings) {

        if (settings.isFullscreen()) {
            if (settings.isFullscreenWindowed()) {
                settings.apply();

                long monitor = glfwGetPrimaryMonitor();
                GLFWVidMode vidMode = glfwGetVideoMode(monitor);
                glfwWindowHint(GLFW_RED_BITS, vidMode.redBits());
                glfwWindowHint(GLFW_GREEN_BITS, vidMode.greenBits());
                glfwWindowHint(GLFW_BLUE_BITS, vidMode.blueBits());
                glfwWindowHint(GLFW_REFRESH_RATE, vidMode.refreshRate());

                window = glfwCreateWindow(vidMode.width(), vidMode.height(), settings.caption, monitor, NULL);
            } else {
                settings.apply();
                long monitor = glfwGetPrimaryMonitor();
                window = glfwCreateWindow(settings.getWidth(), settings.getHeight(), settings.caption, monitor, NULL);

            }
        } else {
            window = glfwCreateWindow(settings.getWidth(), settings.getHeight(), settings.caption, NULL, NULL);
        }

    }

    public void setKeyCallback(GLFWKeyCallback keyCallback) {
        glfwSetKeyCallback(window,keyCallback);
    }

    public void setCharCallback(GLFWCharCallback charCallback) {
        glfwSetCharCallback(window,charCallback);
    }

    public void setCursorPosCallback(GLFWCursorPosCallback cursorPosCallback) {
        glfwSetCursorPosCallback(window,cursorPosCallback);
    }

    public void setCursorMode(int mode) {
        glfwSetInputMode(window,GLFW_CURSOR,mode);
    }

    public void enableRawMouseMotion() {
        if(glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window,GLFW_RAW_MOUSE_MOTION,GLFW_TRUE);
        }
    }
    public void disableRawMouseMotion() {
        if(glfwRawMouseMotionSupported()) {
            glfwSetInputMode(window,GLFW_RAW_MOUSE_MOTION,GLFW_FALSE);
        }
    }

    public void setCursorEnterCallback(GLFWCursorEnterCallback callback) {
        glfwSetCursorEnterCallback(window,callback);
    }

    public boolean isCursorOverWindow() {
        return glfwGetWindowAttrib(window,GLFW_HOVERED) == GLFW_TRUE;
    }

    public void setMouseButtonCallback(GLFWMouseButtonCallback callback) {
        glfwSetMouseButtonCallback(window,callback);
    }

    public void setScrollCallback(GLFWScrollCallback callback) {
        glfwSetScrollCallback(window,callback);
    }

    public void setJoystickCallback(GLFWJoystickCallback callback) {
        glfwSetJoystickCallback(callback);
    }

    public double getTime() {
        return glfwGetTime();
    }

    public String getClipboardText() {
        return glfwGetClipboardString(window);
    }

    public void setClipboardText(String text){
        glfwSetClipboardString(window,text);
    }

    public void setPathDropCallback(GLFWDropCallback callback) {
        glfwSetDropCallback(window, callback);
    }



    public void makeContextCurrent() {
        glfwMakeContextCurrent(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public int getWidth() {
        int[] w = new int[1];
        glfwGetWindowSize(window, w, null);
        return w[0];
    }


    public int getHeight() {
        int[] h = new int[1];
        glfwGetWindowSize(window,null,h);
        return h[0];
    }

    public void getCursorPos(DoubleBuffer x, DoubleBuffer y) {
        x.rewind();
        y.rewind();
        glfwGetCursorPos(window,x,y);
        x.rewind();
        y.rewind();
    }
}