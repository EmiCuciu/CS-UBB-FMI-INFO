# Fantasy Scene — OpenGL Project

## Context
Proiect universitar: scenă 3D în Python + Modern OpenGL (fără game engine).
Deadline: 24 Mai 2026.

## Cerinta proiect
Realizarea unei scene 3D inspirate din universul poveștilor, utilizând Modern OpenGL în Python.


Se cere dezvoltarea unei aplicații grafice 3D în Python, folosind paradigma Modern OpenGL, care să redea o scenă de poveste alcătuită din elemente naturale și decorative, precum: ciuperci, pomi, iarbă, flori, precum și alte obiecte compatibile estetic cu tema aleasă.

Scena trebuie să fie încărcată, compusă și randată în aplicație de către student, utilizând o arhitectură specifică aplicațiilor grafice moderne bazate pe shader-e. Modelele 3D pot fi realizate de student sau preluate din surse externe, cu condiția ca acestea să fie integrate corect în scenă și să fie menționate explicit sursele utilizate.

### Proiectul trebuie să evidențieze capacitatea studentului de a:

- organiza o scenă 3D coerentă vizual;
- integra și poziționa obiecte 3D într-un spațiu virtual;
- aplica texturi și efecte vizuale specifice randării moderne;
- utiliza modele de iluminare pentru creșterea realismului;
- implementa aplicația în Python + Modern OpenGL, nu în engine-uri grafice gata făcute.
Cerințe minime

### Proiectul trebuie să includă:
- o scenă 3D completă, cu tematică inspirată din povești sau fantasy;
- cel puțin câteva categorii de obiecte naturale/decorative, dintre care obligatoriu:
ciuperci,
pomi,
iarbă,
flori;
- încărcarea și afișarea corectă a modelelor 3D în aplicație;
- utilizarea de texturi colorate;
- utilizarea a cel puțin unei normal map texture pe unul sau mai multe obiecte;
- existența unui sistem minim de iluminare, incluzând cel puțin lumină ambientală, cu posibilitatea extinderii la alte tipuri de lumină;
- o prezentare vizuală îngrijită și coerentă a scenei.

### Condiții tehnice
Implementarea va fi realizată în Python.
Randarea va utiliza Modern OpenGL și shader-e dedicate.
Nu este permisă realizarea proiectului într-un game engine complet, precum Unity, Unreal Engine, Godot etc.
Modelele 3D pot proveni din biblioteci externe, platforme gratuite sau surse proprii, dar integrarea lor în scenă trebuie realizată de student.
Toate resursele externe utilizate trebuie documentate într-un fișier separat sau într-o secțiune din raport.

### Livrabile
Studentul va preda:

- codul sursă al proiectului;
- resursele necesare rulării sau instrucțiuni clare de rulare;
- 3-5 capturi de ecran relevante din aplicație.

#### Barem de evaluare
- 1p din oficiu
- 2p modelarea / compunerea obiectelor din scenă
- Se evaluează varietatea obiectelor, integrarea lor în decor și organizarea coerentă a scenei.
- 2p aplicarea de texturi colorate pe obiecte
- Se evaluează corectitudinea mapării și relevanța texturilor pentru obiectele alese.
- 1p aplicarea unei texturi de tip normal map
- Se evaluează utilizarea corectă a efectului pentru a simula detalii de suprafață.
- 1p existența unei lumini ambientale sau a altor modele de iluminare
- Se evaluează implementarea iluminării și contribuția acesteia la aspectul scenei.
- 1p aspect general
- Se evaluează coerența vizuală, calitatea prezentării și impresia artistică de ansamblu.
- 2p munca proprie a studentului
- Se evaluează gradul de implicare în integrarea scenei, configurarea aplicației, organizarea codului și personalizarea rezultatului final. Preluarea de modele este permisă, dar simpla afișare a unor obiecte fără contribuție evidentă la compoziția scenei nu va fi punctată maximal.
- Total: 10 puncte

#### Observații
- Sunt încurajate elemente suplimentare precum:
- animații simple;
- cameră liberă sau control interactiv;
- efecte suplimentare de lumină;
- atmosferă de poveste prin ceață, culori, fundal sau compoziție.
- Proiectele care demonstrează creativitate, coerență estetică și implementare tehnică solidă pot primi apreciere suplimentară în evaluarea calitativă.

## Stack tehnic
- Python 3.11+
- PyOpenGL 3.1.x — raw OpenGL calls
- glfw (PyGLFW) — window + input
- PyGLM — matematică 3D
- Pillow — texturi
- numpy — buffere

## Reguli OpenGL
- EXCLUSIV OpenGL 3.3 Core Profile (`#version 330 core`)
- NU funcții legacy (glBegin/glEnd etc.)
- Date geometry: position(3)+normal(3)+uv(2)+tangent(3) = 11 floats per vertex, stride=44 bytes
- Attribute locations: 0=pos, 1=normal, 2=uv, 3=tangent, 4-6=instanced
- Texturi: unit 0=diffuse, unit 1=normal map

## Sistem iluminare
- Lumină ambientală: uniform vec3 ambient
- Lumină direcțională (soare): DirLight struct
- Lumină punct: PointLight struct cu atenuare
- Normal mapping: TBN matrix în vertex shader
- Fog liniar în fragment shader
- Tone mapping Reinhard + gamma correction

## Animații
- Iarbă: animație vânt în grass.vert (sinusoidal pe Y)
- Soare: rotație lentă a direcției dirLight în scene.py

## Cerințe temă bifate
- [x] Scenă 3D completă fantasy
- [x] Ciuperci, pomi, iarbă, flori
- [x] Texturi colorate procedurale
- [x] Normal map pe trunchiuri de copac
- [x] Iluminare: ambient + directional + 3× point light (licurici)
- [x] Cameră liberă interactivă
- [x] Fog atmosferic dinamic zi/noapte
- [x] Animație vânt pe iarbă
- [x] Screenshot cu F5
- [x] Cer procedurale cu ciclu zi/noapte
- [x] Licurici animați (3 sfere emissive + point lights)

## Cum rulez
```bash
source .venv/bin/activate
python main.py
```
