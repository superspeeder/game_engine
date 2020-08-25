package org.delusion.engine.utils;

import org.lwjgl.opengl.GLUtil;

import static org.lwjgl.opengl.GL46.*;

public class GlUtils {
    public static void enableDebug() {
        glEnable(GL_DEBUG_OUTPUT);
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        GLUtil.setupDebugMessageCallback();

        glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DEBUG_SEVERITY_NOTIFICATION,
                new int[0], false);

    }
}
