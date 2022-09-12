
#version 330 core

uniform float uTime;
uniform sampler2D TEX;

in vec4 fColor;
in vec2 fTexCoords;

out vec4 color;

void main()
{
    color = texture(TEX, fTexCoords);
}
