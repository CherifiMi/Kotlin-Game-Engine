
#version 330 core

uniform float uTime;
uniform sampler2D TEX;

in vec4 fColor;
in vec2 fTexCoords;

out vec4 color;

void main()
{
    //vec2 uv = gl_FragCoord.xy/sin(uTime);
    //color = vec4(0.5 + 0.5*sin(uTime+uv.yxy),1.0) / fColor;
    color = texture(TEX, fTexCoords);
}
