from repository.infrasturctura import InchiriereService
from validator.validari import (validate_titlu, validate_descriere, validate_gen,validate_nume, validate_cnp)

def main():
    inchiriere_service = InchiriereService()

    while True:
        print("\nMeniu:")
        print("z. Adauga din fisier")
        print("0. Afiseaza tot")
        print("1. Adăugare Film")
        print("2. Ștergere Film")
        print("3. Modificare Film")
        print("4. Căutare Film")
        print("5. Adăugare Client")
        print("6. Ștergere Client")
        print("7. Modificare Client")
        print("8. Căutare Client")
        print("9. Închiriere Film")
        print("10. Returnare Film")
        print("11. Raport - Clienți cu filme închiriate")
        print("12. Raport - Cele mai închiriate filme")
        print("13. Raport - Primii 30% clienți cu cele mai multe filme închiriate")
        print("x. Ieșire")
        choice = input("Alegeți opțiunea: ")

        if choice == "z":
            inchiriere_service.adauga_din_fisier('date.txt')
            print("Date adaugate cu succes")

        elif choice == "1":
            titlu = input("Introduceți titlul filmului: ")
            descriere = input("Introduceți descrierea filmului: ")
            gen = input("Introduceți genul filmului: ")

            if validate_titlu(titlu) and validate_descriere(descriere) and validate_gen(gen):
                inchiriere_service.adauga_film(titlu, descriere, gen)
            else:
                print("Eroare: Datele introduse pentru film sunt invalide.")

        elif choice == "2":
            film_id = int(input("Introduceți ID-ul filmului de șters: "))
            inchiriere_service.sterge_film(film_id)

        elif choice == "3":
            film_id = int(input("Introduceți ID-ul filmului de modificat: "))
            titlu = input("Introduceți noul titlu: ")
            descriere = input("Introduceți noua descriere: ")
            gen = input("Introduceți noul gen: ")
            inchiriere_service.modifica_film(film_id, titlu,descriere, gen)

        elif choice == "4":
            titlu = input("Introduceți titlul filmului de căutat: ")
            rezultate = inchiriere_service.cauta_film(titlu)
            for film in rezultate:
                print(f"ID: {film.id}, Titlu: {film.titlu}, Descriere: {film.descriere} Gen: {film.gen}")


        elif choice == "5":
            nume = input("Introduceți numele clientului: ")
            cnp = input("Introduceți CNP-ul clientului: ")

            if validate_nume(nume) and validate_cnp(cnp):
                inchiriere_service.adauga_client(nume, cnp)
            else:
                print("Eroare: Datele introduse pentru client sunt invalide.")

        elif choice == "6":
            client_id = int(input("Introduceți ID-ul clientului de șters: "))
            inchiriere_service.sterge_client(client_id)

        elif choice == "7":
            client_id = int(input("Introduceți ID-ul clientului de modificat: "))
            nume = input("Introduceți noul nume: ")
            cnp = input("Introduceți noul CNP: ")
            inchiriere_service.modifica_client(client_id, nume, cnp)

        elif choice == "8":
            nume = input("Introduceți numele clientului de căutat: ")
            rezultate = inchiriere_service.cauta_client(nume)
            for client in rezultate:
                print(f"ID: {client.id}, Nume: {client.nume}, CNP: {client.cnp}")

        elif choice == "9":
            client_id = int(input("Introduceți ID-ul clientului care închiriază: "))
            film_id = int(input("Introduceți ID-ul filmului de închiriat: "))
            inchiriere_service.inchiriere_film(client_id, film_id)

        elif choice == "10":
            client_id = int(input("Introduceți ID-ul clientului care returnează: "))
            film_id = int(input("Introduceți ID-ul filmului de returnat: "))
            inchiriere_service.returnare_film(client_id, film_id)

        elif choice == "11":
            print("Clienți cu filme închiriate:")
            for client in inchiriere_service.clienti_cu_filme_inchiriate():
                print(f"{client.nume}: {', '.join([film.titlu for film in client.filme_inchiriate])}")

        elif choice == "12":
            print("Cele mai închiriate filme:")
            for film, count in inchiriere_service.cele_mai_inchiriate_filme():
                print(f"{film.titlu}: Inchiriat de {count} ori")

        elif choice == "13":
            print("Primii 30% clienți cu cele mai multe filme închiriate:")
            for nume, count in inchiriere_service.primii_30_procent_clienti():
                print(f"{nume}: {count} filme închiriate")

        elif choice == "0":
            inchiriere_service.afiseaza_toate_filmele()
            inchiriere_service.afiseaza_toti_clientii()

        elif choice == "x":
            break

        else:
            print("Opțiune invalidă. Vă rugăm să alegeți din nou.")

if __name__ == "__main__":
    main()
