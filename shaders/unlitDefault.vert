#version 330 core

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TextureCoords;
layout(location = 2) in vec3 in_Normal;

uniform mat4 u_ModelViewProjectionMatrix;

out vec2 pass_TextureCoords;

void main() {
    pass_TextureCoords = in_TextureCoords;
    gl_Position = u_ModelViewProjectionMatrix * vec4(in_Position, 1.0);
}
