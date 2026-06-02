from pathlib import Path

import numpy as np
from OpenGL.GL import *
from PIL import Image


def load_texture(path, wrap=GL_REPEAT, flip=True):
    """Încarcă o textură color de pe disc. Returnează texture ID."""
    path = Path(path)
    if not path.exists():
        print(f"[WARN] Textură lipsă: {path} — se generează procedural")
        return _generate_checker(64, 64)

    img = Image.open(path).convert("RGBA")
    if flip:
        img = img.transpose(Image.FLIP_TOP_BOTTOM)
    data = np.array(img, dtype=np.uint8)

    tex_id = glGenTextures(1)
    glBindTexture(GL_TEXTURE_2D, tex_id)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.width, img.height,
                 0, GL_RGBA, GL_UNSIGNED_BYTE, data)
    glGenerateMipmap(GL_TEXTURE_2D)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

    glBindTexture(GL_TEXTURE_2D, 0)
    return tex_id


def load_normal_map(path, flip=True):
    """Încarcă o normal map. Dacă nu există, generează una neutră (0.5,0.5,1.0)."""
    path = Path(path)
    if not path.exists():
        print(f"[WARN] Normal map lipsă: {path} — se generează neutră")
        return _generate_flat_normal(64, 64)
    return load_texture(path, flip=flip)


# ── Generatoare procedurale de texturi ───────────────────────────────────────

def _generate_checker(w, h, color_a=(80,160,60,255), color_b=(60,130,45,255)):
    """Generează o textură checker (pentru iarbă/teren fără fișier)."""
    data = np.zeros((h, w, 4), dtype=np.uint8)
    for y in range(h):
        for x in range(w):
            if (x // 8 + y // 8) % 2 == 0:
                data[y, x] = color_a
            else:
                data[y, x] = color_b
    return _upload_texture(data, w, h)


def _generate_flat_normal(w, h):
    """Normal map neutră: (128, 128, 255) = normal pointing up (0,0,1)."""
    data = np.full((h, w, 4), [128, 128, 255, 255], dtype=np.uint8)
    return _upload_texture(data, w, h)


def generate_solid_color(r, g, b, a=255, size=4):
    """Generează o textură 4x4 de o culoare solidă."""
    data = np.full((size, size, 4), [r, g, b, a], dtype=np.uint8)
    return _upload_texture(data, size, size)


def generate_grass_texture():
    """Textură procedurală pentru iarbă cu variație de verde."""
    w, h = 128, 128
    data = np.zeros((h, w, 4), dtype=np.uint8)
    rng = np.random.default_rng(42)
    for y in range(h):
        for x in range(w):
            v = int(rng.integers(0, 30))
            data[y, x] = [40 + v, 120 + v, 30 + v//2, 255]
    return _upload_texture(data, w, h)


def generate_mushroom_cap_texture():
    """Textură roșie cu spoturi albe pentru pălăria ciupercii."""
    w, h = 128, 128
    data = np.full((h, w, 4), [200, 40, 20, 255], dtype=np.uint8)
    rng = np.random.default_rng(7)
    for _ in range(8):
        cx = int(rng.integers(10, w-10))
        cy = int(rng.integers(10, h-10))
        r  = int(rng.integers(6, 14))
        for y in range(max(0, cy-r), min(h, cy+r)):
            for x in range(max(0, cx-r), min(w, cx+r)):
                if (x-cx)**2 + (y-cy)**2 < r*r:
                    data[y, x] = [240, 235, 220, 255]
    return _upload_texture(data, w, h)


def generate_bark_texture():
    """Textură de scoarță de copac (linii verticale maro)."""
    w, h = 64, 128
    data = np.zeros((h, w, 4), dtype=np.uint8)
    rng = np.random.default_rng(13)
    for x in range(w):
        base = int(rng.integers(0, 20))
        for y in range(h):
            v = int(rng.integers(0, 15))
            data[y, x] = [90 + base + v, 55 + base//2 + v//2, 25 + v//3, 255]
    return _upload_texture(data, w, h)


def generate_bark_normal_map():
    """Normal map pentru scoarță — linii verticale simulate."""
    w, h = 64, 128
    data = np.zeros((h, w, 4), dtype=np.uint8)
    rng = np.random.default_rng(13)
    for x in range(w):
        nx = int(rng.integers(100, 156))
        for y in range(h):
            data[y, x] = [nx, 128, 255, 255]
    return _upload_texture(data, w, h)


def generate_flower_texture():
    """Textură gradient pentru petale."""
    w, h = 64, 64
    data = np.zeros((h, w, 4), dtype=np.uint8)
    for y in range(h):
        t = y / h
        r = int(230 - t * 50)
        g = int(80 + t * 40)
        b = int(120 + t * 60)
        for x in range(w):
            data[y, x] = [r, g, b, 255]
    return _upload_texture(data, w, h)


def generate_ground_texture():
    """Textură sol cu nuanțe de verde-maro."""
    w, h = 256, 256
    data = np.zeros((h, w, 4), dtype=np.uint8)
    rng = np.random.default_rng(99)
    noise = rng.integers(0, 25, (h, w))
    for y in range(h):
        for x in range(w):
            v = int(noise[y, x])
            data[y, x] = [50 + v, 100 + v, 35 + v//2, 255]
    return _upload_texture(data, w, h)


def generate_sky_texture():
    """Gradient cer pentru skybox simplu."""
    w, h = 1, 256
    data = np.zeros((h, w, 4), dtype=np.uint8)
    for y in range(h):
        t = y / h
        r = int(30 + t * 100)
        g = int(60 + t * 120)
        b = int(120 + t * 100)
        data[y, 0] = [r, g, b, 255]
    return _upload_texture(data, w, h)


def _upload_texture(data, w, h):
    tex_id = glGenTextures(1)
    glBindTexture(GL_TEXTURE_2D, tex_id)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h,
                 0, GL_RGBA, GL_UNSIGNED_BYTE, data)
    glGenerateMipmap(GL_TEXTURE_2D)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
    glBindTexture(GL_TEXTURE_2D, 0)
    return tex_id
