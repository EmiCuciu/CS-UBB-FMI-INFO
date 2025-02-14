def validate_titlu(titlu):
    # Validare titlu film
    return titlu is not None and len(titlu) > 0 and titlu[0].isupper()

def validate_descriere(descriere):
    # Validare descriere film
    return descriere is not None and len(descriere) > 0  # Adaugă orice altă logică de validare necesară

def validate_gen(gen):
    # Validare gen film
    return gen is not None and len(gen) > 0  # Adaugă orice altă logică de validare necesară

def validate_nume(nume):
    # Validare nume client
    return nume is not None and len(nume) > 0 and nume.istitle()  # Adaugă orice altă logică de validare necesară

def validate_cnp(cnp):
    # Validare CNP client
    return cnp is not None and cnp.isdigit() and len(cnp) == 13 and (cnp[0] == '5' or cnp[0] == '6')

