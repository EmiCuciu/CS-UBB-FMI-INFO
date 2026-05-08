#version 330 core
in vec2 UV;
uniform float time;
out vec4 FragColor;

void main() {
    float t = clamp((sin(time * 0.05) + 1.0) * 0.5, 0.0, 1.0);

    vec3 zenithDay    = vec3(0.20, 0.48, 0.92);
    vec3 horizonDay   = vec3(0.82, 0.62, 0.38);
    vec3 zenithNight  = vec3(0.01, 0.01, 0.08);
    vec3 horizonNight = vec3(0.10, 0.07, 0.18);

    vec3 zenith  = mix(zenithNight,  zenithDay,  t);
    vec3 horizon = mix(horizonNight, horizonDay, t);

    float y = clamp(UV.y, 0.0, 1.0);
    vec3 color = mix(horizon, zenith, y * y);

    FragColor = vec4(color, 1.0);
}
