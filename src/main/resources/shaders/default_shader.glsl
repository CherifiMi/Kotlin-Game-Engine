#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProj;
uniform mat4 uView;
uniform float uZoom;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
    gl_Position = uProj * uView * vec4(aPos, uZoom);
}


/**/

#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTex[8];

out vec4 color;

void main()
{
    if(fTexId>0){
        int id = int(fTexId);
        //color = vec4(fTexCoords, 0, 1);
        color = fColor * texture(uTex[id], fTexCoords);
    }else{
        color = fColor;
    }

}