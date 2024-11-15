"""
P3. Cheltuieli de familie
Scrieți o aplicație care gestionează cheltuielile de familie efectuate într-o lună.
Pentru o cheltuială se vor retine ziua (din luna), suma, tipul cheltuielii (5 categorii:
mâncare, întreținere, îmbrăcăminte, telefon, altele). Aplicația să permită
efectuarea repetată a următoarelor operații:
1. Adaugă cheltuiala
• adaugă o nouă cheltuială (se specifică ziua, suma, tipul)
• actualizează cheltuială (se specifică ziua, suma, tipul)
2. Ștergere.
• Șterge toate cheltuielile pentru ziua dată
• Șterge cheltuielile pentru un interval de timp (se dă ziua de început și
sfârșit, se șterg toate cheltuielile pentru perioada dată)
• Șterge toate cheltuielile de un anumit tip
3. Căutări.
• Tipărește toate cheltuielile mai mari decât o sumă dată
• Tipărește toate cheltuielile efectuate înainte de o zi dată și mai mici
decât o sumă (se dă suma și ziua, se tipăresc toate cheltuielile mai
mici ca suma dată și efectuate înainte de ziua specificată)
• tipărește toate cheltuielile de un anumit tip.
4. Rapoarte.
• Tipărește suma totală pentru un anumit tip de cheltuială
• Găsește ziua în care suma cheltuită e maximă
• Tipărește toate cheltuielile ce au o anumită sumă
• Tipărește cheltuielile sortate după tip
5. Filtrare.
• Elimină toate cheltuielile de un anumit tip
• Elimină toate cheltuielile mai mici decât o sumă dată
6. Undo
• Reface ultima operație (lista de cheltuieli revine ce exista înainte de
ultima operație care a modificat lista). – Nu folosiți funcția deepCopy
"""


def adauga_cheltuiala(cheltuieli, ziua, suma, tip_cheltuiala):
    if tip_cheltuiala not in tipuri_valide:
        print(
            "Tipul cheltuielii nu este valid. Vă rugăm să selectați unul dintre: mâncare, întreținere, îmbrăcăminte, telefon, altele.")
    else:
        if ziua in cheltuieli:
            cheltuieli[ziua].append((suma, tip_cheltuiala))
        else:
            cheltuieli[ziua] = [(suma, tip_cheltuiala)]
    print(f"Cheltuiala adăugată: Ziua {ziua}, Suma: {suma}, Tipul: {tip_cheltuiala}")


def actualizeaza_cheltuieli(cheltuieli, ziua, suma, tip_cheltuiala):
    if ziua in cheltuieli:
        for i, (suma_veche, tip_vechi) in enumerate(cheltuieli[ziua]):
            if tip_vechi == tip_cheltuiala:
                cheltuieli[ziua][i] = (suma_veche + suma, tip_cheltuiala)
                break
        else:
            cheltuieli[ziua].append((suma, tip_cheltuiala))
    else:
        cheltuieli[ziua] = [(suma, tip_cheltuiala)]


def afisare_cheltuieli(cheltuieli):
    if not cheltuieli:
        print("Nu există cheltuieli înregistrate.")
    else:
        print("Cheltuieli înregistrate:")
        for ziua, lista_cheltuieli in cheltuieli.items():
            print(f"Ziua {ziua}:")
            for suma, tip_cheltuiala in lista_cheltuieli:
                print(f"  Suma: {suma}, Tipul: {tip_cheltuiala}")


def sterge_cheltuieli_pentru_ziua_data(cheltuieli, ziua):
    try:
        ziua = int(ziua)
        if 1 <= ziua <= 31:
            if ziua in cheltuieli:
                del cheltuieli[ziua]
                print(f"Cheltuielile pentru ziua {ziua} au fost șterse.")
            else:
                print(f"Nu există cheltuieli înregistrate pentru ziua {ziua}.")
        else:
            print("Ziua introdusă nu este validă. Ziua trebuie să fie între 1 și 31.")
    except ValueError:
        print("Ziua introdusă nu este un număr valid.")


def sterge_cheltuieli_pentru_interval(cheltuieli, ziua_inceput, ziua_sfarsit):
    try:
        ziua_inceput = int(ziua_inceput)
        ziua_sfarsit = int(ziua_sfarsit)
        if 1 <= ziua_inceput <= 31 and 1 <= ziua_sfarsit <= 31 and ziua_inceput <= ziua_sfarsit:
            for ziua in range(ziua_inceput, ziua_sfarsit + 1):
                if ziua in cheltuieli:
                    del cheltuieli[ziua]
            print(f"Cheltuielile pentru intervalul de la ziua {ziua_inceput} la ziua {ziua_sfarsit} au fost șterse.")
        else:
            print("Zilele introduse nu sunt valide sau intervalul nu este corect.")
    except ValueError:
        print("Ziile introduse nu sunt numere valide.")


def sterge_cheltuieli_de_tip(cheltuieli, tip_cheltuiala):
    if tip_cheltuiala in tipuri_valide:
        for ziua in cheltuieli.copy():
            cheltuieli[ziua] = [(suma, tip) for suma, tip in cheltuieli[ziua] if tip != tip_cheltuiala]
        print(f"Cheltuielile de tipul '{tip_cheltuiala}' au fost șterse.")
    else:
        print("Tipul cheltuielii introdus nu este valid.")


def cheltuieli_mai_mari_decat_suma(cheltuieli, suma_minima):
    print(f"Cheltuieli mai mari decât {suma_minima}:")
    for ziua, lista_cheltuieli in cheltuieli.items():
        for suma, tip_cheltuiala in lista_cheltuieli:
            if suma > suma_minima:
                print(f"Ziua {ziua}, Suma: {suma}, Tipul: {tip_cheltuiala}")


def cheltuieli_inainte_de_zi_si_mai_mici_decat_suma(cheltuieli, ziua_maxima, suma_maxima):
    print(f"Cheltuieli efectuate înainte de ziua {ziua_maxima} și mai mici decât {suma_maxima}:")
    for ziua, lista_cheltuieli in cheltuieli.items():
        if ziua < ziua_maxima:
            for suma, tip_cheltuiala in lista_cheltuieli:
                if suma < suma_maxima:
                    print(f"Ziua {ziua}, Suma: {suma}, Tipul: {tip_cheltuiala}")


def cheltuieli_de_tip(cheltuieli, tip_cautat):
    print(f"Cheltuieli de tipul '{tip_cautat}':")
    for ziua, lista_cheltuieli in cheltuieli.items():
        for suma, tip_cheltuiala in lista_cheltuieli:
            if tip_cheltuiala == tip_cautat:
                print(f"Ziua {ziua}, Suma: {suma}, Tipul: {tip_cheltuiala}")


def suma_totala_pentru_tip(cheltuieli, tip_cautat):
    suma_totala = 0
    for ziua, lista_cheltuieli in cheltuieli.items():
        for suma, tip_cheltuiala in lista_cheltuieli:
            if tip_cheltuiala == tip_cautat:
                suma_totala += suma
    print(f"Suma totală pentru cheltuielile de tipul '{tip_cautat}' este: {suma_totala}")


def ziua_cu_suma_maxima(cheltuieli):
    ziua_maxima = None
    suma_maxima = 0
    for ziua, lista_cheltuieli in cheltuieli.items():
        suma_zi = sum(suma for suma, _ in lista_cheltuieli)
        if suma_zi > suma_maxima:
            ziua_maxima = ziua
            suma_maxima = suma_zi
    if ziua_maxima is not None:
        print(f"Ziua cu cea mai mare sumă cheltuită este ziua {ziua_maxima}, cu suma totală de {suma_maxima}")
    else:
        print("Nu există cheltuieli înregistrate.")


def cheltuieli_cu_suma(cheltuieli, suma_cautata):
    print(f"Cheltuieli cu suma de {suma_cautata}:")
    for ziua, lista_cheltuieli in cheltuieli.items():
        for suma, tip_cheltuiala in lista_cheltuieli:
            if suma == suma_cautata:
                print(f"Ziua {ziua}, Suma: {suma}, Tipul: {tip_cheltuiala}")


def cheltuieli_sortate_dupa_tip(cheltuieli):
    sorted_cheltuieli = {}
    for ziua, lista_cheltuieli in cheltuieli.items():
        sorted_cheltuieli[ziua] = sorted(lista_cheltuieli, key=lambda x: x[1])
    print("Cheltuieli sortate după tip:")
    for ziua, lista_cheltuieli in sorted_cheltuieli.items():
        print(f"Ziua {ziua}:")
        for suma, tip_cheltuiala in lista_cheltuieli:
            print(f"  Suma: {suma}, Tipul: {tip_cheltuiala}")


def elimina_cheltuieli_de_tip(cheltuieli, tip_cautat):
    for ziua in cheltuieli.copy():
        cheltuieli[ziua] = [(suma, tip) for suma, tip in cheltuieli[ziua] if tip != tip_cautat]
    print(f"Cheltuielile de tipul '{tip_cautat}' au fost eliminate.")


def elimina_cheltuieli_mai_mici_decat_suma(cheltuieli, suma_minima):
    for ziua in cheltuieli.copy():
        cheltuieli[ziua] = [(suma, tip) for suma, tip in cheltuieli[ziua] if suma >= suma_minima]
    print(f"Cheltuielile mai mici decât {suma_minima} au fost eliminate.")


def undo(cheltuieli):
    if undo_stack:
        ultima_stare = undo_stack.pop()
        cheltuieli.clear()
        cheltuieli.update(ultima_stare)
        print("Operația UNDO a fost efectuată cu succes.")


def test_adauga_cheltuiala():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 14, 100, "mâncare")
    assert len(cheltuieli) == 1
    assert len(cheltuieli[14]) == 1
    assert cheltuieli[14][0] == (100, "mâncare")


def test_actualizeaza_cheltuieli():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 17, 100, "mâncare")
    actualizeaza_cheltuieli(cheltuieli, 17, 50, "mâncare")
    assert len(cheltuieli) == 1
    assert len(cheltuieli[17]) == 1
    assert cheltuieli[17][0] == (150, "mâncare")


def test_sterge_cheltuieli_pentru_ziua_data():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 24, 100, "mâncare")
    sterge_cheltuieli_pentru_ziua_data(cheltuieli, 24)
    assert len(cheltuieli) == 0


def test_sterge_cheltuieli_pentru_interval():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 11, 100, "mâncare")
    adauga_cheltuiala(cheltuieli, 22, 50, "telefon")
    sterge_cheltuieli_pentru_interval(cheltuieli, 11, 22)
    assert len(cheltuieli) == 0


def test_sterge_cheltuieli_de_tip():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 13, 100, "mâncare")
    adauga_cheltuiala(cheltuieli, 26, 50, "telefon")
    sterge_cheltuieli_de_tip(cheltuieli, "telefon")
    assert len(cheltuieli) == 2
    assert len(cheltuieli[13]) == 1


def test_cheltuieli_mai_mari_decat_suma():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 19, 400, "mâncare")
    adauga_cheltuiala(cheltuieli, 23, 50, "telefon")
    cheltuieli_mai_mari_decat_suma(cheltuieli, 75)


def test_cheltuieli_inainte_de_zi_si_mai_mici_decat_suma():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 5, 100, "mâncare")
    adauga_cheltuiala(cheltuieli, 10, 50, "telefon")
    cheltuieli_inainte_de_zi_si_mai_mici_decat_suma(cheltuieli, 11, 60)


def test_cheltuieli_de_tip():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 18, 200, "mâncare")
    adauga_cheltuiala(cheltuieli, 20, 40, "telefon")
    cheltuieli_de_tip(cheltuieli, "mâncare")


def test_suma_totala_pentru_tip():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 6, 150, "mâncare")
    adauga_cheltuiala(cheltuieli, 27, 50, "telefon")
    adauga_cheltuiala(cheltuieli, 13, 250, "mâncare")
    suma_totala_pentru_tip(cheltuieli, "mâncare")


def test_ziua_cu_suma_maxima():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 18, 1000, "mâncare")
    adauga_cheltuiala(cheltuieli, 2, 550, "telefon")
    adauga_cheltuiala(cheltuieli, 14, 3050, "întreținere")
    ziua_cu_suma_maxima(cheltuieli)


def test_cheltuieli_cu_suma():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 31, 750, "mâncare")
    adauga_cheltuiala(cheltuieli, 22, 350, "telefon")
    adauga_cheltuiala(cheltuieli, 13, 750, "întreținere")
    cheltuieli_cu_suma(cheltuieli, 750)


def test_cheltuieli_sortate_dupa_tip():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 13, 150, "mâncare")
    adauga_cheltuiala(cheltuieli, 13, 350, "telefon")
    adauga_cheltuiala(cheltuieli, 31, 750, "mâncare")
    adauga_cheltuiala(cheltuieli, 22, 350, "îmbrăcăminte")
    adauga_cheltuiala(cheltuieli, 13, 750, "îmbrăcăminte")
    adauga_cheltuiala(cheltuieli, 6, 150, "mâncare")
    adauga_cheltuiala(cheltuieli, 27, 50, "altele")
    adauga_cheltuiala(cheltuieli, 13, 250, "întreținere")
    cheltuieli_sortate_dupa_tip(cheltuieli)


def afisare_stare_cheltuieli(cheltuieli):
    print("Starea cheltuielilor:")
    for ziua, lista_cheltuieli in cheltuieli.items():
        print(f"Ziua {ziua}:")
        for suma, tip_cheltuiala in lista_cheltuieli:
            print(f"  Suma: {suma}, Tipul: {tip_cheltuiala}")
    print()


def test_elimina_cheltuieli_de_tip():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 13, 150, "mâncare")
    adauga_cheltuiala(cheltuieli, 13, 350, "telefon")
    adauga_cheltuiala(cheltuieli, 31, 750, "mâncare")
    print("Starea inițială a cheltuielilor:")
    afisare_stare_cheltuieli(cheltuieli)
    elimina_cheltuieli_de_tip(cheltuieli, "mâncare")
    print("Starea cheltuielilor după eliminarea cheltuielilor de tip 'mâncare':")
    afisare_stare_cheltuieli(cheltuieli)
    # Verificați că nu mai există cheltuieli de tip "mâncare" în dicționar
    for ziua, lista_cheltuieli in cheltuieli.items():
        for suma, tip_cheltuiala in lista_cheltuieli:
            assert tip_cheltuiala != "mâncare"
    print()


def test_elimina_cheltuieli_mai_mici_decat_suma():
    cheltuieli = {}
    adauga_cheltuiala(cheltuieli, 13, 150, "mâncare")
    adauga_cheltuiala(cheltuieli, 13, 350, "telefon")
    adauga_cheltuiala(cheltuieli, 31, 750, "îmbrăcăminte")
    print("Starea inițială a cheltuielilor:")
    afisare_stare_cheltuieli(cheltuieli)
    elimina_cheltuieli_mai_mici_decat_suma(cheltuieli, 200)
    print("Starea cheltuielilor după eliminarea cheltuielilor mai mici decât 200:")
    afisare_stare_cheltuieli(cheltuieli)
    # Verificați că nu mai există cheltuieli cu sume mai mici de 200 în dicționar
    for ziua, lista_cheltuieli in cheltuieli.items():
        for suma, tip_cheltuiala in lista_cheltuieli:
            assert suma >= 200
    print()


def test_filtrare():
    print("Testarea funcțiilor de filtrare:")
    test_elimina_cheltuieli_de_tip()
    test_elimina_cheltuieli_mai_mici_decat_suma()


cheltuieli = {}

tipuri_valide = ["mâncare", "întreținere", "îmbrăcăminte", "telefon", "altele"]

undo_stack = []

while True:
    print("-------------------")
    print("MENIU:")
    print("0) Afisare cheltuieli")
    print("1) Adaugă cheltuială")
    print("2) Șterge cheltuială")
    print("3) Caută o cheltuială")
    print("4) Rapoarte")
    print("5) Filtrare")
    print("6) UNDO")
    print("7) Iesire din aplicatie")
    print("11) Testare asserturi de adaugare")
    print("12) Testare asserturi de stergere")
    print("13) Testare asserturi de cautare")
    print("14) Testare asserturi de rapoarte")
    print("15) Testare asserturi de filtrare")
    print("-------------------")

    optiune = input("Alegeți o opțiune: ")

    if (optiune == "0"):
        afisare_cheltuieli(cheltuieli)

    if (optiune == "11"):
        while True:
            sub_optiune = input("Alegeti o suboptione din 1,2,3: ")
            if sub_optiune == "1":
                test_adauga_cheltuiala()
            if sub_optiune == "2":
                test_actualizeaza_cheltuieli()
            if sub_optiune == "3":
                break

    if (optiune == "12"):
        while True:
            sub_optiune = input("Alegeti o suboptione din 1,2,3,4: ")
            if sub_optiune == "1":
                test_sterge_cheltuieli_pentru_ziua_data()
            if sub_optiune == "2":
                test_sterge_cheltuieli_pentru_interval()
            if sub_optiune == "3":
                test_sterge_cheltuieli_de_tip()
            if sub_optiune == "4":
                break

    if (optiune == "13"):
        while True:
            sub_optiune = input("Alegeti o suboptione din 1,2,3,4: ")
            if sub_optiune == "1":
                test_cheltuieli_mai_mari_decat_suma()
            if sub_optiune == "2":
                test_cheltuieli_inainte_de_zi_si_mai_mici_decat_suma()
            if sub_optiune == "3":
                test_cheltuieli_de_tip()
            if sub_optiune == "4":
                break

    if (optiune == "14"):
        while True:
            sub_optiune = input("Alegeti o suboptione din 1,2,3,4,5: ")
            if sub_optiune == "1":
                test_suma_totala_pentru_tip()
            if sub_optiune == "2":
                test_ziua_cu_suma_maxima()
            if sub_optiune == "3":
                test_cheltuieli_cu_suma()
            if sub_optiune == "4":
                test_cheltuieli_sortate_dupa_tip()
            if sub_optiune == "5":
                break

    if (optiune == "15"):
        while True:
            sub_optiune = input("Alegeti o suboptione din 1,2: ")
            if sub_optiune == "1":
                test_filtrare()
            if sub_optiune == "2":
                break

    if (optiune == "1"):
        while True:
            print("1. Adaugă cheltuială")
            print("2. Actualizează cheltuială")
            print("3. Afisare cheltuieli")
            print("4. Iesire din suboptiune")
            sub_optiune = input("Alegeti o suboptione: ")
            if (sub_optiune == "1"):
                ziua = input("Introduceți ziua: ")
                try:
                    ziua = int(ziua)
                    if 1 <= ziua <= 31:
                        suma = float(input("Introduceți suma: "))
                        tip_cheltuiala = input(
                            "Introduceți tipul cheltuielii (mâncare, întreținere, îmbrăcăminte, telefon, altele): ").lower()
                    else:
                        print("Ziua introdusă nu este validă. Ziua trebuie să fie între 1 și 31.")
                except ValueError:
                    print("Ziua introdusă nu este un număr valid.")
                adauga_cheltuiala(cheltuieli, ziua, suma, tip_cheltuiala)
                undo_stack.append(cheltuieli.copy())

            if (sub_optiune == "2"):
                ziua = input("Introduceți ziua pentru actualizare: ")
                try:
                    ziua = int(ziua)
                    if 1 <= ziua <= 31:
                        suma = float(input("Introduceți suma pentru actualizare: "))
                        tip_cheltuiala = input(
                            "Introduceți tipul cheltuielii pentru actualizare (mâncare, întreținere, îmbrăcăminte, telefon, altele): ").lower()
                        actualizeaza_cheltuieli(cheltuieli, ziua, suma, tip_cheltuiala)
                        undo_stack.append(cheltuieli.copy())
                    else:
                        print("Ziua introdusă nu este validă. Ziua trebuie să fie între 1 și 31.")
                except ValueError:
                    print("Ziua introdusă nu este un număr valid.")

            if (sub_optiune == "3"):
                afisare_cheltuieli(cheltuieli)

            if (sub_optiune == "4"):
                if undo_stack:
                    cheltuieli = undo_stack.pop()
                break

    if (optiune == "2"):
        while True:
            print("1. Șterge cheltuielile pentru ziua dată")
            print("2. Șterge cheltuielile pentru un interval de timp")
            print("3. Șterge cheltuielile de un anumit tip")
            print("4. Iesire din suboptiune")

            sub_optiune = input("Alegeti o subopțiune: ")

            if sub_optiune == "1":
                ziua = input("Introduceți ziua pentru ștergere: ")
                undo_stack.append(cheltuieli.copy())
                sterge_cheltuieli_pentru_ziua_data(cheltuieli, ziua)

            if sub_optiune == "2":
                ziua_inceput = input("Introduceți ziua de început pentru interval: ")
                ziua_sfarsit = input("Introduceți ziua de sfârșit pentru interval: ")
                undo_stack.append(cheltuieli.copy())
                sterge_cheltuieli_pentru_interval(cheltuieli, ziua_inceput, ziua_sfarsit)

            if sub_optiune == "3":
                tip_cheltuiala = input(
                    "Introduceți tipul cheltuielii de șters (mâncare, întreținere, îmbrăcăminte, telefon, altele): ").lower()
                undo_stack.append(cheltuieli.copy())
                sterge_cheltuieli_de_tip(cheltuieli, tip_cheltuiala)

            if sub_optiune == "4":
                break

    if (optiune == "3"):
        while True:
            print("1. Tipărește cheltuielile mai mari decât o sumă dată")
            print("2. Tipărește cheltuielile efectuate înainte de o zi dată și mai mici decât o sumă")
            print("3. Tipărește toate cheltuielile de un anumit tip")
            print("4. Ieșire din subopțiune")

            sub_optiune = input("Alegeți o subopțiune: ")

            if sub_optiune == "1":
                suma_minima = float(input("Introduceți suma minimă: "))
                cheltuieli_mai_mari_decat_suma(cheltuieli, suma_minima)

            if sub_optiune == "2":
                ziua_maxima = int(input("Introduceți ziua maximă: "))
                suma_maxima = float(input("Introduceți suma maximă: "))
                cheltuieli_inainte_de_zi_si_mai_mici_decat_suma(cheltuieli, ziua_maxima, suma_maxima)

            if sub_optiune == "3":
                tip_cautat = input("Introduceți tipul cheltuielii de căutat: ").lower()
                cheltuieli_de_tip(cheltuieli, tip_cautat)

            if sub_optiune == "4":
                break

    if (optiune == "4"):
        while True:
            print("1. Tipărește suma totală pentru un anumit tip de cheltuială")
            print("2. Găsește ziua în care suma cheltuită e maximă")
            print("3. Tipărește toate cheltuielile ce au o anumită sumă")
            print("4. Tipărește cheltuielile sortate după tip")
            print("5. Ieșire din subopțiune")

            sub_optiune = input("Alegeți o subopțiune: ")

            if sub_optiune == "1":
                tip_cautat = input("Introduceți tipul cheltuielii pentru a afla suma totală: ").lower()
                suma_totala_pentru_tip(cheltuieli, tip_cautat)

            if sub_optiune == "2":
                ziua_cu_suma_maxima(cheltuieli)

            if sub_optiune == "3":
                suma_cautata = float(input("Introduceți suma pentru a găsi cheltuieli: "))
                cheltuieli_cu_suma(cheltuieli, suma_cautata)

            if sub_optiune == "4":
                cheltuieli_sortate_dupa_tip(cheltuieli)

            if sub_optiune == "5":
                break

    if (optiune == "5"):
        while True:
            print("1. Elimină toate cheltuielile de un anumit tip")
            print("2. Elimină toate cheltuielile mai mici decât o sumă dată")
            print("3. Ieșire din subopțiune de filtrare")

            sub_optiune = input("Alegeți o subopțiune de filtrare: ")

            if sub_optiune == "1":
                tip_cautat = input("Introduceți tipul cheltuielii pentru eliminare: ").lower()
                undo_stack.append(cheltuieli.copy())
                elimina_cheltuieli_de_tip(cheltuieli, tip_cautat)

            if sub_optiune == "2":
                suma_minima = float(input("Introduceți suma minimă pentru eliminare: "))
                undo_stack.append(cheltuieli.copy())
                elimina_cheltuieli_mai_mici_decat_suma(cheltuieli, suma_minima)

            if sub_optiune == "3":
                break

    if (optiune == "6"):
        if undo_stack:
            cheltuieli = undo_stack.pop()
        else:
            print("Nu există cheltuieli introduse încă.")
        continue

    if (optiune == "7"):
        break

print("Program încheiat")
