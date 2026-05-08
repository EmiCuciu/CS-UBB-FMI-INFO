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
Toate geometriile sunt generate **procedural în cod** (src/mesh.py):
- Cilindri, sfere, plane — algoritmi matematici proprii
- Nu s-au folosit modele .obj din surse externe

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

## Cod propriu
Toată implementarea (shaderele GLSL, geometriile procedurale, sistemul de iluminare,
camera, instanced rendering pentru iarbă, animația vântului) a fost scrisă de student.
