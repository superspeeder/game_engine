#type fragment
#version 460 core

in vec2 uv;

uniform sampler2D texture_;

//uniform vec2 center;

void main() {
    gl_FragColor = texture(texture_, uv);
}