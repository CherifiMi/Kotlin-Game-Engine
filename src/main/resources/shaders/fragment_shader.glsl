
#version 330 core

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main()
{
    vec2 uv = gl_FragCoord.xy/tan(uTime);
    color = sin(vec4(0.5 + 0.5*cos(uTime+uv.yxy),1.0) / fColor);
}
