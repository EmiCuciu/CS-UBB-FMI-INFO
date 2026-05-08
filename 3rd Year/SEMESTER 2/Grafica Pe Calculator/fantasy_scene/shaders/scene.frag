#version 330 core

in vec3 FragPos;
in vec3 Normal;
in vec2 UV;
in mat3 TBN;

uniform sampler2D texDiffuse;
uniform sampler2D texNormal;
uniform bool      useNormalMap;
uniform bool      useEmissive;
uniform vec3      emissiveColor;

uniform vec3  viewPos;
uniform vec3  ambient;
uniform float shininess;

// Lumină direcțională (soare)
struct DirLight {
    vec3 direction;
    vec3 color;
    float intensity;
};
uniform DirLight dirLight;

// Lumini punct (licurici)
struct PointLight {
    vec3  position;
    vec3  color;
    float intensity;
    float linear;
    float quadratic;
};
#define NUM_LIGHTS 3
uniform PointLight pointLights[NUM_LIGHTS];

// Fog
uniform vec3  fogColor;
uniform float fogNear;
uniform float fogFar;

out vec4 FragColor;

vec3 calcNormal() {
    if (!useNormalMap) return normalize(Normal);
    vec3 n = texture(texNormal, UV).rgb;
    n = normalize(n * 2.0 - 1.0);
    return normalize(TBN * n);
}

vec3 calcDirLight(DirLight light, vec3 normal, vec3 viewDir, vec3 diffColor) {
    vec3 lightDir = normalize(-light.direction);
    float diff    = max(dot(normal, lightDir), 0.0);
    vec3 halfway  = normalize(lightDir + viewDir);
    float spec    = pow(max(dot(normal, halfway), 0.0), shininess);
    vec3 diffuse  = diff * light.color * diffColor;
    vec3 specular = spec * light.color * 0.3;
    return (diffuse + specular) * light.intensity;
}

vec3 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir, vec3 diffColor) {
    vec3  lightDir = normalize(light.position - fragPos);
    float dist     = length(light.position - fragPos);
    float atten    = 1.0 / (1.0 + light.linear * dist + light.quadratic * dist * dist);
    float diff     = max(dot(normal, lightDir), 0.0);
    vec3  halfway  = normalize(lightDir + viewDir);
    float spec     = pow(max(dot(normal, halfway), 0.0), shininess);
    vec3 diffuse   = diff * light.color * diffColor;
    vec3 specular  = spec * light.color * 0.2;
    return (diffuse + specular) * light.intensity * atten;
}

void main() {
    vec4 texColor = texture(texDiffuse, UV);
    if (texColor.a < 0.1) discard;

    // Mod emissive — sfere licurici
    if (useEmissive) {
        FragColor = vec4(emissiveColor, 1.0);
        return;
    }

    vec3 diffColor = texColor.rgb;
    vec3 norm      = calcNormal();
    vec3 viewDir   = normalize(viewPos - FragPos);

    // Iluminare
    vec3 color = ambient * diffColor;
    color += calcDirLight(dirLight, norm, viewDir, diffColor);
    for (int i = 0; i < NUM_LIGHTS; i++) {
        color += calcPointLight(pointLights[i], norm, FragPos, viewDir, diffColor);
    }

    // Fog liniar
    float dist = length(viewPos - FragPos);
    float fogF = clamp((dist - fogNear) / (fogFar - fogNear), 0.0, 1.0);
    color = mix(color, fogColor, fogF);

    // Tone mapping Reinhard + gamma correction
    color = color / (color + vec3(1.0));
    color = pow(color, vec3(1.0 / 2.2));

    FragColor = vec4(color, texColor.a);
}
