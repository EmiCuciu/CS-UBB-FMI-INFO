import math
from pathlib import Path

import moderngl
import numpy as np
import pyglet
from pyglet.window import key
from pyrr import Matrix44, Vector3, vector, vector3


VERTEX_SHADER = """
#version 330

in vec3 in_position;
in vec3 in_normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 frag_pos;
out vec3 frag_normal;

void main() {
    vec4 world_pos = model * vec4(in_position, 1.0);
    frag_pos = world_pos.xyz;
    frag_normal = mat3(transpose(inverse(model))) * in_normal;
    gl_Position = projection * view * world_pos;
}
"""


FRAGMENT_SHADER = """
#version 330

in vec3 frag_pos;
in vec3 frag_normal;

uniform vec3 object_color;
uniform vec3 light_dir;
uniform vec3 camera_pos;

out vec4 f_color;

void main() {
    vec3 N = normalize(frag_normal);
    vec3 L = normalize(-light_dir);
    vec3 V = normalize(camera_pos - frag_pos);
    vec3 R = reflect(-L, N);

    float ambient = 0.25;
    float diffuse = max(dot(N, L), 0.0);
    float specular = pow(max(dot(V, R), 0.0), 32.0) * 0.20;

    vec3 color = object_color * (ambient + diffuse) + vec3(specular);
    f_color = vec4(color, 1.0);
}
"""


def normalize(v: np.ndarray) -> np.ndarray:
    norm = np.linalg.norm(v)
    if norm < 1e-8:
        return v
    return v / norm


def load_obj(obj_path: str):
    """
    Loader OBJ simplu:
    - citește v și vn
    - ignoră culori, texturi și materiale
    - triangulează fețele dacă este nevoie
    - întoarce vertecși expandați: [x, y, z, nx, ny, nz]
    """
    positions = []
    normals = []
    final_vertices = []

    with open(obj_path, "r", encoding="utf-8", errors="ignore") as f:
        for raw_line in f:
            line = raw_line.strip()
            if not line or line.startswith("#"):
                continue

            if line.startswith("v "):
                _, x, y, z = line.split()[:4]
                positions.append((float(x), float(y), float(z)))

            elif line.startswith("vn "):
                _, x, y, z = line.split()[:4]
                normals.append((float(x), float(y), float(z)))

            elif line.startswith("f "):
                tokens = line.split()[1:]
                face = []
                for tok in tokens:
                    # Formate posibile: v, v//vn, v/vt, v/vt/vn
                    parts = tok.split("/")
                    v_idx = int(parts[0]) - 1
                    vt_present = len(parts) > 1 and parts[1] != ""
                    vn_present = len(parts) > 2 and parts[2] != ""
                    n_idx = int(parts[2]) - 1 if vn_present else None
                    face.append((v_idx, n_idx, vt_present))

                # triangulare tip fan: (0, i, i+1)
                for i in range(1, len(face) - 1):
                    tri = [face[0], face[i], face[i + 1]]

                    tri_positions = np.array([positions[item[0]] for item in tri], dtype="f4")

                    if all(item[1] is not None for item in tri):
                        tri_normals = [normals[item[1]] for item in tri]
                    else:
                        edge1 = tri_positions[1] - tri_positions[0]
                        edge2 = tri_positions[2] - tri_positions[0]
                        computed_normal = normalize(np.cross(edge1, edge2))
                        tri_normals = [computed_normal, computed_normal, computed_normal]

                    for pos, nrm in zip(tri_positions, tri_normals):
                        final_vertices.extend([*pos, *nrm])

    vertex_data = np.array(final_vertices, dtype="f4")
    return vertex_data


class Camera:
    def __init__(self):
        self.position = Vector3([0.0, 2.0, 8.0])
        self.front = Vector3([0.0, 0.0, -1.0])
        self.world_up = Vector3([0.0, 1.0, 0.0])
        self.right = Vector3([1.0, 0.0, 0.0])
        self.up = Vector3([0.0, 1.0, 0.0])

        self.yaw = -90.0
        self.pitch = 0.0
        self.speed = 4.5
        self.sensitivity = 0.10

        self.update_vectors()

    def update_vectors(self):
        front = np.array(
            [
                math.cos(math.radians(self.yaw)) * math.cos(math.radians(self.pitch)),
                math.sin(math.radians(self.pitch)),
                math.sin(math.radians(self.yaw)) * math.cos(math.radians(self.pitch)),
            ],
            dtype="f4",
        )
        self.front = Vector3(normalize(front))
        self.right = Vector3(normalize(np.cross(self.front, self.world_up)))
        self.up = Vector3(normalize(np.cross(self.right, self.front)))

    def get_view_matrix(self):
        target = self.position + self.front
        return Matrix44.look_at(self.position, target, self.up, dtype="f4")

    def process_keyboard(self, keys, dt: float):
        velocity = self.speed * dt
        if keys[key.W]:
            self.position += self.front * velocity
        if keys[key.S]:
            self.position -= self.front * velocity
        if keys[key.A]:
            self.position -= self.right * velocity
        if keys[key.D]:
            self.position += self.right * velocity
        if keys[key.Q]:
            self.position -= self.world_up * velocity
        if keys[key.E]:
            self.position += self.world_up * velocity

    def process_mouse(self, dx: float, dy: float):
        self.yaw += dx * self.sensitivity
        self.pitch += dy * self.sensitivity

        self.pitch = max(-89.0, min(89.0, self.pitch))
        self.update_vectors()


class BuildingViewer(pyglet.window.Window):
    def __init__(self, obj_path: str, **kwargs):
        config = pyglet.gl.Config(double_buffer=True, depth_size=24, major_version=3, minor_version=3)
        super().__init__(config=config, resizable=True, caption="Laborator Modern OpenGL - cladire OBJ", **kwargs)

        self.ctx = moderngl.create_context()
        self.ctx.enable(moderngl.DEPTH_TEST)
        self.ctx.enable(moderngl.CULL_FACE)

        self.keys = key.KeyStateHandler()
        self.push_handlers(self.keys)

        self.program = self.ctx.program(vertex_shader=VERTEX_SHADER, fragment_shader=FRAGMENT_SHADER)

        raw_vertex_data = load_obj(obj_path)
        self.vertex_data, self.model_matrix = self.prepare_model(raw_vertex_data)

        self.vbo = self.ctx.buffer(self.vertex_data.tobytes())
        self.vao = self.ctx.vertex_array(
            self.program,
            [
                (self.vbo, "3f 3f", "in_position", "in_normal"),
            ],
        )

        self.camera = Camera()
        self.set_exclusive_mouse(True)
        self.wireframe = False

        self.program["model"].write(self.model_matrix.astype("f4").tobytes())
        self.program["object_color"].value = (0.72, 0.74, 0.78)
        self.program["light_dir"].value = (-0.6, -1.0, -0.3)

        self.update_projection()
        pyglet.clock.schedule_interval(self.update, 1 / 120.0)

    def prepare_model(self, vertex_data: np.ndarray):
        positions = vertex_data.reshape(-1, 6)[:, 0:3]
        min_corner = positions.min(axis=0)
        max_corner = positions.max(axis=0)
        center = (min_corner + max_corner) / 2.0
        size = max_corner - min_corner
        max_extent = np.max(size)
        scale = 4.0 / max_extent if max_extent > 0 else 1.0

        prepared = vertex_data.copy().reshape(-1, 6)
        prepared[:, 0:3] = (prepared[:, 0:3] - center) * scale

        model = Matrix44.identity(dtype="f4")
        return prepared.astype("f4").reshape(-1), np.array(model, dtype="f4")

    def update_projection(self):
        aspect = self.width / self.height if self.height else 1.0
        projection = Matrix44.perspective_projection(60.0, aspect, 0.1, 100.0, dtype="f4")
        self.program["projection"].write(np.array(projection, dtype="f4").tobytes())

    def on_resize(self, width, height):
        super().on_resize(width, height)
        self.ctx.viewport = (0, 0, width, height)
        self.update_projection()

    def on_draw(self):
        self.clear()
        self.ctx.clear(0.10, 0.12, 0.16, 1.0)

        view = self.camera.get_view_matrix()
        self.program["view"].write(np.array(view, dtype="f4").tobytes())
        self.program["camera_pos"].value = tuple(self.camera.position)
        self.vao.render(moderngl.TRIANGLES)

    def update(self, dt):
        self.camera.process_keyboard(self.keys, dt)

    def on_mouse_motion(self, x, y, dx, dy):
        # pe axa Y inversăm semnul ca mișcarea să fie naturală
        self.camera.process_mouse(dx, dy)

    def on_key_press(self, symbol, modifiers):
        if symbol == key.ESCAPE:
            self.close()
        elif symbol == key.M:
            self.set_exclusive_mouse(not self._mouse_exclusive)
        elif symbol == key.F:
            self.wireframe = not self.wireframe
            self.ctx.wireframe = self.wireframe
        elif symbol == key.R:
            self.camera = Camera()


def main():
    obj_file = Path(__file__).resolve().parent / "cladire.obj"
    if not obj_file.exists():
        raise FileNotFoundError(
            "Fișierul 'cladire.obj' nu a fost găsit în același director cu main.py"
        )

    BuildingViewer(obj_path=str(obj_file), width=1280, height=720)
    pyglet.app.run()


if __name__ == "__main__":
    main()
