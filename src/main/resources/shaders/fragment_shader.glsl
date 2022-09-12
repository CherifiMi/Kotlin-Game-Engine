#version 330 core

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main()
{
    float avg = (fColor.r, fColor.g, fColor.b)/3;
    color = sin(uTime) * fColor;
}