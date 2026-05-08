"""
Fantasy Scene — Main Entry Point
=================================
Rulează: python main.py
Controale:
  WASD        — mișcare cameră
  Mouse       — rotire cameră (click stânga + drag)
  Scroll      — zoom
  F5          — screenshot
  ESC         — ieșire
"""

import glfw
from src.window import Window
from src.scene import Scene


def main():
    window = Window(width=1280, height=720, title="Fantasy Scene — OpenGL")
    scene = Scene(window)
    scene.load()
    window.run(scene)


if __name__ == "__main__":
    main()
