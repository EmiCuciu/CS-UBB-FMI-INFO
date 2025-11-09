import sys
import os
import token_defs as defs
from HashSymbolTable import HashSymbolTable
from AutomatFinit import AutomatFinit  # <-- Importam clasa noua


class Lexer:
    def __init__(self, path: str):
        self.path = path
        self.ts = HashSymbolTable()
        self.fip = []
        self.has_errors = False

        self.fa_id = self.load_fa("automate/automat_id.txt")
        self.fa_int = self.load_fa("automate/automat_int.txt")
        self.fa_float = self.load_fa("automate/automat_float.txt")

        if not self.fa_id or not self.fa_float or not self.fa_int:
            print("Eroare critica: Nu s-au putut incarca fisierele automatelor. Oprire.", file=sys.stderr)
            sys.exit(1)

    def load_fa(self, filename: str) -> AutomatFinit | None:
        """ Functie helper pentru a incarca un AF si a raporta erori. """
        fa = AutomatFinit()
        if not fa.citire_fisier(filename):
            print(f"Eroare la incarcarea {filename}", file=sys.stderr)
            return None
        return fa

    def tokenize(self):
        """ Proceseaza fisierul de input linie cu linie. """
        try:
            with open(self.path, "r", encoding="utf-8") as f:
                for line_num, line in enumerate(f, 1):
                    self._process_line(line.strip(), line_num)
        except FileNotFoundError:
            print(f"Eroare: fisierul {self.path} nu exista.", file=sys.stderr)
            sys.exit(1)

        if self.has_errors:
            print("\nAnaliza lexicala a esuat din cauza erorilor.", file=sys.stderr)

    def _process_line(self, line: str, line_num: int):
        """
        Proceseaza o singura linie folosind automatele finite
        si principiul "celui mai lung prefix acceptat".
        """
        i = 0
        while i < len(line):
            start_col = i + 1
            ch = line[i]

            # 1. Ignora spatiile albe
            if ch.isspace():
                i += 1
                continue

            # 2. Gestiune String-uri (caz special)
            if ch == '"':
                start = i
                i += 1  # Trecem peste ghilimeaua de deschidere

                while i < len(line) and line[i] != '"':
                    # Gestionare escape-uri simple
                    if line[i] == '\\' and i + 1 < len(line):
                        i += 2
                        continue
                    i += 1

                if i == len(line):
                    print(f"Eroare Lexicala [Linia {line_num}, Coloana {start + 1}]: String neterminat.", file=sys.stderr)
                    self.has_errors = True
                    i = len(line)  # Oprim procesarea acestei linii
                    continue

                i += 1  # Trecem peste ghilimeaua de inchidere
                token = line[start:i]
                pos = self.ts.insert(token, "CONST_STRING")
                self.fip.append((defs.ATOM_CODES["CONST_STRING"], pos))
                continue

            # 3. Gestiune Operatori si Delimitatori (caz simplu, prioritar)
            # Verificam de la cel mai lung la cel mai scurt (ex: '<<' inainte de '<')
            best_match_simple = ""
            for op_or_delim in defs.SORTED_OPS_AND_DELIMS:
                if line.startswith(op_or_delim, i):
                    best_match_simple = op_or_delim
                    break  # Am gasit cea mai lunga potrivire posibila

            if best_match_simple:
                # Verificare speciala pentru #include <iostream>
                if best_match_simple == "#include":
                    self.fip.append((defs.ATOM_CODES["#include"], "-"))
                    i += len(best_match_simple)
                    continue

                code = defs.ATOM_CODES.get(best_match_simple)
                if code is not None:
                    self.fip.append((code, "-"))
                    i += len(best_match_simple)
                    continue

            # 4. Gestiune ID-uri si Constante (cu Automate Finite)
            substring_to_test = line[i:]

            prefix_id = self.fa_id.cel_mai_lung_prefix_acceptat(substring_to_test)
            prefix_int = self.fa_int.cel_mai_lung_prefix_acceptat(substring_to_test)
            prefix_float = self.fa_float.cel_mai_lung_prefix_acceptat(substring_to_test)

            matches = {
                "ID": prefix_id,
                "CONST_INT": prefix_int,
                "CONST_FLOAT": prefix_float
            }

            # Gasim potrivirea cea mai lunga
            best_match_type = ""
            best_match_token = ""
            for ttype, token in matches.items():
                if len(token) > len(best_match_token):
                    best_match_token = token
                    best_match_type = ttype

            # 5. Procesare potrivire FA
            if best_match_token:
                # Am gasit un token!

                # Verificam daca e un Keyword
                if best_match_type == "ID" and best_match_token in defs.KEYWORDS:
                    code = defs.ATOM_CODES[best_match_token]
                    self.fip.append((code, "-"))
                else:
                    # E ID sau Constanta
                    pos = self.ts.insert(best_match_token, best_match_type)
                    code = defs.ATOM_CODES[best_match_type]
                    self.fip.append((code, pos))

                i += len(best_match_token)

            # 6. Daca NIMIC nu s-a potrivit -> EROARE
            elif not best_match_simple:
                print(f"Eroare Lexicala [Linia {line_num}, Coloana {start_col}]: Simbol necunoscut: '{line[i]}'",
                      file=sys.stderr)
                self.has_errors = True
                i += 1  # Sarim peste caracterul problematic

    def save(self, out_dir="."):
        """ Salveaza FIP si TS in fisiere. """
        # Nu salvam output-ul daca au fost erori
        if self.has_errors:
            print("\nFisierele de output (FIP, TS) NU au fost generate din cauza erorilor.")
            return None, None

        ts_path = os.path.join(out_dir, "ts.txt")
        fip_path = os.path.join(out_dir, "fip.txt")

        self.ts.save_to_file(ts_path)

        with open(fip_path, "w", encoding="utf-8") as f:
            f.write("COD\tPoz_TS\n")
            f.write("-----------------\n")
            for cod, pos in self.fip:
                f.write(f"{cod}\t{pos}\n")

        print(f"\nAnaliza lexicala finalizata cu succes.")
        print(f"TS salvat in: {ts_path}")
        print(f"FIP salvat in: {fip_path}")
        return ts_path, fip_path