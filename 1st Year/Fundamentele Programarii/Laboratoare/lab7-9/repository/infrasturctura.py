from domain.entitati import Film,Client,Inchiriere

class InchiriereService:
    def __init__(self):
        # Inițializare serviciu de închiriere
        self.filme = []  # Lista pentru a stoca filmele
        self.clienti = []  # Lista pentru a stoca clienții
        self.inchirieri = []  # Lista pentru a stoca închirierile

    def adauga_film(self, titlu, descriere, gen):
        # Adaugă un film nou în listă
        film_id = len(self.filme) + 1
        film = Film(film_id, titlu, descriere, gen)
        self.filme.append(film)
        return film

    def sterge_film(self, film_id):
        # Sterge un film din listă după ID
        film = self.get_film_by_id(film_id)
        if film:
            self.filme.remove(film)
            return True
        return False

    def modifica_film(self, film_id, titlu, descriere, gen):
        # Modifică detaliile unui film existent
        film = self.get_film_by_id(film_id)
        if film:
            film.titlu = titlu
            film.descriere = descriere
            film.gen = gen
            return True
        return False

    def cauta_film(self, titlu):
        # Caută filme după titlu
        return [film for film in self.filme if titlu.lower() in film.titlu.lower()]

    def adauga_client(self, nume, cnp):
        # Adaugă un client nou în listă
        client_id = len(self.clienti) + 1
        client = Client(client_id, nume, cnp)
        self.clienti.append(client)
        return client

    def sterge_client(self, client_id):
        # Sterge un client din listă după ID
        client = self.get_client_by_id(client_id)
        if client:
            self.clienti.remove(client)
            return True
        return False

    def modifica_client(self, client_id, nume, cnp):
        # Modifică detaliile unui client existent
        client = self.get_client_by_id(client_id)
        if client:
            client.nume = nume
            client.cnp = cnp
            return True
        return False

    def cauta_client(self, nume):
        # Caută clienți după nume
        return [client for client in self.clienti if nume.lower() in client.nume.lower()]

    def inchiriere_film(self, client_id, film_id):
        # Închiriază un film către un client
        client = self.get_client_by_id(client_id)
        film = self.get_film_by_id(film_id)

        if client and film:
            inchiriere = Inchiriere(client, film)
            self.inchirieri.append(inchiriere)
            client.filme_inchiriate.append(film)
            return True
        return False

    def returnare_film(self, client_id, film_id):
        # Returnează un film în lista de filme disponibile
        client = self.get_client_by_id(client_id)
        film = self.get_film_by_id(film_id)

        if client and film and film.inchiriat:
            inchiriere = next((i for i in self.inchirieri if i.client == client and i.film == film), None)
            if inchiriere:
                self.inchirieri.remove(inchiriere)
                client.filme_inchiriate.remove(film)
                film.inchiriat = False
                return True
        return False

    def clienti_cu_filme_inchiriate(self):
        # Returnează lista de clienți cu filme închiriate, ordonați după nume și număr de filme închiriate
        return sorted(self.clienti, key=lambda x: (x.nume, len(x.filme_inchiriate)))

    def cele_mai_inchiriate_filme(self):
        # Returnează lista de cele mai închiriate filme, ordonate după numărul de închirieri
        filme_inchiriate = [inchiriere.film for inchiriere in self.inchirieri]
        filme_count = {film: filme_inchiriate.count(film) for film in set(filme_inchiriate)}
        return sorted(filme_count.items(), key=lambda x: x[1], reverse=True)

    def primii_30_procent_clienti(self):
        # Returnează lista primilor 30% clienți cu cele mai multe filme închiriate
        numar_clienti = int(0.3 * len(self.clienti))
        clienti_sortati = sorted(self.clienti, key=lambda x: len(x.filme_inchiriate), reverse=True)
        return [(client.nume, len(client.filme_inchiriate)) for client in clienti_sortati[:numar_clienti]]

    def get_film_by_id(self, film_id):
        # Returnează un film după ID
        return next((film for film in self.filme if film.id == film_id), None)

    def get_client_by_id(self, client_id):
        # Returnează un client după ID
        return next((client for client in self.clienti if client.id == client_id), None)

    def afiseaza_toate_filmele(self):
        # Afișează toate filmele cu detaliile lor
        print("Lista de filme:")
        for film in self.filme:
            print(f"ID: {film.id}, Titlu: {film.titlu}, "
                  f"Descriere: {film.descriere},"
                  f" Gen: {film.gen}")

    def afiseaza_toti_clientii(self):
        # Afișează toți clienții cu detaliile lor
        print("Lista de clienți:")
        for client in self.clienti:
            print(f"ID: {client.id}, Nume: {client.nume}, CNP: {client.cnp}")

    def adauga_din_fisier(self, nume_fisier):
        with open(nume_fisier, 'r') as file:
            for line in file:
                data = line.strip().split(',')
                if len(data) == 3:
                    # Presupunem că sunt date pentru un film
                    self.adauga_film(*data)
                elif len(data) == 2:
                    # Presupunem că sunt date pentru un client
                    self.adauga_client(*data)