import sys
import os
import token_defs as defs
from HashSymbolTable import HashSymbolTable


class Lexer:
    def __init__(self, path: str):
        self.path = path
        self.ts = HashSymbolTable()
        self.fip = []

    def classify(self, token: str):
        if token in defs.KEYWORDS:
            return "KEYWORD"
        if token in defs.OPERATORS:
            return "OPERATOR"
        if token in defs.DELIMITERS:
            return "DELIMITER"
        if defs.INT_RE(token):
            return "CONST_INT"
        if defs.FLOAT_RE(token):
            return "CONST_FLOAT"
        if defs.STR_RE(token):
            return "CONST_STRING"
        if defs.ID_RE(token):
            return "ID"
        return "UNKNOWN"

    def tokenize(self):
        try:
            with open(self.path, "r", encoding="utf-8") as f:
                for line in f:
                    self._process_line(line.strip())
        except FileNotFoundError:
            print(f"Eroare: fisierul {self.path} nu exista.")
            sys.exit(1)

    def _process_line(self, line: str):
        i = 0
        while i < len(line):
            ch = line[i]
            if ch.isspace():
                i += 1
                continue
            if ch == '"':
                start = i
                i += 1
                while i < len(line) and line[i] != '"':
                    i += 1
                i += 1
                token = line[start:i]
                pos = self.ts.insert(token, "CONST_STRING")
                self.fip.append((defs.ATOM_CODES["CONST_STRING"], pos))
                continue
            if i + 1 < len(line) and line[i:i + 2] in defs.MULTI_OPS:
                op = line[i:i + 2]
                self.fip.append((defs.ATOM_CODES[op], "-"))
                i += 2
                continue
            if ch in defs.OPERATORS or ch in defs.DELIMITERS:
                code = defs.ATOM_CODES.get(ch, None)
                if code is not None:
                    self.fip.append((code, "-"))
                i += 1
                continue
            start = i
            while i < len(line) and not line[i].isspace() and line[i] not in defs.OPERATORS and line[i] not in defs.DELIMITERS:
                i += 1
            token = line[start:i]
            ttype = self.classify(token)
            if ttype == "KEYWORD":
                code = defs.ATOM_CODES[token]
                self.fip.append((code, "-"))
            elif ttype in ("ID", "CONST_INT", "CONST_FLOAT", "CONST_STRING"):
                pos = self.ts.insert(token, ttype)
                code = defs.ATOM_CODES[ttype]
                self.fip.append((code, pos))
            else:
                self.fip.append(("??", "-"))

    def save(self, out_dir="."):
        ts_path = os.path.join(out_dir, "ts.txt")
        fip_path = os.path.join(out_dir, "fip.txt")
        self.ts.save_to_file(ts_path)
        with open(fip_path, "w", encoding="utf-8") as f:
            f.write("COD\tPoz_TS\n")
            f.write("-----------------\n")
            for cod, pos in self.fip:
                f.write(f"{cod}\t{pos}\n")
        return ts_path, fip_path
