#version 330 core

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aUV;
layout(location = 3) in vec3 aTangent;
// Instanced attributes
layout(location = 4) in vec3 iPosition;
layout(location = 5) in float iRotation;
layout(location = 6) in float iScale;

uniform mat4 view;
uniform mat4 projection;
uniform mat3 normalMatrix;
uniform float time;

out vec3 FragPos;
out vec3 Normal;
out vec2 UV;
out mat3 TBN;

void main() {
    // Animație vânt: firele se mișcă sinusoidal în funcție de înălțime
    float windStrength = aPos.y * 0.12;
    float windX = sin(time * 1.8 + iPosition.x * 0.5 + iPosition.z * 0.3) * windStrength;
    float windZ = cos(time * 1.4 + iPosition.z * 0.4) * windStrength * 0.5;

    vec3 pos = aPos;
    pos.x += windX;
    pos.z += windZ;

    // Rotație per instanță
    float c = cos(iRotation);
    float s = sin(iRotation);
    mat2 rot2 = mat2(c, -s, s, c);
    vec2 rotXZ = rot2 * pos.xz;
    pos = vec3(rotXZ.x, pos.y, rotXZ.y);

    // Scale + translate
    pos = pos * iScale + iPosition;

    FragPos = pos;
    Normal  = normalize(normalMatrix * aNormal);

    vec3 T = normalize(normalMatrix * aTangent);
    T = normalize(T - dot(T, Normal) * Normal);
    vec3 B = cross(Normal, T);
    TBN = mat3(T, B, Normal);

    UV = aUV;
    gl_Position = projection * view * vec4(pos, 1.0);
}
