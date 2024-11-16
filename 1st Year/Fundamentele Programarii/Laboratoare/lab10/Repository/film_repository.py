from Domain.domain import films

class FilmRepository:
    def __init__(self):
        """
        Initializam lista de filme
        """
        self.__film_list = [
            # films(1,"Nasul", "mafia siciliana","Actiune"),
            # films(2,"Aliens","extraterestri","SF"),
            # films(3,"Titanic","poveste de dragoste","Romantic")
        ]

    def add_film_repo(self, new_film):
        self.__film_list.append(new_film)

    def delete_film_repo(self, film):
        fl = self.find_film_repo(id)
        self.__film_list.remove(fl)

    def modify_film_repo(self, id, new_title, new_description, new_genre):
        fl = self.find_film_repo(id)
        fl.set_title(new_title)
        fl.set_description(new_description)
        fl.set_gemre(new_genre)

    def verify_not_existent_id_repo(self, id):
        """
        Verifica daca NU exista un film cu id-ul dat
        """
        k = 0
        for fl in self.__film_list:
            if fl.get_id() == id:
                k += 1
                break
        if k == 0:
            raise ValueError("Nu exista carte cu acest id")

    def verify_existent_id_random_repo(self, id):
        """
        Verifica daca exista un film cu id-ul dat
        """
        for fl in self.__film_list:
            if fl.get_id() == id:
                return 1

    def verify_existent_id_repo(self, id):
        """
        Verifica daca exista un film cu id-ul dat
        """
        for fl in self.__film_list:
            if fl.get_id() == id:
                raise KeyError("Exista deja carte cu acest id")

    def find_film_repo(self, id):
        """
        Cauta un film dupa id in lista filmelor si o returneaza
        id - id-ul filmului
        type: integer
        """
        for fl in self.__film_list:
            if fl.get_id() == id:
                return fl

    def size(self):
        return len(self.__film_list)

    def get_all_films_repo(self):
        return self.__film_list