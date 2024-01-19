#version 330 core

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TextureCoords;
layout(location = 2) in vec3 in_Normal;

uniform mat3 u_NormalMatrix;
uniform mat4 u_ModelMatrix;
uniform mat4 u_ModelViewProjectionMatrix;

out vec2 pass_TextureCoords;
out vec3 pass_FragmentPosition;
out vec3 pass_Normal;

void main() {
    pass_TextureCoords = in_TextureCoords;
    pass_FragmentPosition = vec3(u_ModelMatrix * vec4(in_Position, 1.0));
    pass_Normal = u_NormalMatrix * in_Normal;

    gl_Position = u_ModelViewProjectionMatrix * vec4(in_Position, 1.0);
}
