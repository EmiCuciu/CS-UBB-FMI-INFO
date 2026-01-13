from grammar import Grammar
from ll1_logic import LL1Parser
import re


def simple_lexer(filename):
    with open(filename, 'r') as f:
        content = f.read()

    token_spec = [
        ('INCLUDE', r'#include\s*<\s*iostream\s*>'),
        ('CONST', r'\d+(\.\d+)?'),
        ('STRING', r'"[^"]*"'),
        ('ID', r'[a-zA-Z_][a-zA-Z0-9_]*'),
        ('ASSIGN', r'<-'),
        ('IO_OP', r'<<|>>'),
        ('REL_OP', r'==|!=|<=|>=|[<>]'),
        ('ARITH_OP', r'[+\-*/%]'),
        ('SEP', r'[;(){}]'),
        ('SKIP', r'[ \t\n]+')
    ]

    keywords = {
        'int', 'float', 'double', 'string', 'word', 'main', 'return',
        'maybe', 'else', 'loop', 'forloop', 'hear', 'speak',
        'cattimp', 'executa', 'sfcattimp'
    }

    tok_regex = '|'.join('(?P<%s>%s)' % pair for pair in token_spec)
    tokens = []

    for mo in re.finditer(tok_regex, content):
        kind = mo.lastgroup
        value = mo.group()
        if kind == 'SKIP': continue

        if kind == 'INCLUDE':
            tokens.extend(['#include', '<iostream>'])
        elif kind == 'ID' and value in keywords:
            tokens.append(value)
        elif kind == 'ID':
            tokens.append('ID')
        elif kind == 'CONST':
            tokens.append('CONST')
        elif kind == 'STRING':       # <--- ADAGĂ ACEST BLOC
            tokens.append('CONST')   # Tratăm string-ul ca pe o constantă generică
        elif kind == 'ASSIGN':
            tokens.append('<-')
        elif kind in ['IO_OP', 'REL_OP', 'ARITH_OP', 'SEP']:
            tokens.append(value)
        else:
            tokens.append(value)

    return tokens


# --- Functie Generala de Testare ---
def ruleaza_test(nume_test, fisier_gramatica, fisier_input, foloseste_lexer=True):
    print(f"\n Rulare: {nume_test}")
    try:
        g = Grammar(fisier_gramatica)
        parser = LL1Parser(g)

        if foloseste_lexer:
            seq = simple_lexer(fisier_input)
        else:
            with open(fisier_input, 'r') as f:
                seq = f.read().strip().split()

        result = parser.parse(seq)
        print(" SECVENTA ACCEPTATA")

        # 4. Scriem rezultatul in fisier (optional, doar pt MLP principal)
        # if "program.txt" in fisier_input:
        if "exemple/1.txt" in fisier_input:
            with open("arbore_mlp.txt", "w") as f:
                for line in result: f.write(line + "\n")
            print("   (Arborele salvat in arbore_mlp.txt)")

    except Exception as e:
        print(f" EROARE: {e}")


# --- Main Simplificat ---
if __name__ == "__main__":
    # Test 1: Partea 1
    ruleaza_test("Partea 1 - Gramatica Expresii", "g1.txt", "seq.txt", foloseste_lexer=False)

    # Test 2: Partea 2
    ruleaza_test("Partea 2 - Program MLP Corect", "mlp_grammar.txt", "program.txt")
    ruleaza_test("Partea 2 - Program MLP Corect", "mlp_grammar.txt", "exemple/1.txt")
    ruleaza_test("Partea 2 - Program MLP Corect", "mlp_grammar.txt", "exemple/3_simplu.txt")

    # Test 3: Exemplu de eroare
    ruleaza_test("Test Eroare Sintactica", "mlp_grammar.txt", "exemple/eroare_sintactica.txt")


    # Test 4: Exemplu de GRAMATICA gresita (Non-LL1)

    ruleaza_test("Test Conflict LL(1) (Gramatica Gresita)", "rau/mlp_grammar_BAD.txt", "program.txt")