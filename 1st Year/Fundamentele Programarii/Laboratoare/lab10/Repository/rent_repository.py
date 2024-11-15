from Domain.domain import films, clients,RentDTO
from Repository.film_repository import FilmRepository
from Repository.client_repository import ClientRepository


class RentRepository:
    def __init__(self):
        """
        Se initializeaza lista unde se vor stoca filmele inchiriate
        """
        self.__rent_list = []
        self.__repo = FilmRepository


    def rented_films_by_client_repo(self, id):
        """
        Returneaza o lista cu id-ul filmelor inchiriate de un client
        """
        rented_films = []
        for item in self.__rent_list:
            if item.get_client_id == id:
                rented_films.append(item.film_id)
        return rented_films

    def add_rented_film_repo(self, rent_dto):
        """
        Se adauga in lista filmelor inchiriate id-ul filmului si al clientului
        """
        self.__rent_list.append(rent_dto)

    def return_film_repo(self, rent_dto):
        """
        Elimina o carte returnata din lista cartilor inchiriate

        """
        if rent_dto in self.__rent_list:
            self.__rent_list.remove(rent_dto)
        else:
            raise ValueError("Inchirierea nu a fost inregistrata")

    def clear(self):
        """
        Curata fisierul
        """
        self.__rent_list = []


    def size(self):
        """
        Returneaza lungimea listei
        """
        return len(self.__rent_list)

    def rented_films_frequency_repo(self):
        """
        Returneaza o lista cu numarul de inchirieri a filmelor
        """
        freq_list = {}
        for rent_dto in self.__rent_list:
            film_id = rent_dto.get_film_id()
            if film_id in freq_list:
                freq_list[film_id] += 1
            else:
                freq_list[film_id] = 1
        return freq_list

    def clients_frequency_repo(self):
        """
        Returneaza o lista cu numarul filmelor inchiriate de fiecare client
        """
        freq_list = [0] * 100
        for item in self.__rent_list:
            freq_list[item.get_client_id()] += 1
        return freq_list
