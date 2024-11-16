from Repository.rent_repository import RentRepository
from Domain.domain import RentDTO

class RentService:
    def __init__(self, repo):
        """
        Iinitializam clasa de service
        repo - repository-ul care stocheaza valorile
        """
        self.__repo = repo

    def rented_films_by_client_srv(self, id):
        """
        Returneaza lista cu id-urile filmelor inchiriate de un client
        id - id-ul clientului
        type: integer
        """
        return self.__repo.rented_films_by_client_repo(id)

    def add_rented_film_srv(self, id_film, id_client):
        """
        Se apeleaza operatia de inchiriere
        id_film - id-ul filmului
        id_client - id-ul clientului
        type: integer
        """
        rent_dto = RentDTO(id_film, id_client)
        self.__repo.add_rented_film_repo(rent_dto)

    def return_film_srv(self, id_film, id_client):
        """
        Elimina un film returnat din lista filmelor inchiriate
        id_film - id-ul filmului
        id_client - id-ul clientului
        type: integer
        """
        rent_dto = RentDTO(id_film, id_client)
        self.__repo.return_film_repo(rent_dto)

    def rented_films_frequency_srv(self):
        """
        Returneaza o lista cu numarul de inchirieri a filmelor
        """
        return self.__repo.rented_films_frequency_repo()

    def clients_frequency_srv(self):
        """
        Returneaza o lista cu numarul filmelor inchiriate de fiecare client
        """
        return self.__repo.clients_frequency_repo()