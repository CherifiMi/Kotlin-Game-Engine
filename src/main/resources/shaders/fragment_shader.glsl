
#version 330 core

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main()
{
    //vec2 uv = gl_FragCoord.xy/sin(uTime);
    //color = vec4(0.5 + 0.5*sin(uTime+uv.yxy),1.0) / fColor;
    color = fColor;
}
