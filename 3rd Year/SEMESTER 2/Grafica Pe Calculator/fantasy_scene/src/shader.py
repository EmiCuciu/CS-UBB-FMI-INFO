from pathlib import Path

from pyglm import glm
from OpenGL.GL import *


class Shader:
    def __init__(self, vert_path, frag_path):
        vert_src = Path(vert_path).read_text()
        frag_src = Path(frag_path).read_text()
        self.program = self._link(
            self._compile(vert_src, GL_VERTEX_SHADER, vert_path),
            self._compile(frag_src, GL_FRAGMENT_SHADER, frag_path),
        )

    # ── Public API ────────────────────────────────────────

    def use(self):
        glUseProgram(self.program)

    def set_int(self, name, value):
        glUniform1i(self._loc(name), int(value))

    def set_float(self, name, value):
        glUniform1f(self._loc(name), float(value))

    def set_vec3(self, name, value):
        glUniform3f(self._loc(name), *value)

    def set_vec4(self, name, value):
        glUniform4f(self._loc(name), *value)

    def set_mat4(self, name, matrix):
        glUniformMatrix4fv(self._loc(name), 1, GL_FALSE, glm.value_ptr(matrix))

    def set_bool(self, name, value):
        glUniform1i(self._loc(name), int(value))

    # ── Internal ──────────────────────────────────────────

    def _loc(self, name):
        return glGetUniformLocation(self.program, name)

    def _compile(self, source, shader_type, path=""):
        shader = glCreateShader(shader_type)
        glShaderSource(shader, source)
        glCompileShader(shader)
        if not glGetShaderiv(shader, GL_COMPILE_STATUS):
            log = glGetShaderInfoLog(shader).decode()
            raise RuntimeError(f"Shader compile error [{path}]:\n{log}")
        return shader

    def _link(self, vert, frag):
        program = glCreateProgram()
        glAttachShader(program, vert)
        glAttachShader(program, frag)
        glLinkProgram(program)
        if not glGetProgramiv(program, GL_LINK_STATUS):
            log = glGetProgramInfoLog(program).decode()
            raise RuntimeError(f"Shader link error:\n{log}")
        glDeleteShader(vert)
        glDeleteShader(frag)
        return program

    def __del__(self):
        try:
            glDeleteProgram(self.program)
        except Exception:
            pass
