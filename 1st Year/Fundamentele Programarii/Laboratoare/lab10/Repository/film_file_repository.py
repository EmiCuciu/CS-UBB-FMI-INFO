from Domain.domain import films


class FilmFileRepository:
    def __init__(self, filename):
        self.__film_list = []
        self.__file_name = filename
        self.load_from_file()

#   def load_from_file(self):
#       """
#       Stocheaza datele in memorie din fisierul dat
#       """
#       try:
#           with open(self.__file_name, "r") as file:
#               for line in file:
#                   contents = line.strip().split(",")
#                   id = int(contents[0])
#                   title = contents[1]
#                   description = contents[2]
#                   genre = contents[3]
#                   self.__film_list.append(films(id, title, description, genre))
#       except FileNotFoundError as nf:
#           print(nf)

#   def write_to_file(self):
#       """
#       Scrie in fisier datele stocate in memorie
#       """
#       with open(self.__file_name, "w") as file:
#           for element in self.get_all_films_repo():
#               film_str = f"{element.get_id()},{element.get_title()},{element.get_description()},{element.get_genre()}"
#               file.write(film_str + "\n")

    def load_from_file(self):
        """
        Stocheaza datele in memorie din fisierul dat
        """
        try:
            with open(self.__file_name, "r") as file:
                for line in file:
                    contents = line.strip().split(",")
                    id = int(contents[0])
                    title = contents[1]
                    description = contents[2]
                    genre = contents[3]
                    self.__film_list.append(films(id, title, description, genre))
        except FileNotFoundError as nf:
            print(nf)

    def write_to_file(self, output_file):
        """
        Scrie in fisier datele stocate in memorie
        """
        with open(output_file, "w") as file:
            for element in self.get_all_films_repo():
                file.write(f"{element.get_id()}\n")
                file.write(f"{element.get_title()}\n")
                file.write(f"{element.get_description()}\n")
                file.write(f"{element.get_genre()}\n\n")


    def get_all_films_repo(self):
        """
        Returneaza lista cu filme din memorie
        """
        return self.__film_list

    def clear(self):
        """
        Curata fisierul
        """
        self.__film_list = []
        self.write_to_file(output_file="fisier_filme_nou.txt")

    def size(self):
        """
        Returneaza lungimea listei
        """
        return len(self.__film_list)

    def add_film_repo(self, new_film):
        self.__film_list.append(new_film)
        self.write_to_file(output_file="fisier_filme_nou.txt")

    def find_film_repo(self, id):
        for fl in self.__film_list:
            if fl.get_id() == id:
                return fl

    def delete_film_repo(self, id):
        fl = self.find_film_repo(id)
        self.__film_list.remove(fl)
        self.write_to_file(output_file="fisier_filme_nou.txt")

    def verify_existent_id_srv(self, id):
        """
        Verifica daca exista un film cu id-ul dat
        """
        for fl in self.__film_list:
            if fl.get_id() == id:
                return True
        return False

    def modify_film_repo(self, id, new_title, new_description, new_genre):
        """
        Modifica un film dupa id
        """
        fl = self.find_film_repo(id)
        fl.set_title(new_title)
        fl.set_description(new_description)
        fl.set_genre(new_genre)
        self.write_to_file(output_file="fisier_filme_nou.txt")

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
            raise ValueError("Nu exista film cu acest id")

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
                raise KeyError("Exista deja film cu acest id")


def test_add_film_repo():
    repo = FilmFileRepository("../film_file.txt")
    repo.clear()
    repo.add_film_repo(films(1, "Nasul", "mafia siciliana", "Actiune"))
    assert repo.size() == 1


test_add_film_repo()

