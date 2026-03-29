import math
from pathlib import Path

import moderngl
import numpy as np
import pyglet
from pyglet.window import key
from pyrr import Matrix44, Vector3

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
        # Am mutat camera putin mai departe și mai sus ca să vezi toată curtea
        self.position = Vector3([0.0, 5.0, 15.0])
        self.front = Vector3([0.0, 0.0, -1.0])
        self.world_up = Vector3([0.0, 1.0, 0.0])
        self.right = Vector3([1.0, 0.0, 0.0])
        self.up = Vector3([0.0, 1.0, 0.0])

        self.yaw = -90.0
        self.pitch = -15.0  # Camera privește ușor în jos
        self.speed = 10.0  # Viteză de mișcare ușor mărită pentru o scenă mai mare
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


class SceneViewer(pyglet.window.Window):
    def __init__(self, **kwargs):
        config = pyglet.gl.Config(double_buffer=True, depth_size=24, major_version=3, minor_version=3)
        super().__init__(config=config, resizable=True, caption="Laborator Modern OpenGL - Scena 3D", **kwargs)

        self.ctx = moderngl.create_context()
        self.ctx.enable(moderngl.DEPTH_TEST)
        self.ctx.enable(moderngl.CULL_FACE)

        self.keys = key.KeyStateHandler()
        self.push_handlers(self.keys)

        self.program = self.ctx.program(vertex_shader=VERTEX_SHADER, fragment_shader=FRAGMENT_SHADER)

        # Calea către directorul cu obiecte
        assets_path = Path(__file__).resolve().parent

        # Încărcăm cele 3 obiecte (exportate din Blender)
        # Am creat funcția _create_vao pentru a nu repeta codul de 3 ori
        self.building_vao = self._create_vao(load_obj(str(assets_path / "cladire.obj")))
        self.fence_vao = self._create_vao(load_obj(str(assets_path / "gard.obj")))
        self.tree_vao = self._create_vao(load_obj(str(assets_path / "pom.obj")))

        # Matricea modelului este Matricea Identitate pentru toată scena,
        # deoarece Blender a salvat deja pozițiile globale.
        self.model_matrix = Matrix44.identity(dtype="f4")
        self.program["model"].write(np.array(self.model_matrix, dtype="f4").tobytes())

        # Setăm direcția luminii
        self.program["light_dir"].value = (-0.6, -1.0, -0.3)

        self.camera = Camera()
        self.set_exclusive_mouse(True)
        self.wireframe = False

        self.update_projection()
        pyglet.clock.schedule_interval(self.update, 1 / 120.0)

    def _create_vao(self, vertex_data: np.ndarray):
        """Helper pentru a crea un VAO pe baza array-ului de vertecși."""
        vbo = self.ctx.buffer(vertex_data.tobytes())
        return self.ctx.vertex_array(
            self.program,
            [(vbo, "3f 3f", "in_position", "in_normal")],
        )

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
        self.ctx.clear(0.10, 0.12, 0.16, 1.0)  # Fundal de scenă

        # Actualizăm camera
        view = self.camera.get_view_matrix()
        self.program["view"].write(np.array(view, dtype="f4").tobytes())
        self.program["camera_pos"].value = tuple(self.camera.position)

        # Randează Clădirea
        self.program["object_color"].value = (0.72, 0.74, 0.78)  # Gri pentru clădire
        self.building_vao.render(moderngl.TRIANGLES)

        # Randează Gardul
        self.program["object_color"].value = (0.45, 0.28, 0.18)  # Maro pentru gard
        self.fence_vao.render(moderngl.TRIANGLES)

        # Randează Pomul
        self.program["object_color"].value = (0.15, 0.50, 0.20)  # Verde pentru pom
        self.tree_vao.render(moderngl.TRIANGLES)

    def update(self, dt):
        self.camera.process_keyboard(self.keys, dt)

    def on_mouse_motion(self, x, y, dx, dy):
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
    # Simplificăm funcția main, verificând doar dacă există fișierele
    assets_dir = Path(__file__).resolve().parent

    required_files = ["cladire.obj", "gard.obj", "pom.obj"]
    for f in required_files:
        if not (assets_dir / f).exists():
            raise FileNotFoundError(
                f"Fișierul '{f}' nu a fost găsit în directorul cu main.py. Asigură-te că l-ai exportat din Blender!")

    SceneViewer(width=1280, height=720)
    pyglet.app.run()


if __name__ == "__main__":
    main()