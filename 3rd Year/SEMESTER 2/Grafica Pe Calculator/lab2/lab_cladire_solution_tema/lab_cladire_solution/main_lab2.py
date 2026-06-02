import math
from pathlib import Path

import moderngl
import numpy as np
import pyglet
from pyglet.window import key
from pyrr import Matrix44, Vector3
from PIL import Image


# ---------------- SHADERS ----------------

VERTEX_SHADER = """
#version 330

in vec3 in_position;
in vec3 in_normal;
in vec2 in_uv;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 frag_pos;
out vec3 frag_normal;
out vec2 frag_uv;

void main() {
    vec4 world_pos = model * vec4(in_position, 1.0);
    frag_pos = world_pos.xyz;
    frag_normal = mat3(transpose(inverse(model))) * in_normal;
    frag_uv = in_uv;
    gl_Position = projection * view * world_pos;
}
"""

FRAGMENT_SHADER = """
#version 330

in vec3 frag_pos;
in vec3 frag_normal;
in vec2 frag_uv;

uniform vec3 object_color;
uniform vec3 light_dir;
uniform vec3 camera_pos;

uniform sampler2D tex;
uniform int use_texture;

out vec4 f_color;

void main() {
    vec3 N = normalize(frag_normal);
    vec3 L = normalize(-light_dir);
    vec3 V = normalize(camera_pos - frag_pos);
    vec3 R = reflect(-L, N);

    float ambient = 0.25;
    float diffuse = max(dot(N, L), 0.0);
    float specular = pow(max(dot(V, R), 0.0), 32.0) * 0.20;

    vec3 base_color = object_color;

    if (use_texture == 1) {
        base_color = texture(tex, frag_uv).rgb;
    }

    vec3 color = base_color * (ambient + diffuse) + vec3(specular);
    f_color = vec4(color, 1.0);
}
"""


# ---------------- UTILS ----------------

def normalize(v):
    norm = np.linalg.norm(v)
    return v if norm < 1e-8 else v / norm


def load_obj(path):
    positions, normals, texcoords = [], [], []
    final_vertices = []

    with open(path, "r") as f:
        for line in f:
            if line.startswith("v "):
                positions.append(tuple(map(float, line.split()[1:4])))
            elif line.startswith("vn "):
                normals.append(tuple(map(float, line.split()[1:4])))
            elif line.startswith("vt "):
                texcoords.append(tuple(map(float, line.split()[1:3])))
            elif line.startswith("f "):
                face = []
                for tok in line.split()[1:]:
                    parts = tok.split("/")
                    v = int(parts[0]) - 1
                    t = int(parts[1]) - 1 if len(parts) > 1 and parts[1] else None
                    n = int(parts[2]) - 1 if len(parts) > 2 and parts[2] else None
                    face.append((v, t, n))

                for i in range(1, len(face) - 1):
                    tri = [face[0], face[i], face[i + 1]]

                    tri_pos = np.array([positions[v[0]] for v in tri], dtype="f4")

                    if all(v[2] is not None for v in tri):
                        tri_nrm = [normals[v[2]] for v in tri]
                    else:
                        e1 = tri_pos[1] - tri_pos[0]
                        e2 = tri_pos[2] - tri_pos[0]
                        n = normalize(np.cross(e1, e2))
                        tri_nrm = [n, n, n]

                    for (v, t, n), pos, norm in zip(tri, tri_pos, tri_nrm):
                        uv = texcoords[t] if t is not None and t < len(texcoords) else (0.0, 0.0)
                        final_vertices.extend([*pos, *norm, *uv])

    return np.array(final_vertices, dtype="f4")


# ---------------- CAMERA ----------------

class Camera:
    def __init__(self):
        self.position = Vector3([0.0, 4.0, 10.0])
        self.front = Vector3([0.0, 0.0, -1.0])
        self.world_up = Vector3([0.0, 1.0, 0.0])

        self.yaw = -90.0
        self.pitch = -15.0

        self.speed = 8.0
        self.sensitivity = 0.1

        self.update_vectors()

    def update_vectors(self):
        front = np.array([
            math.cos(math.radians(self.yaw)) * math.cos(math.radians(self.pitch)),
            math.sin(math.radians(self.pitch)),
            math.sin(math.radians(self.yaw)) * math.cos(math.radians(self.pitch)),
        ], dtype="f4")

        self.front = Vector3(normalize(front))
        self.right = Vector3(normalize(np.cross(self.front, self.world_up)))
        self.up = Vector3(normalize(np.cross(self.right, self.front)))

    def get_view_matrix(self):
        return Matrix44.look_at(self.position, self.position + self.front, self.up, dtype="f4")

    def process_keyboard(self, keys, dt):
        v = self.speed * dt
        if keys[key.W]: self.position += self.front * v
        if keys[key.S]: self.position -= self.front * v
        if keys[key.A]: self.position -= self.right * v
        if keys[key.D]: self.position += self.right * v

    def process_mouse(self, dx, dy):
        self.yaw += dx * self.sensitivity
        self.pitch += dy * self.sensitivity
        self.pitch = max(-89, min(89, self.pitch))
        self.update_vectors()


# ---------------- MAIN WINDOW ----------------

class SceneViewer(pyglet.window.Window):
    def __init__(self, **kwargs):
        super().__init__(resizable=True, **kwargs)

        self.ctx = moderngl.create_context()
        self.ctx.enable(moderngl.DEPTH_TEST)

        self.keys = key.KeyStateHandler()
        self.push_handlers(self.keys)

        self.program = self.ctx.program(
            vertex_shader=VERTEX_SHADER,
            fragment_shader=FRAGMENT_SHADER
        )

        assets = Path(__file__).parent

        # MODELE
        self.building = self._vao(load_obj(assets / "cladire.obj"))
        self.fence = self._vao(load_obj(assets / "gard.obj"))
        self.tree = self._vao(load_obj(assets / "pom.obj"))
        self.cube = self._vao(load_obj(assets / "cube.obj"))

        # TEXTURA
        img = Image.open(assets / "crate.jpg").transpose(Image.FLIP_TOP_BOTTOM)
        self.texture = self.ctx.texture(img.size, 3, img.tobytes())
        self.texture.build_mipmaps()
        self.program["tex"].value = 0

        # CAMERA
        self.camera = Camera()

        # MATRICE MODEL
        model = Matrix44.identity(dtype="f4")
        self.program["model"].write(model.astype("f4").tobytes())

        # LUMINĂ
        self.program["light_dir"].value = (-0.6, -1.0, -0.3)

        self.update_projection()

        pyglet.clock.schedule_interval(self.update, 1/60)

    def _vao(self, data):
        vbo = self.ctx.buffer(data.tobytes())
        return self.ctx.vertex_array(
            self.program,
            [(vbo, "3f 3f 2f", "in_position", "in_normal", "in_uv")]
        )

    def update_projection(self):
        aspect = self.width / self.height
        proj = Matrix44.perspective_projection(60.0, aspect, 0.1, 100.0, dtype="f4")
        self.program["projection"].write(proj.astype("f4").tobytes())

    def on_resize(self, w, h):
        self.ctx.viewport = (0, 0, w, h)
        self.update_projection()

    def on_draw(self):
        self.clear()
        self.ctx.clear(0.1, 0.12, 0.16)

        view = self.camera.get_view_matrix()
        self.program["view"].write(view.astype("f4").tobytes())
        self.program["camera_pos"].value = tuple(self.camera.position)

        # CLADIRE
        self.program["use_texture"].value = 0
        self.program["object_color"].value = (0.7, 0.7, 0.7)
        self.building.render()

        # GARD
        self.program["object_color"].value = (0.4, 0.25, 0.15)
        self.fence.render()

        # POM
        self.program["object_color"].value = (0.2, 0.6, 0.2)
        self.tree.render()

        # CUB TEXTURAT 🔥
        self.program["use_texture"].value = 1
        self.texture.use()
        self.cube.render()

    def update(self, dt):
        self.camera.process_keyboard(self.keys, dt)

    def on_mouse_motion(self, x, y, dx, dy):
        self.camera.process_mouse(dx, dy)


# ---------------- RUN ----------------

def main():
    SceneViewer(width=1280, height=720)
    pyglet.app.run()


if __name__ == "__main__":
    main()