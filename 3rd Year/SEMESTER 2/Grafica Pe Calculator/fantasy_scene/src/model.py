import math
from pathlib import Path

import numpy as np

from src.mesh import Mesh, _compute_tangents


def load_obj(path) -> list[Mesh]:
    """
    Încarcă un fișier .obj și returnează o listă de Mesh-uri (unul per grup/obiect).
    Dacă fișierul nu există, returnează listă goală și afișează warning.
    """
    path = Path(path)
    if not path.exists():
        print(f"[WARN] .obj lipsă: {path}")
        return []

    positions = []
    normals   = []
    uvs       = []
    groups    = {}          # nume_grup → listă de fețe
    cur_group = "default"

    with open(path, "r") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue

            parts = line.split()
            tag   = parts[0]

            if tag == "v":
                positions.append([float(x) for x in parts[1:4]])
            elif tag == "vn":
                normals.append([float(x) for x in parts[1:4]])
            elif tag == "vt":
                uvs.append([float(x) for x in parts[1:3]])
            elif tag in ("g", "o"):
                cur_group = parts[1] if len(parts) > 1 else "default"
            elif tag == "f":
                face = _parse_face(parts[1:])
                groups.setdefault(cur_group, []).append(face)

    meshes = []
    for group_name, faces in groups.items():
        mesh = _build_mesh(faces, positions, normals, uvs)
        if mesh is not None:
            meshes.append(mesh)

    if meshes:
        print(f"[OK] Încărcat: {path.name} — {len(meshes)} grup(uri), "
              f"{sum(1 for _ in meshes)} mesh(uri)")
    return meshes


def load_obj_by_material(path) -> dict:
    """
    Încarcă OBJ splitând după directive usemtl.
    Returnează dict: material_name → Mesh.
    """
    path = Path(path)
    if not path.exists():
        print(f"[WARN] .obj lipsă: {path}")
        return {}

    positions = []
    normals   = []
    uvs       = []
    groups    = {}
    cur_mat   = "default"

    with open(path, "r") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            parts = line.split()
            tag   = parts[0]
            if tag == "v":
                positions.append([float(x) for x in parts[1:4]])
            elif tag == "vn":
                normals.append([float(x) for x in parts[1:4]])
            elif tag == "vt":
                uvs.append([float(x) for x in parts[1:3]])
            elif tag == "usemtl":
                cur_mat = parts[1] if len(parts) > 1 else "default"
            elif tag == "f":
                groups.setdefault(cur_mat, []).append(_parse_face(parts[1:]))

    result = {}
    for mat_name, faces in groups.items():
        mesh = _build_mesh(faces, positions, normals, uvs)
        if mesh is not None:
            result[mat_name] = mesh

    if result:
        print(f"[OK] Încărcat: {path.name} — {len(result)} material(e): {', '.join(result)}")
    return result


def load_obj_single(path) -> "Mesh | None":
    """Returnează un singur Mesh (join toate grupurile din .obj)."""
    path = Path(path)
    if not path.exists():
        print(f"[WARN] .obj lipsă: {path}")
        return None

    positions = []
    normals   = []
    uvs       = []
    all_faces = []

    with open(path, "r") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            parts = line.split()
            tag   = parts[0]
            if tag == "v":
                positions.append([float(x) for x in parts[1:4]])
            elif tag == "vn":
                normals.append([float(x) for x in parts[1:4]])
            elif tag == "vt":
                uvs.append([float(x) for x in parts[1:3]])
            elif tag == "f":
                all_faces.append(_parse_face(parts[1:]))

    mesh = _build_mesh(all_faces, positions, normals, uvs)
    if mesh:
        print(f"[OK] Încărcat: {path.name} — {len(all_faces)} fețe")
    return mesh


# ── Internals ─────────────────────────────────────────────────────────────────

def _parse_face(tokens):
    """
    Parsează tokenele unei linii 'f'.
    Suportă: v, v/vt, v/vt/vn, v//vn
    Face-urile cu >3 vertecși sunt triangulate fan.
    """
    verts = []
    for tok in tokens:
        parts = tok.split("/")
        vi  = int(parts[0]) - 1 if parts[0] else 0
        vti = int(parts[1]) - 1 if len(parts) > 1 and parts[1] else -1
        vni = int(parts[2]) - 1 if len(parts) > 2 and parts[2] else -1
        verts.append((vi, vti, vni))

    # Triangulare fan pentru quads / n-gons
    triangles = []
    for i in range(1, len(verts) - 1):
        triangles.append((verts[0], verts[i], verts[i+1]))
    return triangles


def _build_mesh(faces, positions, normals, uvs):
    """Construiește un Mesh din liste de fețe + date geometrice."""
    verts_out = []
    idxs_out  = []
    cache     = {}   # (vi,vti,vni) → index în verts_out

    # Normală de fallback calculată din cross product
    def face_normal(tri):
        p0 = positions[tri[0][0]]
        p1 = positions[tri[1][0]]
        p2 = positions[tri[2][0]]
        e1 = [p1[i]-p0[i] for i in range(3)]
        e2 = [p2[i]-p0[i] for i in range(3)]
        nx = e1[1]*e2[2] - e1[2]*e2[1]
        ny = e1[2]*e2[0] - e1[0]*e2[2]
        nz = e1[0]*e2[1] - e1[1]*e2[0]
        ln = math.sqrt(nx*nx+ny*ny+nz*nz) or 1.0
        return [nx/ln, ny/ln, nz/ln]

    for face_tris in faces:
        for tri in face_tris:
            fn = face_normal(tri)
            tri_idxs = []
            for (vi, vti, vni) in tri:
                key = (vi, vti, vni)
                if key not in cache:
                    pos = positions[vi] if vi < len(positions) else [0,0,0]
                    uv  = uvs[vti]      if vti >= 0 and vti < len(uvs) else [0,0]
                    n   = normals[vni]  if vni >= 0 and vni < len(normals) else fn
                    # tangent placeholder — calculat mai jos
                    verts_out.append(list(pos) + list(n) + list(uv) + [1,0,0])
                    cache[key] = len(verts_out) - 1
                tri_idxs.append(cache[key])
            idxs_out.extend(tri_idxs)

    if not verts_out:
        return None

    idxs_arr = np.array(idxs_out, dtype=np.uint32)
    verts_out = _compute_tangents(verts_out, idxs_out)
    return Mesh(np.array(verts_out, dtype=np.float32), idxs_arr)
