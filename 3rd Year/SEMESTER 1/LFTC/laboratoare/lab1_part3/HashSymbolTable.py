class HashSymbolTable:
    def __init__(self, size=211):   #211 buckets, numar prim pentru distributie mai buna
        self.size = size
        self.buckets = [[] for _ in range(size)]    # fiecare bucket contine o lista de tokenuri
        self.entries = []  # (token, tip)
        self.token_to_pos = {}  # mapare token -> pozitie in entries

    def _hash(self, token: str):
        h = 0
        for ch in token:
            h = (h * 31 + ord(ch)) & 0xFFFFFFFF
        return h % self.size

    def insert(self, token: str, tip: str):
        if token in self.token_to_pos:
            return self.token_to_pos[token] # daca tokenul exista deja, returneaza pozitia lui
        idx = self._hash(token)
        self.buckets[idx].append(token)
        pos = len(self.entries)
        self.entries.append((token, tip))
        self.token_to_pos[token] = pos
        return pos

    def save_to_file(self, path: str):
        with open(path, "w", encoding="utf-8") as f:
            f.write("Poz_TS\tAtom_lexical\tTip\n")
            f.write("---------------------------------\n")
            for pos, (token, tip) in enumerate(self.entries):
                f.write(f"{pos}\t{token}\t{tip}\n")