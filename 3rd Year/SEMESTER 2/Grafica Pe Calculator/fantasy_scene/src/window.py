import time
from pathlib import Path

import glfw
from OpenGL.GL import *


class Window:
    def __init__(self, width=1280, height=720, title="Fantasy Scene"):
        self.width = width
        self.height = height
        self.title = title
        self._scene = None

        if not glfw.init():
            raise RuntimeError("GLFW init failed")

        # OpenGL 3.3 Core Profile
        glfw.window_hint(glfw.CONTEXT_VERSION_MAJOR, 3)
        glfw.window_hint(glfw.CONTEXT_VERSION_MINOR, 3)
        glfw.window_hint(glfw.OPENGL_PROFILE, glfw.OPENGL_CORE_PROFILE)
        glfw.window_hint(glfw.OPENGL_FORWARD_COMPAT, GL_TRUE)
        glfw.window_hint(glfw.SAMPLES, 4)  # MSAA

        self.handle = glfw.create_window(width, height, title, None, None)
        if not self.handle:
            glfw.terminate()
            raise RuntimeError("Window creation failed")

        glfw.make_context_current(self.handle)
        glfw.swap_interval(1)  # vsync

        # Input state
        self._keys = {}
        self._mouse_last = None
        self._mouse_pressed = False
        self._scroll_delta = 0.0

        # Callbacks
        glfw.set_key_callback(self.handle, self._key_cb)
        glfw.set_cursor_pos_callback(self.handle, self._mouse_move_cb)
        glfw.set_mouse_button_callback(self.handle, self._mouse_btn_cb)
        glfw.set_scroll_callback(self.handle, self._scroll_cb)
        glfw.set_framebuffer_size_callback(self.handle, self._resize_cb)

        # OpenGL global state
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        print(f"OpenGL {glGetString(GL_VERSION).decode()}")
        print(f"Renderer: {glGetString(GL_RENDERER).decode()}")

    def run(self, scene):
        self._scene = scene
        last_time = glfw.get_time()

        while not glfw.window_should_close(self.handle):
            now = glfw.get_time()
            dt = now - last_time
            last_time = now

            glfw.poll_events()

            scene.update(dt, self)
            scene.render()

            glfw.swap_buffers(self.handle)
            self._scroll_delta = 0.0

        glfw.terminate()

    # ── Input helpers ──────────────────────────────────────

    def is_key_down(self, key):
        return self._keys.get(key, False)

    def get_scroll(self):
        return self._scroll_delta

    def get_mouse_delta(self):
        return getattr(self, '_mouse_delta', (0.0, 0.0))

    def is_mouse_pressed(self):
        return self._mouse_pressed

    # ── Callbacks ──────────────────────────────────────────

    def _key_cb(self, window, key, scancode, action, mods):
        if action == glfw.PRESS:
            self._keys[key] = True
        elif action == glfw.RELEASE:
            self._keys[key] = False

        if key == glfw.KEY_ESCAPE and action == glfw.PRESS:
            glfw.set_window_should_close(window, True)

        if key == glfw.KEY_F5 and action == glfw.PRESS:
            self._screenshot()

    def _mouse_move_cb(self, window, xpos, ypos):
        if self._mouse_last is None:
            self._mouse_last = (xpos, ypos)
        dx = xpos - self._mouse_last[0]
        dy = ypos - self._mouse_last[1]
        self._mouse_delta = (dx, dy)
        self._mouse_last = (xpos, ypos)

    def _mouse_btn_cb(self, window, button, action, mods):
        if button == glfw.MOUSE_BUTTON_LEFT:
            self._mouse_pressed = (action == glfw.PRESS)
            if action == glfw.PRESS:
                x, y = glfw.get_cursor_pos(window)
                self._mouse_last = (x, y)
            self._mouse_delta = (0.0, 0.0)

    def _scroll_cb(self, window, xoffset, yoffset):
        self._scroll_delta = yoffset

    def _resize_cb(self, window, width, height):
        self.width = width
        self.height = height
        glViewport(0, 0, width, height)
        if self._scene:
            self._scene.camera.aspect = width / max(height, 1)

    def _screenshot(self):
        from PIL import Image
        glPixelStorei(GL_PACK_ALIGNMENT, 1)
        data = glReadPixels(0, 0, self.width, self.height, GL_RGB, GL_UNSIGNED_BYTE)
        img = Image.frombytes("RGB", (self.width, self.height), data)
        img = img.transpose(Image.FLIP_TOP_BOTTOM)
        path = Path("screenshots") / f"screenshot_{int(time.time())}.png"
        path.parent.mkdir(exist_ok=True)
        img.save(path)
        print(f"Screenshot salvat: {path}")
