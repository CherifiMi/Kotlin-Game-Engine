#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

uniform mat4 uProj;
uniform mat4 uView;
uniform float uZoom;

out vec4 fColor;

void main()
{
    fColor = aColor;
    gl_Position = uProj * uView * vec4(aPos, uZoom);
}


/**/


#version 330 core

in vec4 fColor;

out vec4 color;

void main()
{
    color = fColor;
}