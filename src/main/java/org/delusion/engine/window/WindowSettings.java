package org.delusion.engine.window;

import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class WindowSettings {

    public int glProfile = GLFW_OPENGL_ANY_PROFILE;

    public int contextVersionMajor = 3;
    public int contextVersionMinor = 3;
    public boolean resizable = false;
    public boolean visible = true;
    public boolean decorated = true;
    public boolean focused = true;
    public boolean auto_iconify = false;
    public boolean floating = false;
    public boolean maximized = false;
    public boolean center_cursor = true;
    public boolean transparent_framebuffer = false;
    public boolean focus_on_show = true;
    public boolean scale_to_monitor = false;

    public int red_bits = 8;
    public int green_bits = 8;
    public int blue_bits = 8;
    public int alpha_bits = 8;
    public int depth_bits = 24;
    public int stencil_bits = 8;

    public int accum_red_bits = 0;
    public int accum_blue_bits = 0;
    public int accum_green_bits = 0;
    public int accum_alpha_bits = 0;

    public int samples = 0;
    public int refresh_rate = GLFW_DONT_CARE;

    public boolean stereo = false;
    public boolean srgb_capable = false;
    public boolean doublebuffer = true;

    public int client_api = GLFW_OPENGL_API;
    public int context_creation_api = GLFW_NATIVE_CONTEXT_API;
    public int context_robustness = GLFW_NO_ROBUSTNESS;
    public int context_release_behavior = GLFW_ANY_RELEASE_BEHAVIOR;

    public boolean opengl_forward_compat = false;
    public boolean opengl_debug_context = false;

    public int window_width = 800, window_height = 800;
    public boolean fullscreen_is_windowed = true;
    public boolean fullscreen = false;
    public String caption = "Window";


    public boolean isFullscreen() {
        return fullscreen;
    }

    // Ignores color bits, refresh rate, and size.
    public boolean isFullscreenWindowed() {
        return fullscreen_is_windowed;
    }

    public void apply() {
        if (isFullscreen() && !isFullscreenWindowed()) {
            hint(GLFW_RED_BITS, red_bits);
            hint(GLFW_GREEN_BITS, green_bits);
            hint(GLFW_BLUE_BITS, blue_bits);
            hint(GLFW_REFRESH_RATE, refresh_rate);
        }

        hint(GLFW_RESIZABLE, resizable);
        hint(GLFW_VISIBLE, visible);
        hint(GLFW_DECORATED,decorated);
        hint(GLFW_FOCUSED,focused);
        hint(GLFW_AUTO_ICONIFY,auto_iconify);
        hint(GLFW_FLOATING,floating);
        hint(GLFW_MAXIMIZED,maximized);
        hint(GLFW_CENTER_CURSOR,center_cursor);
        hint(GLFW_TRANSPARENT_FRAMEBUFFER,transparent_framebuffer);
        hint(GLFW_FOCUS_ON_SHOW,focus_on_show);
        hint(GLFW_SCALE_TO_MONITOR,scale_to_monitor);

        hint(GLFW_ALPHA_BITS,alpha_bits);
        hint(GLFW_DEPTH_BITS,depth_bits);
        hint(GLFW_STENCIL_BITS,stencil_bits);

        hint(GLFW_ACCUM_RED_BITS,accum_red_bits);
        hint(GLFW_ACCUM_GREEN_BITS,accum_green_bits);
        hint(GLFW_ACCUM_BLUE_BITS,accum_blue_bits);
        hint(GLFW_ACCUM_ALPHA_BITS,accum_alpha_bits);

        hint(GLFW_SAMPLES,samples);

        hint(GLFW_STEREO,stereo);
        hint(GLFW_SRGB_CAPABLE,srgb_capable);
        hint(GLFW_DOUBLEBUFFER,doublebuffer);

        hint(GLFW_CLIENT_API,client_api);
        hint(GLFW_CONTEXT_CREATION_API,context_creation_api);
        hint(GLFW_CONTEXT_ROBUSTNESS,context_robustness);
        hint(GLFW_CONTEXT_RELEASE_BEHAVIOR,context_release_behavior);

        hint(GLFW_OPENGL_FORWARD_COMPAT,opengl_forward_compat);
        hint(GLFW_OPENGL_DEBUG_CONTEXT,opengl_debug_context);

        hint(GLFW_CONTEXT_VERSION_MAJOR,contextVersionMajor);
        hint(GLFW_CONTEXT_VERSION_MINOR,contextVersionMinor);
    }

    private static void hint(int hint, int value) {
        glfwWindowHint(hint,value);
    }

    private static void hint(int hint, boolean value) {
        if (value) {
            hint(hint, GLFW_TRUE);
        } else {
            hint(hint, GLFW_FALSE);
        }

    }


    public int getWidth() {
        return window_width;
    }

    public int getHeight() {
        return window_height;
    }

    public void makeFullscreenSize() {
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);

        window_width = vidMode.width();
        window_height = vidMode.height();
    }

    public void debugMode(boolean debugMode) {
        opengl_debug_context = debugMode;
    }

    public enum GLProfile {
        COMPAT,
        CORE,
        ANY
    }


    public WindowSettings profile(GLProfile profile) {
        switch (profile) {
            case ANY:
                glProfile = GLFW_OPENGL_ANY_PROFILE;
                break;
            case CORE:
                glProfile = GLFW_OPENGL_CORE_PROFILE;
                break;
            case COMPAT:
                glProfile = GLFW_OPENGL_COMPAT_PROFILE;
                break;
        }
        return this;
    }

    public WindowSettings glVersion(int major, int minor) {
        contextVersionMajor = major;
        contextVersionMinor = minor;
        return this;
    }
}
