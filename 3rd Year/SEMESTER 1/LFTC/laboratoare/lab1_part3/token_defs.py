import re

ATOM_CODES = {
    "ID": 0,
    "CONST_INT": 1,
    "CONST_FLOAT": 2,
    "CONST_STRING": 3,
    "#include": 4,
    "<~library~>": 5,
    "main": 6,
    "(": 7,
    ")": 8,
    "{": 9,
    "}": 10,
    "int": 11,
    "double": 12,
    "float": 13,
    "string": 14,
    "word": 15,
    "maybe": 16,
    "else": 17,
    "loop": 18,
    "forloop": 19,
    "hear": 20,
    "speak": 21,
    "<<": 22,
    ">>": 23,
    "<-": 24,
    "+": 25,
    "-": 26,
    "*": 27,
    "/": 28,
    "%": 29,
    "<": 30,
    ">": 31,
    "<=": 32,
    ">=": 33,
    "==": 34,
    "!=": 35,
    ";": 36,
    ",": 37,
    "return": 38
}

KEYWORDS = {"int", "float", "word", "loop", "maybe", "else", "forloop", "hear", "speak", "return", "#include"}
OPERATORS = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=", "==", "!=", "<-", "<<", ">>"}
DELIMITERS = {"(", ")", "{", "}", ";", ",", "[", "]"}
MULTI_OPS = {op for op in OPERATORS if len(op) > 1}

def INT_RE(s: str) -> bool:
    return bool(s) and all(ch in "0123456789" for ch in s)

def FLOAT_RE(s: str) -> bool:
    parts = s.split('.')
    if len(parts) != 2:
        return False
    a, b = parts
    return bool(a) and bool(b) and all(ch in "0123456789" for ch in a) and all(ch in "0123456789" for ch in b)

def ID_RE(s: str) -> bool:
    if not s:
        return False
    first = s[0]
    if not (('A' <= first <= 'Z') or ('a' <= first <= 'z') or first == '_'):
        return False
    for ch in s[1:]:
        if not (('A' <= ch <= 'Z') or ('a' <= ch <= 'z') or ('0' <= ch <= '9') or ch == '_'):
            return False
    return True

def STR_RE(s: str) -> bool:
    if len(s) < 2 or s[0] != '"' or s[-1] != '"':
        return False
    i = 1
    while i < len(s) - 1:
        ch = s[i]
        if ch == '\\':
            i += 2
            if i > len(s) - 1:
                return False
            continue
        if ch == '"':
            return False
        i += 1
    return True