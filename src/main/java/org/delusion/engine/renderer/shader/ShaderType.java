package org.delusion.engine.renderer.shader;

import org.lwjgl.opengl.GL46;

public enum ShaderType {
    VERTEX(GL46.GL_VERTEX_SHADER),
    FRAGMENT(GL46.GL_FRAGMENT_SHADER),
    GEOMETRY(GL46.GL_GEOMETRY_SHADER),
    TESSCONTROL(GL46.GL_TESS_CONTROL_SHADER),
    TESSEVAL(GL46.GL_TESS_EVALUATION_SHADER),
    COMPUTE(GL46.GL_COMPUTE_SHADER)
    ;

    public final int shader_type_id;

    ShaderType(int shader_type_id) {

        this.shader_type_id = shader_type_id;
    }

    public static ShaderType from(String line0) {
        // #type <type>
        String tn = line0.substring(6);
        return ShaderType.valueOf(tn.toUpperCase());
    }
}
