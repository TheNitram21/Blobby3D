#version 330 core

in vec2 pass_TextureCoords;

uniform sampler2D u_Texture;

out vec4 out_Color;

void main() {
    out_Color = texture2D(u_Texture, pass_TextureCoords);
    if(out_Color.a == 0.0)
        discard;
}
