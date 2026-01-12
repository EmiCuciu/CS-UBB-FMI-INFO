def get_first(grammar):
    first = {t: {t} for t in grammar.terminals}
    for nt in grammar.non_terminals:
        first[nt] = set()
    first['epsilon'] = {'epsilon'}

    changed = True
    while changed:
        changed = False
        for lhs in grammar.productions:
            for rhs in grammar.productions[lhs]:
                rhs_first = set()
                should_add_epsilon = True

                for symbol in rhs:
                    sym_first = first.get(symbol, set())
                    rhs_first.update(sym_first - {'epsilon'})
                    if 'epsilon' not in sym_first:
                        should_add_epsilon = False
                        break

                if should_add_epsilon:
                    rhs_first.add('epsilon')

                if not rhs_first.issubset(first[lhs]):
                    first[lhs].update(rhs_first)
                    changed = True
    return first


def get_follow(grammar, first):
    follow = {nt: set() for nt in grammar.non_terminals}
    if grammar.start_symbol:
        follow[grammar.start_symbol].add('$')

    changed = True
    while changed:
        changed = False
        for lhs in grammar.productions:
            for rhs in grammar.productions[lhs]:
                if rhs == ['epsilon']: continue

                for i, symbol in enumerate(rhs):
                    if symbol in grammar.non_terminals:
                        beta = rhs[i + 1:]
                        beta_first = set()
                        all_beta_eps = True

                        if not beta:
                            beta_first = {'epsilon'}
                        else:
                            for b_sym in beta:
                                f = first.get(b_sym, {b_sym})
                                beta_first.update(f - {'epsilon'})
                                if 'epsilon' not in f:
                                    all_beta_eps = False
                                    break
                            if all_beta_eps: beta_first.add('epsilon')

                        # Regula 1: FIRST(beta) - {epsilon}
                        before_len = len(follow[symbol])
                        follow[symbol].update(beta_first - {'epsilon'})

                        # Regula 2: Daca beta -> epsilon, adauga FOLLOW(lhs)
                        if 'epsilon' in beta_first:
                            follow[symbol].update(follow[lhs])

                        if len(follow[symbol]) > before_len:
                            changed = True
    return follow


class LL1Parser:
    def __init__(self, grammar):
        self.grammar = grammar
        self.first = get_first(grammar)
        self.follow = get_follow(grammar, self.first)
        self.table = {}
        try:
            self.build_table()
        except Exception as e:
            print(f"EROARE FATALA LA GENERAREA TABELEI: {e}")
            self.table = None # Invalidam tabela

    def build_table(self):
        for lhs in self.grammar.productions:
            for rhs in self.grammar.productions[lhs]:
                first_alpha = set()
                all_eps = True
                if rhs == ['epsilon']:
                    first_alpha = {'epsilon'}
                else:
                    for s in rhs:
                        f = self.first.get(s, {s})
                        first_alpha.update(f - {'epsilon'})
                        if 'epsilon' not in f:
                            all_eps = False
                            break
                    if all_eps: first_alpha.add('epsilon')

                for term in first_alpha:
                    if term != 'epsilon':
                        self._add_entry(lhs, term, rhs)

                if 'epsilon' in first_alpha:
                    for term in self.follow[lhs]:
                        self._add_entry(lhs, term, rhs)

    def _add_entry(self, lhs, term, rhs):
        if lhs not in self.table: self.table[lhs] = {}
        if term in self.table[lhs]:
            if self.table[lhs][term] != rhs:
                # Aici verificam daca avem Conflict
                raise Exception(f"Conflict LL(1) la M[{lhs}, {term}]: {self.table[lhs][term]} vs {rhs}")
        self.table[lhs][term] = rhs

    def parse(self, input_seq):
        if self.table is None:
            raise Exception("Nu se poate efectua parsarea: Gramatica nu este LL(1).")

        stack = ['$', self.grammar.start_symbol]
        w = input_seq + ['$']
        idx = 0
        output = []

        # print(f"Start Parsare. Secventa: {w}")

        while stack:
            top = stack.pop()
            current = w[idx]

            # print(f"Stiva: {stack + [top]} | Input curent: {current}")

            if top == '$':
                if current == '$':
                    return output
                else:
                    raise Exception("Input neterminat (stiva goala dar mai exista intrare).")

            if top == 'epsilon': continue

            if top in self.grammar.terminals:
                if top == current:
                    idx += 1
                else:
                    raise Exception(f"Eroare sintactica: Asteptat '{top}', gasit '{current}'")
            elif top in self.grammar.non_terminals:
                if top in self.table and current in self.table[top]:
                    rhs = self.table[top][current]
                    # Formatare output ca sirul productiilor
                    prod_str = f"{top} -> {' '.join(rhs)}"
                    output.append(prod_str)

                    if rhs != ['epsilon']:
                        for sym in reversed(rhs):
                            stack.append(sym)
                else:
                    raise Exception(f"Eroare sintactica: Nu exista tranzitie in tabela M[{top}, {current}]")
            else:
                raise Exception(f"Simbol invalid in stiva: {top}")