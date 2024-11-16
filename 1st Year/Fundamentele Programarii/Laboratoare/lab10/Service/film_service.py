from Repository.film_repository import FilmRepository
from Domain.domain import films
from Utils.utils import *
from Utils.validate import validate_titlu,validate_descriere,validate_gen
from Domain.domain import films

class FilmService:
    def __init__(self, repo):
        self.__repo = repo

    def add_film_srv(self, id, title, description, genre):
        """
        Creeaza un film nou cu parametrii dati si il adauga in memorie
        """
        if validate_titlu(title) and validate_descriere(description) and validate_gen(genre):
            film = films(id, title, description, genre)
            self.__repo.add_film_repo(film)
            return True  # Adăugarea a avut succes
        else:
            return False  # Date invalide pentru film

    def add_random_film_srv(self, id, size):
        """
        Genereaza filme random
        """
        while size:
            while self.__repo.verify_existent_id_random_repo(id) == 1:
                id += 1
            title = random_string(5)
            description = random_string(7)
            genre = random_string(5)
            new_film = films(int(id), title, description, genre)
            self.__repo.add_film_repo(new_film)
            size -= 1
            id += 1

    def delete_film_srv(self, id):
        """
        Sterge un film dupa id
        """
        self.__repo.delete_film_repo(id)

    def modify_film_srv(self, id, new_title, new_description, new_genre):
        """
        Modifica un film dupa id
        """
        self.__repo.modify_film_repo(id, new_title, new_description, new_genre)

    def verify_not_existent_id_srv(self, id):
        """
        Verifica in repository daca NU exista filmul cu id-ul dat
        """
        self.__repo.verify_not_existent_id_repo(id)

    def verify_existent_id_srv(self, id):
        """
        Verifica in repository daca exista filmul cu id-ul dat
        """
        self.__repo.verify_existent_id_repo(id)

    def find_film_srv(self, id):
        """
        Cauta un film dupa id in lista fiomelor si o returneaza
        id - id-ul filmului
        type: integer
        """
        return self.__repo.find_film_repo(id)

    def get_all_films_srv(self):
        """
        Returneaza lista de filme
        """
        return self.__repo.get_all_films_repo()

