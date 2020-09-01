#type fragment
#version 330 core

in vec2 uv;
in vec2 wpos;

out vec4 fragColor;

uniform sampler2D texture_;

uniform int outline;
//uniform vec2 center;

vec4 invertOrBlack(vec4 c) {
    if (c.a < 0.5f) {
        return vec4(0,0,0,1);
    } else {
        c.r = 1-c.r;
        c.g = 1-c.g;
        c.b = 1-c.b;
        return c;
    }

}

void main() {
    if (outline == 0) {
        fragColor = texture(texture_, uv);
    } else {
        fragColor = invertOrBlack(texture(texture_, uv));
    }
}