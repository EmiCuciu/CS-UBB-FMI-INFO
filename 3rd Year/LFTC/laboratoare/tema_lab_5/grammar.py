class Grammar:
    def __init__(self, filename):
        self.productions = {}
        self.start_symbol = None
        self.non_terminals = set()
        self.terminals = set()
        self.load_from_file(filename)

    def load_from_file(self, filename):
        with open(filename, 'r') as f:
            for line in f:
                line = line.strip()
                if not line or line.startswith('#'): continue

                # Format asteptat: S -> A B | c
                if "->" in line:
                    lhs, rhs = line.split("->")
                    lhs = lhs.strip()

                    if self.start_symbol is None:
                        self.start_symbol = lhs

                    self.non_terminals.add(lhs)

                    alternatives = rhs.split("|")
                    if lhs not in self.productions:
                        self.productions[lhs] = []

                    for alt in alternatives:
                        symbols = alt.strip().split()
                        # Tratare epsilon explicit
                        if symbols == ['epsilon']:
                            symbols = ['epsilon']
                        self.productions[lhs].append(symbols)

                        # Colectare potentiale terminale
                        for s in symbols:
                            if s != 'epsilon':
                                self.terminals.add(s)

        # Tot ce apare in dreapta si nu e in stanga este terminal
        self.terminals = self.terminals - self.non_terminals