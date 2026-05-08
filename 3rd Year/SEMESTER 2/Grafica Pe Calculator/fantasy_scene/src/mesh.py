"""src/mesh.py — VAO/VBO/EBO wrapper + geometrii procedurale"""

from OpenGL.GL import *
import numpy as np
import math


class Mesh:
    """
    Mesh generic. Date per vertex: position(3) + normal(3) + uv(2) + tangent(3) = 11 floats
    """
    def __init__(self, vertices, indices=None):
        """
        vertices: np.array float32, shape (N, 11)
        indices:  np.array uint32  sau None
        """
        self.vertex_count = len(vertices)
        self.index_count  = len(indices) if indices is not None else 0
        self.has_indices  = indices is not None

        self.vao = glGenVertexArrays(1)
        self.vbo = glGenBuffers(1)
        self.ebo = glGenBuffers(1) if self.has_indices else None

        glBindVertexArray(self.vao)

        # VBO
        flat = vertices.astype(np.float32).flatten()
        glBindBuffer(GL_ARRAY_BUFFER, self.vbo)
        glBufferData(GL_ARRAY_BUFFER, flat.nbytes, flat, GL_STATIC_DRAW)

        stride = 11 * 4  # 11 floats × 4 bytes

        # position (loc 0)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, stride, ctypes.c_void_p(0))
        # normal (loc 1)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, stride, ctypes.c_void_p(12))
        # uv (loc 2)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, stride, ctypes.c_void_p(24))
        # tangent (loc 3)
        glEnableVertexAttribArray(3)
        glVertexAttribPointer(3, 3, GL_FLOAT, GL_FALSE, stride, ctypes.c_void_p(32))

        # EBO
        if self.has_indices:
            idx = indices.astype(np.uint32)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, self.ebo)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, idx.nbytes, idx, GL_STATIC_DRAW)

        glBindVertexArray(0)

    def draw(self):
        glBindVertexArray(self.vao)
        if self.has_indices:
            glDrawElements(GL_TRIANGLES, self.index_count, GL_UNSIGNED_INT, None)
        else:
            glDrawArrays(GL_TRIANGLES, 0, self.vertex_count)
        glBindVertexArray(0)

    def draw_instanced(self, count):
        glBindVertexArray(self.vao)
        if self.has_indices:
            glDrawElementsInstanced(GL_TRIANGLES, self.index_count, GL_UNSIGNED_INT, None, count)
        else:
            glDrawArraysInstanced(GL_TRIANGLES, 0, self.vertex_count, count)
        glBindVertexArray(0)

    def __del__(self):
        try:
            glDeleteVertexArrays(1, [self.vao])
            glDeleteBuffers(1, [self.vbo])
            if self.ebo:
                glDeleteBuffers(1, [self.ebo])
        except Exception:
            pass


import ctypes

# ─────────────────────────────────────────────────────────────────────────────
# GEOMETRII PROCEDURALE
# Fiecare funcție returnează un Mesh gata de folosit.
# ─────────────────────────────────────────────────────────────────────────────

def _v(pos, normal, uv, tangent=(1,0,0)):
    return list(pos) + list(normal) + list(uv) + list(tangent)


def _compute_tangents(verts, indices):
    """Calculează tangentele per triunghi pentru normal mapping."""
    verts = list(verts)
    for i in range(0, len(indices), 3):
        i0, i1, i2 = indices[i], indices[i+1], indices[i+2]
        v0, v1, v2 = verts[i0], verts[i1], verts[i2]
        e1 = [v1[j] - v0[j] for j in range(3)]
        e2 = [v2[j] - v0[j] for j in range(3)]
        du1 = v1[6] - v0[6]; dv1 = v1[7] - v0[7]
        du2 = v2[6] - v0[6]; dv2 = v2[7] - v0[7]
        denom = du1*dv2 - du2*dv1
        if abs(denom) < 1e-8:
            t = (1,0,0)
        else:
            f = 1.0 / denom
            tx = f * (dv2*e1[0] - dv1*e2[0])
            ty = f * (dv2*e1[1] - dv1*e2[1])
            tz = f * (dv2*e1[2] - dv1*e2[2])
            ln = math.sqrt(tx*tx + ty*ty + tz*tz) or 1.0
            t = (tx/ln, ty/ln, tz/ln)
        for idx in (i0, i1, i2):
            verts[idx] = verts[idx][:8] + list(t)
    return verts


# ── Cilindru ──────────────────────────────────────────────────────────────────
def make_cylinder(radius=0.2, height=1.0, segments=16, top_radius=None):
    if top_radius is None:
        top_radius = radius
    verts = []
    idxs  = []

    # Lateral
    for i in range(segments):
        a0 = 2*math.pi * i     / segments
        a1 = 2*math.pi * (i+1) / segments
        for a, r, y, v in [(a0,radius,0,0),(a1,radius,0,0),
                            (a1,top_radius,height,1),(a0,top_radius,height,1)]:
            nx = math.cos(a); nz = math.sin(a)
            verts.append(_v((r*nx,y,r*nz),(nx,0,nz),(a/(2*math.pi),v)))

        base = i * 4
        idxs += [base,base+1,base+2, base,base+2,base+3]

    # Capace
    for y_cap, sign in [(0,-1),(height,1)]:
        r_cap = radius if sign < 0 else top_radius
        center_idx = len(verts)
        verts.append(_v((0,y_cap,0),(0,sign,0),(0.5,0.5)))
        for i in range(segments):
            a = 2*math.pi * i / segments
            verts.append(_v((r_cap*math.cos(a),y_cap,r_cap*math.sin(a)),(0,sign,0),
                            (0.5+0.5*math.cos(a),0.5+0.5*math.sin(a))))
        for i in range(segments):
            a = center_idx + 1 + i
            b = center_idx + 1 + (i+1) % segments
            if sign > 0:
                idxs += [center_idx, a, b]
            else:
                idxs += [center_idx, b, a]

    idxs_arr = np.array(idxs, dtype=np.uint32)
    verts = _compute_tangents(verts, idxs)
    return Mesh(np.array(verts, dtype=np.float32), idxs_arr)


# ── Sferă ─────────────────────────────────────────────────────────────────────
def make_sphere(radius=1.0, stacks=16, slices=16):
    verts = []
    idxs  = []
    for i in range(stacks+1):
        phi = math.pi/2 - math.pi*i/stacks
        y   = radius * math.sin(phi)
        r   = radius * math.cos(phi)
        for j in range(slices+1):
            theta = 2*math.pi*j/slices
            x = r * math.cos(theta)
            z = r * math.sin(theta)
            nx,ny,nz = x/radius, y/radius, z/radius
            u = j/slices; v = i/stacks
            verts.append(_v((x,y,z),(nx,ny,nz),(u,v)))

    for i in range(stacks):
        for j in range(slices):
            a = i*(slices+1)+j
            b = a+slices+1
            idxs += [a,b,a+1, b,b+1,a+1]

    idxs_arr = np.array(idxs, dtype=np.uint32)
    verts = _compute_tangents(verts, idxs)
    return Mesh(np.array(verts, dtype=np.float32), idxs_arr)


# ── Plan ──────────────────────────────────────────────────────────────────────
def make_plane(size=1.0, subdivisions=1, uv_scale=1.0):
    verts = []
    idxs  = []
    n = subdivisions
    step = size / n
    for i in range(n+1):
        for j in range(n+1):
            x = -size/2 + j*step
            z = -size/2 + i*step
            u = (j/n) * uv_scale
            v = (i/n) * uv_scale
            verts.append(_v((x,0,z),(0,1,0),(u,v),(1,0,0)))
    for i in range(n):
        for j in range(n):
            a = i*(n+1)+j
            idxs += [a, a+n+1, a+1, a+1, a+n+1, a+n+2]

    idxs_arr = np.array(idxs, dtype=np.uint32)
    return Mesh(np.array(verts, dtype=np.float32), idxs_arr)


# ── Quad (pentru skybox background) ──────────────────────────────────────────
def make_quad():
    verts = np.array([
        _v((-1,-1,0),(0,0,1),(0,0)),
        _v(( 1,-1,0),(0,0,1),(1,0)),
        _v(( 1, 1,0),(0,0,1),(1,1)),
        _v((-1,-1,0),(0,0,1),(0,0)),
        _v(( 1, 1,0),(0,0,1),(1,1)),
        _v((-1, 1,0),(0,0,1),(0,1)),
    ], dtype=np.float32)
    return Mesh(verts)


# ── Grass blade (fir de iarbă curbat) ────────────────────────────────────────
def make_grass_blade(height=0.8, lean=0.15):
    segs = 5
    verts = []
    idxs  = []
    for i in range(segs+1):
        t    = i / segs
        x    = lean * t * t
        y    = height * t
        w    = 0.04 * (1 - t)
        nx   = 0.0; ny = 1.0; nz = 0.0
        verts.append(_v((x-w, y, 0), (nx,ny,nz), (0, t)))
        verts.append(_v((x+w, y, 0), (nx,ny,nz), (1, t)))

    for i in range(segs):
        b = i*2
        idxs += [b, b+1, b+2, b+1, b+3, b+2]

    idxs_arr = np.array(idxs, dtype=np.uint32)
    return Mesh(np.array(verts, dtype=np.float32), idxs_arr)


# ── Rock (icosphere deformată) ────────────────────────────────────────────────
def make_rock(radius=0.4, seed=42):
    rng = np.random.default_rng(seed)
    base = make_sphere(radius, stacks=8, slices=8)
    # Deformează poziția fiecărui vertex cu noise
    data = base.vao  # nu putem modifica după upload, deci reconstruim
    stacks, slices = 8, 8
    verts = []
    idxs  = []
    for i in range(stacks+1):
        phi = math.pi/2 - math.pi*i/stacks
        y0  = radius * math.sin(phi)
        r0  = radius * math.cos(phi)
        for j in range(slices+1):
            theta = 2*math.pi*j/slices
            noise = 1.0 + rng.uniform(-0.25, 0.25)
            x = r0 * math.cos(theta) * noise
            z = r0 * math.sin(theta) * noise
            y = y0 * noise
            ln = math.sqrt(x*x+y*y+z*z) or 1
            verts.append(_v((x,y,z),(x/ln,y/ln,z/ln),(j/slices, i/stacks)))
    for i in range(stacks):
        for j in range(slices):
            a = i*(slices+1)+j; b = a+slices+1
            idxs += [a,b,a+1, b,b+1,a+1]
    idxs_arr = np.array(idxs, dtype=np.uint32)
    verts = _compute_tangents(verts, idxs)
    return Mesh(np.array(verts, dtype=np.float32), idxs_arr)
