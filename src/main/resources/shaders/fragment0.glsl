#type fragment
#version 460 core

in vec2 uv;
in vec2 wpos;

uniform sampler2D texture_;

uniform int outline;
//uniform vec2 center;

void main() {
    if (outline == 0) {
        gl_FragColor = texture(texture_, uv);
    } else {
        gl_FragColor = vec4(1,1,1,1);
    }
}