#version 330 core

#define MAX_POINT_LIGHT_COUNT 1024

struct PointLight {
    vec3 position;
    vec3 color;
    vec3 constLinearQuad;
};

in vec2 pass_TextureCoords;
in vec3 pass_FragmentPosition;
in vec3 pass_Normal;

uniform sampler2D u_Texture;
uniform vec3 u_AmbientLightColor;
uniform PointLight u_PointLights[MAX_POINT_LIGHT_COUNT];
uniform int u_PointLightCount;
uniform vec3 u_CameraPosition;

out vec4 out_Color;

vec3 calculatePointLight(PointLight light) {
    vec3 normal = normalize(pass_Normal);
    vec3 lightDirection = normalize(light.position - pass_FragmentPosition);
    float diff = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = diff * light.color;

    float specularStrength = 0.5;

    vec3 viewDirection = normalize(u_CameraPosition - pass_FragmentPosition);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), 32.0);
    vec3 specular = specularStrength * spec * light.color;

    float distance = length(light.position - pass_FragmentPosition);
    float attenuation = 1.0 / (light.constLinearQuad.x + light.constLinearQuad.y * distance +
            light.constLinearQuad.z * distance * distance);
    return attenuation * (diffuse + specular);
}

vec3 calculateAllPointLights() {
    vec3 lightingResult = vec3(0.0);

    for(int i = 0; i < MAX_POINT_LIGHT_COUNT; i++) {
        if(i >= u_PointLightCount)
            return lightingResult;
        lightingResult += calculatePointLight(u_PointLights[i]);
    }
    return lightingResult;
}

void main() {
    vec3 lighting = u_AmbientLightColor;
    lighting += calculateAllPointLights();
//    lighting += calculatePointLight(u_PointLights[0]);
//    lighting += calculatePointLight(PointLight(vec3(0.0, 2.0, 0.0), vec3(1.0, 1.0, 1.0), 1.0, 0.14, 0.07));
//    lighting += calculatePointLight(PointLight(vec3(-7.0, 2.0, -7.0), vec3(1.0, 1.0, 1.0), 1.0, 0.14, 0.07));

    out_Color = texture2D(u_Texture, pass_TextureCoords) * vec4(lighting, 1.0);
}
