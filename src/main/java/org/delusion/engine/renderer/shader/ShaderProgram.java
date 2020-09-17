package org.delusion.engine.renderer.shader;

import org.delusion.engine.resources.ResourceUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram {

    private int programID;

    public int readShaderFile(String path) throws IOException {
        String content = ResourceUtils.readString(path);
        List<String> lines = Arrays.stream(content.split("\n")).collect(Collectors.toList());
        String line0 = lines.get(0);
        String line1 = lines.get(1);
        ShaderType type = ShaderType.from(line0);


        StringBuilder shadercode_builder = new StringBuilder();
        for (List<String> strings : Arrays.asList(lines.subList(1, lines.size()))) {
            for (String string : strings) {
                shadercode_builder.append(string).append('\n');
            }
        }
        String shadercode = shadercode_builder.toString();
//        System.out.println("shadercode = " + shadercode_builder);

        int shader = glCreateShader(type.shader_type_id);
        glShaderSource(shader,shadercode);
        glCompileShader(shader);



System.out.println("======================================");
System.out.println("Shader Type: " + type.name());
System.out.println("Shader Version: " + line1.substring(9).toUpperCase());
System.out.println("Compile Status: " + (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_TRUE ? true : false));
        String ilog = glGetShaderInfoLog(shader);
        if (!ilog.isEmpty())
System.out.println(ilog);

        return shader;
    }

    public ShaderProgram(String... shaderPaths) {
        List<Integer> shaders = new ArrayList<>();

        Arrays.asList(shaderPaths).forEach(path -> {
            try {
                shaders.add(readShaderFile(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        programID = glCreateProgram();
        shaders.forEach(integer -> {
            glAttachShader(programID,integer);
        });
        glLinkProgram(programID);

        int attachedShaderCount = glGetProgrami(programID,GL_ATTACHED_SHADERS);
        int atomicCounterBuffers = glGetProgrami(programID,GL_ACTIVE_ATOMIC_COUNTER_BUFFERS);
        int attributes = glGetProgrami(programID,GL_ACTIVE_ATTRIBUTES);
        int shaderCount = glGetProgrami(programID,GL_ACTIVE_UNIFORMS);
        int link_status = glGetProgrami(programID, GL_LINK_STATUS);
        

System.out.println("======================================");
System.out.println("Program ID: " + programID);
System.out.println("Link Status: " + (link_status == GL_TRUE ? true : false));
System.out.println("Attached Shader Count: " + attachedShaderCount);
System.out.println("Atomic Counter Buffer Count: " + atomicCounterBuffers);
System.out.println("Attribute Count: " + attributes);
System.out.println("Shader Count: " + shaderCount);
        for (int i = 0; i < shaderCount; i++) {
            IntBuffer size = BufferUtils.createIntBuffer(1);
            IntBuffer type = BufferUtils.createIntBuffer(1);

            String name = glGetActiveUniform(programID, i, size, type);
System.out.println("--------------------------------------");
System.out.println("Uniform Name: " + name);
System.out.println("Uniform Type: " + getTypeString(type.get()));

            uniformMap.put(name,i);

        }
System.out.println("======================================");

System.out.println(glGetProgramInfoLog(programID));
    }

    private Map<String, Integer> uniformMap = new HashMap<>();

    public void use() {
        glUseProgram(programID);
    }

    public void uniform1i(String name, int value) {
        int l = getUniformLoc(name);
        glUniform1i(l,value);
    }

    private int getUniformLoc(String name) {
        if (uniformMap.containsKey(name)) return uniformMap.get(name);
        return glGetUniformLocation(programID, name);
    }

    public void uniform2i(String name, int x, int y) {
        int l = getUniformLoc(name);
        glUniform2i(l,x,y);
    }

    public void uniform3i(String name, int x, int y, int z) {
        int l = getUniformLoc(name);
        glUniform3i(l,x,y,z);
    }

    public void uniform4i(String name, int x, int y, int z, int w) {
        int l = getUniformLoc(name);
        glUniform4i(l,x,y,z,w);
    }

    public void uniform1f(String name, float value) {
        int l = getUniformLoc(name);
        glUniform1f(l,value);
    }

    public void uniform2f(String name, float x, float y) {
        int l = getUniformLoc(name);
        glUniform2f(l,x,y);
    }

    public void uniform3f(String name, float x, float y, float z) {
        int l = getUniformLoc(name);
        glUniform3f(l,x,y,z);
    }

    public void uniform4f(String name, float x, float y, float z, float w) {
        int l = getUniformLoc(name);
        glUniform4f(l,x,y,z,w);
    }

    public void uniform1d(String name, double value) {
        int l = getUniformLoc(name);
        glUniform1d(l,value);
    }

    public void uniform2d(String name, double x, double y) {
        int l = getUniformLoc(name);
        glUniform2d(l,x,y);
    }

    public void uniform3d(String name, double x, double y, double z) {
        int l = getUniformLoc(name);
        glUniform3d(l,x,y,z);
    }

    public void uniform4d(String name, double x, double y, double z, double w) {
        int l = getUniformLoc(name);
        glUniform4d(l,x,y,z,w);
    }

    public void uniform1ui(String name, int value) {
        if (value < 0) throw new IllegalArgumentException("value cant be negative");

        int l = getUniformLoc(name);
        glUniform1ui(l,value);
    }

    public void uniform2ui(String name, int x, int y) {
        if (x < 0) throw new IllegalArgumentException("x cant be negative");
        if (y < 0) throw new IllegalArgumentException("y cant be negative");

        int l = getUniformLoc(name);
        glUniform2ui(l,x,y);
    }

    public void uniform3ui(String name, int x, int y, int z) {
        if (x < 0) throw new IllegalArgumentException("x cant be negative");
        if (y < 0) throw new IllegalArgumentException("y cant be negative");
        if (z < 0) throw new IllegalArgumentException("z cant be negative");

        int l = getUniformLoc(name);
        glUniform3ui(l,x,y,z);
    }

    public void uniform4ui(String name, int x, int y, int z, int w) {
        if (x < 0) throw new IllegalArgumentException("x cant be negative");
        if (y < 0) throw new IllegalArgumentException("y cant be negative");
        if (z < 0) throw new IllegalArgumentException("z cant be negative");
        if (w < 0) throw new IllegalArgumentException("w cant be negative");

        int l = getUniformLoc(name);
        glUniform4ui(l,x,y,z,w);
    }

    public void uniformMat4(String name, Matrix4f mat4) {
        float[] data = new float[16];
        mat4.get(data);

        int l = getUniformLoc(name);
        glUniformMatrix4fv(l, false, data);
    }


    private String getTypeString(int i) {
        switch (i) {
            case GL_FLOAT:
                return "float";
            case GL_FLOAT_VEC2:
                return "vec2";
            case GL_FLOAT_VEC3:
                return "vec3";
            case GL_FLOAT_VEC4:
                return "vec4";

            case GL_INT:
                return "int";
            case GL_INT_VEC2:
                return "ivec2";
            case GL_INT_VEC3:
                return "ivec3";
            case GL_INT_VEC4:
                return "ivec4";

            case GL_DOUBLE:
                return "double";
            case GL_DOUBLE_VEC2:
                return "dvec2";
            case GL_DOUBLE_VEC3:
                return "dvec3";
            case GL_DOUBLE_VEC4:
                return "dvec4";

            case GL_UNSIGNED_INT:
                return "unsigned int";
            case GL_UNSIGNED_INT_VEC2:
                return "uvec2";
            case GL_UNSIGNED_INT_VEC3:
                return "uvec3";
            case GL_UNSIGNED_INT_VEC4:
                return "uvec4";

            case GL_BOOL:
                return "bool";
            case GL_BOOL_VEC2:
                return "bvec2";
            case GL_BOOL_VEC3:
                return "bvec3";
            case GL_BOOL_VEC4:
                return "bvec4";

            case GL_FLOAT_MAT2:
                return "mat2";
            case GL_FLOAT_MAT2x3:
                return "mat2x3";
            case GL_FLOAT_MAT2x4:
                return "mat2x4";
            case GL_FLOAT_MAT3:
                return "mat3";
            case GL_FLOAT_MAT3x2:
                return "mat3x2";
            case GL_FLOAT_MAT3x4:
                return "mat3x4";
            case GL_FLOAT_MAT4:
                return "mat4";
            case GL_FLOAT_MAT4x2:
                return "mat4x2";
            case GL_FLOAT_MAT4x3:
                return "mat4x3";

            case GL_DOUBLE_MAT2:
                return "dmat2";
            case GL_DOUBLE_MAT2x3:
                return "dmat2x3";
            case GL_DOUBLE_MAT2x4:
                return "dmat2x4";
            case GL_DOUBLE_MAT3:
                return "dmat3";
            case GL_DOUBLE_MAT3x2:
                return "dmat3x2";
            case GL_DOUBLE_MAT3x4:
                return "dmat3x4";
            case GL_DOUBLE_MAT4:
                return "dmat4";
            case GL_DOUBLE_MAT4x2:
                return "dmat4x2";
            case GL_DOUBLE_MAT4x3:
                return "dmat4x3";

            case GL_SAMPLER_1D:
                return "sampler1D";
            case GL_SAMPLER_2D:
                return "sampler2D";
            case GL_SAMPLER_3D:
                return "sampler3D";
            case GL_SAMPLER_CUBE:
                return "samplerCube";

            case GL_SAMPLER_1D_SHADOW:
                return "sampler1DShadow";
            case GL_SAMPLER_2D_SHADOW:
                return "sampler2DShadow";

            case GL_SAMPLER_1D_ARRAY:
                return "sampler1DArray";
            case GL_SAMPLER_2D_ARRAY:
                return "sampler2DArray";

            case GL_SAMPLER_1D_ARRAY_SHADOW:
                return "sampler1DArrayShadow";
            case GL_SAMPLER_2D_ARRAY_SHADOW:
                return "sampler2DArrayShadow";

            case GL_SAMPLER_2D_MULTISAMPLE:
                return "sampler2DMS";
            case GL_SAMPLER_2D_MULTISAMPLE_ARRAY:
                return "sampler2DMSArray";

            case GL_SAMPLER_CUBE_SHADOW:
                return "samplerCubeShadow";

            case GL_SAMPLER_BUFFER:
                return "samplerBuffer";

            case GL_SAMPLER_2D_RECT:
                return "sampler2DRect";
            case GL_SAMPLER_2D_RECT_SHADOW:
                return "sampler2DRectShadow";

            case GL_INT_SAMPLER_1D:
                return "isampler1D";
            case GL_INT_SAMPLER_2D:
                return "isampler2D";
            case GL_INT_SAMPLER_3D:
                return "isampler3D";
            case GL_INT_SAMPLER_CUBE:
                return "isamplerCube";

            case GL_INT_SAMPLER_1D_ARRAY:
                return "isampler1DArray";
            case GL_INT_SAMPLER_2D_ARRAY:
                return "isampler2DArray";

            case GL_INT_SAMPLER_2D_MULTISAMPLE:
                return "isampler2DMS";
            case GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY:
                return "isampler2DMSArray";

            case GL_INT_SAMPLER_BUFFER:
                return "isamplerBuffer";

            case GL_INT_SAMPLER_2D_RECT:
                return "isampler2DRect";

            case GL_UNSIGNED_INT_SAMPLER_1D:
                return "usampler1D";
            case GL_UNSIGNED_INT_SAMPLER_2D:
                return "usampler2D";
            case GL_UNSIGNED_INT_SAMPLER_3D:
                return "usampler3D";
            case GL_UNSIGNED_INT_SAMPLER_CUBE:
                return "usamplerCube";

            case GL_UNSIGNED_INT_SAMPLER_1D_ARRAY:
                return "usampler1DArray";
            case GL_UNSIGNED_INT_SAMPLER_2D_ARRAY:
                return "usampler2DArray";

            case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE:
                return "usampler2DMS";
            case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY:
                return "usampler2DMSArray";

            case GL_UNSIGNED_INT_SAMPLER_BUFFER:
                return "usamplerBuffer";

            case GL_UNSIGNED_INT_SAMPLER_2D_RECT:
                return "usampler2DRect";

            case GL_IMAGE_1D:
                return "image1D";
            case GL_IMAGE_2D:
                return "image2D";
            case GL_IMAGE_3D:
                return "image3D";
            case GL_IMAGE_CUBE:
                return "imageCube";
            case GL_IMAGE_BUFFER:
                return "imageBuffer";
            case GL_IMAGE_1D_ARRAY:
                return "image1DArray";
            case GL_IMAGE_2D_ARRAY:
                return "image2DArray";
            case GL_IMAGE_2D_MULTISAMPLE:
                return "image2DMS";
            case GL_IMAGE_2D_MULTISAMPLE_ARRAY:
                return "image2DMSArray";

            case GL_INT_IMAGE_1D:
                return "iimage1D";
            case GL_INT_IMAGE_2D:
                return "iimage2D";
            case GL_INT_IMAGE_3D:
                return "iimage3D";
            case GL_INT_IMAGE_CUBE:
                return "iimageCube";
            case GL_INT_IMAGE_BUFFER:
                return "iimageBuffer";
            case GL_INT_IMAGE_1D_ARRAY:
                return "iimage1DArray";
            case GL_INT_IMAGE_2D_ARRAY:
                return "iimage2DArray";
            case GL_INT_IMAGE_2D_MULTISAMPLE:
                return "iimage2DMS";
            case GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY:
                return "iimage2DMSArray";

            case GL_UNSIGNED_INT_IMAGE_1D:
                return "uimage1D";
            case GL_UNSIGNED_INT_IMAGE_2D:
                return "uimage2D";
            case GL_UNSIGNED_INT_IMAGE_3D:
                return "uimage3D";
            case GL_UNSIGNED_INT_IMAGE_CUBE:
                return "uimageCube";
            case GL_UNSIGNED_INT_IMAGE_BUFFER:
                return "uimageBuffer";
            case GL_UNSIGNED_INT_IMAGE_1D_ARRAY:
                return "uimage1DArray";
            case GL_UNSIGNED_INT_IMAGE_2D_ARRAY:
                return "uimage2DArray";
            case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE:
                return "uimage2DMS";
            case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY:
                return "uimage2DMSArray";

            case GL_UNSIGNED_INT_ATOMIC_COUNTER:
                return "atomic_uint";

            default:
                return Integer.toString(i);
        }
    }

}
