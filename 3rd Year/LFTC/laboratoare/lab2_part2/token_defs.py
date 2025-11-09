
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

KEYWORDS = {"int", "float", "word", "loop", "maybe", "else", "forloop", "hear", "speak", "return", "#include", "main", "double", "string"}
OPERATORS = {"+", "-", "*", "/", "%", "<", "<=", ">", ">=", "==", "!=", "<-", "<<", ">>"}
DELIMITERS = {"(", ")", "{", "}", ";", ","}
MULTI_OPS = {op for op in OPERATORS if len(op) > 1}

# sortam operatorii si delimitatorii descrescator dupa lungime pentru a evita ambiguitatile la tokenizare,
# de exemplu sa nu interpretam "<=" ca "<" si "="
SORTED_OPS_AND_DELIMS = sorted(list(OPERATORS | DELIMITERS | KEYWORDS | {"<~library~>"}), key=len, reverse=True)