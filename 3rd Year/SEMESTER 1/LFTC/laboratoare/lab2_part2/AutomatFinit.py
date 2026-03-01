# AutomatFinit.py

import sys


class AutomatFinit:
    def __init__(self):
        self.states = set()
        self.alphabet = set()
        self.transitions = {}
        self.start_state = ""
        self.final_states = set()

    def citire_tastatura(self):
        """ Citeste definitia automatului de la tastatura. """
        try:
            nr_states = int(input("Numar de stari: "))
            states_input = input(f"Introduceti {nr_states} stari (separate de spatiu): ").split()
            self.states.update(states_input)
            if len(self.states) != nr_states:
                print("Atentie: Ati introdus mai putine stari unice decat numarul declarat.", file=sys.stderr)

            alphabet_size = int(input("Dimensiunea alfabetului: "))
            alphabet_input = input(f"Introduceti {alphabet_size} simboluri (separate de spatiu): ").split()
            self.alphabet.update(alphabet_input)
            if len(self.alphabet) != alphabet_size:
                print("Atentie: Ati introdus mai putine simboluri unice decat numarul declarat.", file=sys.stderr)

            nr_transitions = int(input("Numar de tranzitii: "))
            print("Introduceti tranzitiile (format: stare_curenta simbol stare_urmatoare):")
            for _ in range(nr_transitions):
                try:
                    current_state, symbol, next_state = input().split()

                    if (current_state, symbol) in self.transitions:
                        print(f"\nError: Automatul nu este determinist!", file=sys.stderr)
                        print(f"Tranzitie nedeterminista detectata la starea {current_state} cu simbolul {symbol}.",
                              file=sys.stderr)
                        return False  # Esueaza citirea

                    self.transitions[(current_state, symbol)] = next_state
                except ValueError:
                    print("Format invalid. Va rugam introduceti 3 elemente separate de spatiu.", file=sys.stderr)
                    continue  # Permite utilizatorului sa reincerce pentru aceasta tranzitie

            self.start_state = input("Starea initiala: ")

            nr_final_states = int(input("Numar de stari finale: "))
            final_states_input = input(f"Introduceti {nr_final_states} stari finale (separate de spatiu): ").split()
            self.final_states.update(final_states_input)

            print("Automat citit cu succes de la tastatura.")
            return True

        except ValueError:
            print("Input invalid. Va rugam introduceti un numar.", file=sys.stderr)
            return False
        except Exception as e:
            print(f"A aparut o eroare: {e}", file=sys.stderr)
            return False

    def citire_fisier(self, filename: str):
        """ Citeste definitia automatului dintr-un fisier. """
        try:
            with open(filename, "r") as f:
                nr_states = int(f.readline().strip())
                self.states.update(f.readline().strip().split())

                alphabet_size = int(f.readline().strip())
                self.alphabet.update(f.readline().strip().split())

                nr_transitions = int(f.readline().strip())
                for _ in range(nr_transitions):
                    current_state, symbol, next_state = f.readline().strip().split()

                    if (current_state, symbol) in self.transitions:
                        print(f"\nError: Automatul nu este determinist!", file=sys.stderr)
                        print(f"Tranzitie nedeterminista detectata la starea {current_state} cu simbolul {symbol}.",
                              file=sys.stderr)
                        return False  # Esueaza citirea

                    self.transitions[(current_state, symbol)] = next_state

                self.start_state = f.readline().strip()

                nr_final_states = int(f.readline().strip())
                self.final_states.update(f.readline().strip().split())

            print(f"Automat citit cu succes din {filename}.")
            return True

        except FileNotFoundError:
            print(f"Eroare: Fisierul {filename} nu a fost gasit.", file=sys.stderr)
            return False
        except Exception as e:
            print(f"Eroare la parsarea fisierului {filename}: {e}", file=sys.stderr)
            return False

    def afisare_stari(self):
        """ Afiseaza starile automatului. """
        print("Starile sunt:", " ".join(sorted(list(self.states))))

    def afisare_alfabet(self):
        """ Afiseaza alfabetul automatului. """
        print("Alfabetul este:", " ".join(sorted(list(self.alphabet))))

    def afisare_tranzitii(self):
        """ Afiseaza tranzitiile automatului. """
        print("Tranzitiile sunt:")
        for (state, symbol), next_state in self.transitions.items():
            print(f"  ({state}, {symbol}) -> {next_state}")

    def afisare_stari_finale(self):
        """ Afiseaza starile finale ale automatului. """
        print("Starile finale sunt:", " ".join(sorted(list(self.final_states))))

    def verifica_secventa(self, secventa: str) -> bool:
        """ Verifica daca o secventa data este acceptata de automat. """
        current_state = self.start_state

        for symbol in secventa:
            if (current_state, symbol) not in self.transitions:
                return False
            current_state = self.transitions[(current_state, symbol)]

        return current_state in self.final_states

    def cel_mai_lung_prefix_acceptat(self, secventa: str) -> str:
        """
        Determina cel mai lung prefix dintr-o secventa data
        care este o secventa acceptata de automat.
        """
        current_state = self.start_state
        longest_prefix = ""
        current_prefix = ""

        for symbol in secventa:
            if (current_state, symbol) not in self.transitions:
                break

            current_state = self.transitions[(current_state, symbol)]
            current_prefix += symbol

            if current_state in self.final_states:
                longest_prefix = current_prefix

        return longest_prefix