import os
import sys

import token_defs as defs
from HashSymbolTable import HashSymbolTable


class Lexer:
    def __init__(self, path: str):
        self.path = path
        self.ts = HashSymbolTable()
        self.fip = []
        self.has_errors = False

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
                for line_num, line in enumerate(f, 1):
                    self._process_line(line.strip(), line_num)
        except FileNotFoundError:
            print(f"Eroare: fisierul {self.path} nu exista.")
            sys.exit(1)

        if self.has_errors:
            print("\n Analiza lexicala s-a incheiat cu erori.", file=sys.stderr)

    def _process_line(self, line: str, line_num: int):
        i = 0
        while i < len(line):
            start_col = i + 1
            ch = line[i]

            if ch.isspace():
                i += 1
                continue

            # Gestiune String-uri
            if ch == '"':
                start = i
                i += 1
                while i < len(line) and line[i] != '"':
                    if line[i] == '\\' and i + 1 < len(line):
                        i += 2
                        continue
                    i += 1

                if i >= len(line) or line[i] != '"':
                    # Eroare: String neterminat
                    print(f"Eroare Lexicala [Linia {line_num} , Coloana {start + 1}]: String neterminat.",
                          file=sys.stderr)
                    self.has_errors = True
                    i = len(line)  # oprim procesarea liniei
                    continue

                i += 1  # trecem peste " de inchidere
                token = line[start:i]
                pos = self.ts.insert(token, "CONST_STRING")
                self.fip.append((defs.ATOM_CODES["CONST_STRING"], pos))
                continue

            # Gestiune Operatori & Delimitatori
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

            # Gestiune token-uri generale (ID, CONST, KEYWORD)
            start = i
            while i < len(line) and not line[i].isspace() and line[i] not in defs.OPERATORS and line[i] not in defs.DELIMITERS:
                i += 1

            # daca nu am avansat inseamna ca ch este un caracter necunoscut
            if start == i:
                # Eroare: Simbol necunoscut
                print(f"Eroare Lexicala [Linia {line_num} , Coloana {start_col}]: Simbol necunoscut.", file=sys.stderr)
                self.has_errors = True
                i += 1
                continue

            token = line[start:i]
            ttype = self.classify(token)

            if ttype == "KEYWORD":
                code = defs.ATOM_CODES[token]
                self.fip.append((code, "-"))
            elif ttype in ("ID", "CONST_INT", "CONST_FLOAT"):
                pos = self.ts.insert(token, ttype)
                code = defs.ATOM_CODES[ttype]
                self.fip.append((code, pos))
            else:
                # Eroare: token-urile UNKNOWN (ex: "9abc", "12.3.4")
                print(
                    f"Eroare Lexicala [Linia {line_num}, Coloana {start_col}]: Token invalid sau necunoscut: '{token}'", file=sys.stderr)
                self.has_errors = True

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
