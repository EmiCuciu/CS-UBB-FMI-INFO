"""src/scene.py — Scena principală: obiecte, iluminare, render

Prioritate modele:
  1. assets/models/*.obj  (generate de Blender)
  2. geometrii procedurale din mesh.py (fallback automat)
"""

from OpenGL.GL import *
import glm
import math
import numpy as np
from pathlib import Path

from src.shader  import Shader
from src.camera  import Camera
from src.model   import load_obj_single, load_obj_by_material
from src.texture import (generate_grass_texture, generate_mushroom_cap_texture,
                          generate_bark_texture, generate_bark_normal_map,
                          generate_flower_texture, generate_ground_texture,
                          generate_solid_color, _generate_flat_normal)
from src.mesh    import (make_cylinder, make_sphere, make_plane,
                          make_quad, make_grass_blade, make_rock)


MODELS_DIR = Path("assets/models")


def _load_or(obj_name, fallback_fn):
    """Încearcă să încarce .obj; dacă nu există, folosește geometria procedurală."""
    path = MODELS_DIR / obj_name
    mesh = load_obj_single(path)
    if mesh is not None:
        return mesh, True
    print(f"  → fallback procedural pentru {obj_name}")
    return fallback_fn(), False


class Scene:
    def __init__(self, window):
        self.window = window
        self.camera = Camera(position=(0, 4, 14))
        self.time   = 0.0

    # ── Load ───────────────────────────────────────────────────────────────────

    def load(self):
        print("\nSe încarcă scena...")

        self.shader       = Shader("shaders/scene.vert", "shaders/scene.frag")
        self.grass_shader = Shader("shaders/grass.vert", "shaders/scene.frag")
        self.sky_shader   = Shader("shaders/sky.vert",   "shaders/sky.frag")

        print("Modele:")
        self._mush_meshes = load_obj_by_material(MODELS_DIR / "mushroom.obj")
        if not self._mush_meshes:
            print("  → fallback procedural pentru mushroom.obj")
            self.mesh_mushroom      = make_cylinder(radius=0.18, height=0.9, segments=16)
            self.mesh_mushroom_cap  = make_sphere(radius=0.55, stacks=16, slices=16)
            self.mesh_mushroom_spot = make_sphere(radius=0.06, stacks=6, slices=8)

        self._tree_meshes = load_obj_by_material(MODELS_DIR / "tree.obj")
        if not self._tree_meshes:
            print("  → fallback procedural pentru tree.obj")
            self.mesh_tree  = make_cylinder(radius=0.22, height=2.2, segments=12, top_radius=0.12)
            self.mesh_crown = make_sphere(radius=1.0, stacks=14, slices=14)

        self._flower_meshes = load_obj_by_material(MODELS_DIR / "flower.obj")
        if not self._flower_meshes:
            print("  → fallback procedural pentru flower.obj")
            self.mesh_flower = make_cylinder(radius=0.03, height=0.8, segments=8)
            self.mesh_petal  = make_sphere(radius=0.12, stacks=8, slices=8)
            self.mesh_center = make_sphere(radius=0.12, stacks=8, slices=8)

        self.mesh_grass_blade, _ = _load_or(
            "grass_blade.obj",
            lambda: make_grass_blade(height=0.7, lean=0.12)
        )
        self.mesh_rock, _  = _load_or("rock.obj",   lambda: make_rock(radius=0.35))
        self.mesh_ground, _ = _load_or("ground.obj", lambda: make_plane(size=30.0, subdivisions=8, uv_scale=8.0))
        self.mesh_quad = make_quad()

        print("Texturi:")
        self.tex_stem      = generate_solid_color(220, 200, 160)
        self.tex_cap       = generate_mushroom_cap_texture()
        self.tex_bark      = generate_bark_texture()
        self.tex_bark_nmap = generate_bark_normal_map()
        self.tex_white     = generate_solid_color(240, 235, 220)
        self.tex_center    = generate_solid_color(255, 210, 0)
        self.tex_grass     = generate_grass_texture()
        self.tex_ground    = generate_ground_texture()
        self.tex_rock      = generate_solid_color(130, 120, 110)
        self.tex_flat_nmap = _generate_flat_normal(64, 64)
        self.tex_green     = [generate_solid_color(45,130,35), generate_solid_color(30,110,25), generate_solid_color(55,145,40)]
        self.tex_petals    = [generate_solid_color(230,80,120), generate_solid_color(255,150,50),
                              generate_solid_color(180,80,220), generate_solid_color(80,160,220)]

        self.tex_firefly_colors = [
            generate_solid_color(255, 220,  80),
            generate_solid_color( 80, 220, 255),
            generate_solid_color(200,  80, 255),
        ]
        self.mesh_firefly  = make_sphere(radius=0.07, stacks=6, slices=6)
        self._firefly_pos  = [(0.0, 1.2, 0.0)] * 3
        self._firefly_emit = [(1.0, 1.0, 0.6), (0.6, 1.0, 1.0), (1.0, 0.6, 1.0)]

        self._build_grass_instances(count=800, spread=12.0)

        print("\nScenă încărcată ✓")
        print("WASD=mișcare | mouse stânga=rotire | scroll=zoom | Q/E=sus/jos | F5=screenshot | ESC=ieșire\n")

    # ── Update ─────────────────────────────────────────────────────────────────

    def update(self, dt, window):
        self.time += dt
        self.camera.update(dt, window)

    # ── Render ─────────────────────────────────────────────────────────────────

    def render(self):
        glClearColor(0.0, 0.0, 0.0, 1.0)
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        view = self.camera.get_view()
        proj = self.camera.get_projection()

        # Cer procedurale (fullscreen quad, fără depth write)
        glDepthMask(GL_FALSE)
        self.sky_shader.use()
        self.sky_shader.set_float("time", self.time)
        self.mesh_quad.draw()
        glDepthMask(GL_TRUE)

        sh = self.shader
        sh.use()
        sh.set_mat4("view", view)
        sh.set_mat4("projection", proj)
        sh.set_vec3("viewPos", tuple(self.camera.position))
        sh.set_bool("useEmissive", False)
        self._set_lighting(sh)
        self._render_scene(sh)

        gs = self.grass_shader
        gs.use()
        gs.set_mat4("view", view)
        gs.set_mat4("projection", proj)
        gs.set_vec3("viewPos", tuple(self.camera.position))
        gs.set_bool("useEmissive", False)
        self._set_lighting(gs)
        gs.set_float("time", self.time)
        self._render_grass(gs)

    # ── Lighting ───────────────────────────────────────────────────────────────

    def _set_lighting(self, sh):
        t = (math.sin(self.time * 0.05) + 1) * 0.5   # 0=noapte, 1=zi
        sun_angle = self.time * 0.02

        # Ambient dinamic zi/noapte
        amb = 0.15 + t * 0.25
        sh.set_vec3("ambient", (amb * 0.95, amb * 0.90, amb + 0.05))

        # Soare cu intensitate dinamică
        sh.set_vec3("dirLight.direction",  (math.cos(sun_angle), -0.8, math.sin(sun_angle)))
        sh.set_vec3("dirLight.color",      (1.0, 0.95, 0.85))
        sh.set_float("dirLight.intensity", 0.4 + t * 0.7)

        # 3 licurici animați în jurul scenei
        a = self.time
        self._firefly_pos = [
            ( math.cos(a * 0.7) * 3.0,        1.2 + math.sin(a * 0.9) * 0.3,  math.sin(a * 0.7) * 3.0),
            ( math.cos(a * 0.5 + 2.1) * 4.0,  0.8 + math.sin(a * 1.1) * 0.4,  math.sin(a * 0.5 + 2.1) * 2.5),
            ( math.cos(a * 0.9 + 4.2) * 2.5,  1.5 + math.sin(a * 0.7) * 0.5,  math.sin(a * 0.9 + 4.2) * 4.0),
        ]
        self._firefly_emit = [(1.0, 1.0, 0.6), (0.6, 1.0, 1.0), (1.0, 0.6, 1.0)]
        firefly_colors_gl  = [(1.0, 0.85, 0.3), (0.3, 0.85, 1.0), (0.85, 0.3, 1.0)]

        for i, (pos, col) in enumerate(zip(self._firefly_pos, firefly_colors_gl)):
            sh.set_vec3(f"pointLights[{i}].position",   pos)
            sh.set_vec3(f"pointLights[{i}].color",      col)
            sh.set_float(f"pointLights[{i}].intensity", 2.5)
            sh.set_float(f"pointLights[{i}].linear",    0.30)
            sh.set_float(f"pointLights[{i}].quadratic", 0.44)

        # Fog dinamic zi/noapte
        sh.set_vec3("fogColor",  (0.30 + t*0.25, 0.32 + t*0.32, 0.45 + t*0.35))
        sh.set_float("fogNear",  18.0)
        sh.set_float("fogFar",   60.0)

    # ── Draw helper ────────────────────────────────────────────────────────────

    def _draw(self, sh, mesh, model, tex_diffuse, tex_normal=None, shininess=32.0):
        if mesh is None:
            return
        sh.set_mat4("model", model)
        nm = glm.transpose(glm.inverse(glm.mat3(model)))
        glUniformMatrix3fv(glGetUniformLocation(sh.program, "normalMatrix"),
                           1, GL_FALSE, glm.value_ptr(nm))
        sh.set_float("shininess", shininess)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, tex_diffuse)
        sh.set_int("texDiffuse", 0)
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, tex_normal if tex_normal else self.tex_flat_nmap)
        sh.set_int("texNormal", 1)
        sh.set_bool("useNormalMap", tex_normal is not None)
        mesh.draw()

    # ── Scene objects ──────────────────────────────────────────────────────────

    def _render_scene(self, sh):
        # Teren
        self._draw(sh, self.mesh_ground, glm.mat4(1.0), self.tex_ground, shininess=8.0)

        # Ciuperci
        positions_sc = [(0,0,0,1.0),(2.5,0,1.0,0.7),(-1.8,0,2.2,1.3),
                        (1.2,0,3.5,0.85),(-3.0,0,-1.0,1.1),(3.8,0,-2.0,0.6)]
        for px,py,pz,sc in positions_sc:
            m = glm.scale(glm.translate(glm.mat4(1.0), glm.vec3(px,py,pz)), glm.vec3(sc))
            if self._mush_meshes:
                self._draw(sh, self._mush_meshes.get('MushroomStem.007'), m, self.tex_stem)
                self._draw(sh, self._mush_meshes.get('MushroomCap.007'),  m, self.tex_cap)
                for idx in range(35, 40):
                    self._draw(sh, self._mush_meshes.get(f'MushroomSpot.0{idx}'), m, self.tex_white)
            else:
                self._draw(sh, self.mesh_mushroom, m, self.tex_stem)
                cap_m = glm.scale(glm.translate(m, glm.vec3(0, 1.26, 0)), glm.vec3(1.0, 0.65, 1.0))
                self._draw(sh, self.mesh_mushroom_cap, cap_m, self.tex_cap)
                for sx,sz in [(0.2,0.1),(-0.15,0.2),(0.05,-0.22)]:
                    self._draw(sh, self.mesh_mushroom_spot,
                               glm.scale(glm.translate(m, glm.vec3(sx,1.25,sz)), glm.vec3(0.5)),
                               self.tex_white)

        # Pomi
        trees = [(-6,0,-3,1.0),(6,0,-4,1.2),(-5,0,4,0.85),(7,0,3,1.1),
                 (0,0,-8,1.3),(-9,0,0,0.9),(9,0,1,1.0),(4,0,-9,0.75)]
        for px,py,pz,sc in trees:
            m = glm.scale(glm.translate(glm.mat4(1.0), glm.vec3(px,py,pz)), glm.vec3(sc))
            if self._tree_meshes:
                self._draw(sh, self._tree_meshes.get('TreeTrunk.007'), m, self.tex_bark, self.tex_bark_nmap, shininess=8.0)
                for j, ckey in enumerate(['TreeCrown0.007', 'TreeCrown1.007', 'TreeCrown2.007']):
                    self._draw(sh, self._tree_meshes.get(ckey), m, self.tex_green[j], shininess=12.0)
            else:
                self._draw(sh, self.mesh_tree, m, self.tex_bark, self.tex_bark_nmap, shininess=8.0)
                for j,(cx,cy,cz) in enumerate([(0,0,0),(0.2,0,0.1),(-0.15,0,0.2)]):
                    cms = 1.0 - j*0.15
                    cm = glm.scale(glm.translate(m, glm.vec3(cx, 2.8+j*0.3, cz)),
                                   glm.vec3(cms, cms*0.85, cms))
                    self._draw(sh, self.mesh_crown, cm, self.tex_green[j % 3], shininess=12.0)

        # Flori
        flowers = [(1.5,0,1.5),(-1.0,0,3.0),(3.0,0,0.5),(-2.5,0,-0.5),
                   (0.5,0,-2.0),(2.0,0,-3.5),(-3.5,0,2.5),(4.5,0,2.0)]
        for i,(px,py,pz) in enumerate(flowers):
            m = glm.translate(glm.mat4(1.0), glm.vec3(px,py,pz))
            if self._flower_meshes:
                self._draw(sh, self._flower_meshes.get('FlowerStem.007'), m, self.tex_green[0])
                self._draw(sh, self._flower_meshes.get('FlowerLeaf.007'), m, self.tex_green[1])
                self._draw(sh, self._flower_meshes.get('FlowerCenter.007'), m, self.tex_center, shininess=64.0)
                petal_tex = self.tex_petals[i % 4]
                for j in range(6):
                    self._draw(sh, self._flower_meshes.get(f'Petal{j}.007'), m, petal_tex)
            else:
                self._draw(sh, self.mesh_flower, m, generate_solid_color(50,150,40))
                for j in range(6):
                    a = j * math.pi / 3
                    pm = glm.scale(glm.translate(m, glm.vec3(math.cos(a)*0.22, 0.85, math.sin(a)*0.22)),
                                   glm.vec3(1.0, 0.3, 1.0))
                    self._draw(sh, self.mesh_petal, pm, self.tex_petals[(i+j)%4])
                self._draw(sh, self.mesh_center,
                           glm.translate(m, glm.vec3(0,0.85,0)), self.tex_center, shininess=64.0)

        # Pietre
        for i,(px,pz) in enumerate([(-4,1),(3,3.5),(-2,-4),(5,-1)]):
            sc = 0.8 + (i%3)*0.3
            m = glm.scale(glm.rotate(glm.translate(glm.mat4(1.0), glm.vec3(px,0,pz)),
                                     float(i), glm.vec3(0,1,0)), glm.vec3(sc, sc*0.6, sc))
            self._draw(sh, self.mesh_rock, m, self.tex_rock)

        # Licurici (sfere emissive)
        for i, pos in enumerate(self._firefly_pos):
            m = glm.translate(glm.mat4(1.0), glm.vec3(*pos))
            sh.set_bool("useEmissive", True)
            sh.set_vec3("emissiveColor", self._firefly_emit[i])
            self._draw(sh, self.mesh_firefly, m, self.tex_firefly_colors[i])
        sh.set_bool("useEmissive", False)

    def _render_grass(self, gs):
        gs.set_mat4("model", glm.mat4(1.0))
        glUniformMatrix3fv(glGetUniformLocation(gs.program, "normalMatrix"),
                           1, GL_FALSE, glm.value_ptr(glm.mat3(1.0)))
        gs.set_float("shininess", 8.0)
        gs.set_bool("useNormalMap", False)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, self.tex_grass)
        gs.set_int("texDiffuse", 0)
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, self.tex_flat_nmap)
        gs.set_int("texNormal", 1)
        self.mesh_grass_blade.draw_instanced(self._grass_count)

    # ── Grass instances ────────────────────────────────────────────────────────

    def _build_grass_instances(self, count=800, spread=12.0):
        self._grass_count = count
        rng = np.random.default_rng(55)
        positions = rng.uniform(-spread, spread, (count, 3)).astype(np.float32)
        positions[:, 1] = 0.0
        rotations = rng.uniform(0, math.pi*2, count).astype(np.float32)
        scales    = rng.uniform(0.7, 1.4, count).astype(np.float32)

        glBindVertexArray(self.mesh_grass_blade.vao)
        for loc, data, size in [(4, positions, 3), (5, rotations, 1), (6, scales, 1)]:
            buf = glGenBuffers(1)
            glBindBuffer(GL_ARRAY_BUFFER, buf)
            glBufferData(GL_ARRAY_BUFFER, data.nbytes, data, GL_STATIC_DRAW)
            glEnableVertexAttribArray(loc)
            glVertexAttribPointer(loc, size, GL_FLOAT, GL_FALSE, 0, None)
            glVertexAttribDivisor(loc, 1)
        glBindVertexArray(0)


from src.texture import generate_solid_color
