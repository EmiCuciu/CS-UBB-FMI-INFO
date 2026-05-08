import math

import glfw
from pyglm import glm


class Camera:
    def __init__(self, position=(0, 3, 10), yaw=-90.0, pitch=-15.0):
        self.position = glm.vec3(*position)
        self.yaw = yaw
        self.pitch = pitch
        self.speed = 8.0
        self.sensitivity = 0.15
        self.fov = 45.0
        self.aspect = 1280 / 720
        self.near = 0.1
        self.far = 200.0
        self._update_vectors()

    def update(self, dt, window):
        # Mișcare WASD
        vel = self.speed * dt
        if window.is_key_down(glfw.KEY_W):
            self.position += self.front * vel
        if window.is_key_down(glfw.KEY_S):
            self.position -= self.front * vel
        if window.is_key_down(glfw.KEY_A):
            self.position -= self.right * vel
        if window.is_key_down(glfw.KEY_D):
            self.position += self.right * vel
        if window.is_key_down(glfw.KEY_E):
            self.position += self.world_up * vel
        if window.is_key_down(glfw.KEY_Q):
            self.position -= self.world_up * vel

        # Mouse look (doar când butonul stâng e apăsat)
        if window.is_mouse_pressed():
            dx, dy = window.get_mouse_delta()
            self.yaw   += dx * self.sensitivity
            self.pitch -= dy * self.sensitivity
            self.pitch = max(-89.0, min(89.0, self.pitch))
            self._update_vectors()

        # Scroll zoom
        scroll = window.get_scroll()
        if scroll != 0:
            self.fov -= scroll * 2.0
            self.fov = max(10.0, min(90.0, self.fov))

        # Reset mouse delta după ce l-am consumat
        window._mouse_delta = (0.0, 0.0)

    def get_view(self):
        return glm.lookAt(self.position, self.position + self.front, self.up)

    def get_projection(self):
        return glm.perspective(glm.radians(self.fov), self.aspect, self.near, self.far)

    def _update_vectors(self):
        yaw_r   = math.radians(self.yaw)
        pitch_r = math.radians(self.pitch)
        fx = math.cos(pitch_r) * math.cos(yaw_r)
        fy = math.sin(pitch_r)
        fz = math.cos(pitch_r) * math.sin(yaw_r)
        self.front     = glm.normalize(glm.vec3(fx, fy, fz))
        self.world_up  = glm.vec3(0, 1, 0)
        self.right     = glm.normalize(glm.cross(self.front, self.world_up))
        self.up        = glm.normalize(glm.cross(self.right, self.front))
