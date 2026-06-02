# Surse și resurse externe

## Biblioteci Python utilizate
| Bibliotecă | Versiune | Licență | Utilizare |
|---|---|---|---|
| PyOpenGL | 3.1.7 | BSD | Wrapper OpenGL pentru Python |
| glfw | 2.6.5 | zlib/libpng | Window management + input |
| PyGLM | 2.7.1 | MIT | Matematică 3D (matrice, vectori) |
| Pillow | 10.3.0 | HPND | Încărcare și procesare imagini |
| numpy | 1.26.4 | BSD | Buffere de date numerice |

## Modele 3D
Fișierele `.obj` din `assets/models/` au fost generate cu **scripturi Python în Blender**
(Blender Python API — `bpy`):
- `mushroom.obj` — ciupercă (picior cilindric + pălărie sferică cu spoturi)
- `tree.obj` — copac (trunchi conic + coroană sferică stratificată)
- `flower.obj` — floare (tijă + petale radiale)
- `grass_blade.obj` — fir de iarbă curbat
- `rock.obj` — piatră neregulată
- `ground.obj` — teren plat

Geometriile procedurale de rezervă (fallback) sunt implementate în `src/mesh.py`.
Nu s-au utilizat modele descărcate din surse externe.

## Texturi
Toate texturile sunt **generate procedural în cod** (src/texture.py):
- Nu s-au folosit imagini din surse externe
- Normal map generată procedural pentru scoarța copacilor

## Referințe tehnice
- [LearnOpenGL](https://learnopengl.com) — tutoriale OpenGL (Joey de Vries, CC BY-NC 4.0)
  - Normal Mapping: https://learnopengl.com/Advanced-Lighting/Normal-Mapping
  - Lighting: https://learnopengl.com/Lighting/Basic-Lighting
- [docs.gl](https://docs.gl) — referință funcții OpenGL
- OpenGL 3.3 Core Profile Specification
