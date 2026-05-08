#version 330 core
in vec2 UV;
uniform sampler2D texDiffuse;
out vec4 FragColor;
void main() {
    vec3 col = texture(texDiffuse, vec2(0.5, UV.y)).rgb;
    FragColor = vec4(col, 1.0);
}
