# Laborator - Clădire 3D încărcată din OBJ, cu navigare prin tastatură și mouse

## Cerință
Să se realizeze o aplicație în Python, folosind Modern OpenGL, care:
- încarcă o clădire dintr-un fișier `OBJ`;
- nu folosește culori din model și nici texturi;
- afișează clădirea într-o scenă 3D;
- permite deplasarea în jurul clădirii cu tastatura și mouse-ul.

## Ce conține rezolvarea
- `main.py` - aplicația completă;
- `cladire.obj` - modelul 3D al clădirii;
- `requirements.txt` - bibliotecile necesare.

## Instalare
```bash
pip install -r requirements.txt
```

## Rulare
```bash
python main.py
```

## Controale
- `W` / `S` - înainte / înapoi
- `A` / `D` - stânga / dreapta
- `Q` / `E` - jos / sus
- mișcarea mouse-ului - rotirea camerei
- `F` - wireframe on/off
- `R` - reset cameră
- `M` - activează/dezactivează capturarea mouse-ului
- `ESC` - ieșire

## Explicație pe scurt
Aplicația:
1. citește geometria din `cladire.obj`;
2. extrage pozițiile și normalele;
3. trimite datele către GPU folosind `VBO` și `VAO`;
4. folosește un `vertex shader` și un `fragment shader`;
5. desenează clădirea cu o culoare neutră și iluminare simplă;
6. permite navigarea liberă în scenă cu o cameră de tip FPS/free camera.

## Observații
- Modelul este centrat și scalat automat, astfel încât să fie vizibil imediat după pornirea aplicației.
- Fișierul OBJ poate conține coordonate de textură și materiale, dar acestea sunt ignorate.
- Dacă modelul nu are normale pe unele fețe, acestea sunt calculate automat.
