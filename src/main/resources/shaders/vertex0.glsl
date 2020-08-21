#type vertex
#version 460 core

layout(location=0) in vec4 pos;
layout(location=1) in vec2 uv_;


out vec2 uv;
uniform mat4 mvp;

void main() {
//    float x = pos.x;
//    float y = pos.y;
//    float z = pos.z;
//
//    gl_Position = vec4(x, y+0.5f, z, 0.0f);
    gl_Position = mvp * pos;
    uv = uv_;
}