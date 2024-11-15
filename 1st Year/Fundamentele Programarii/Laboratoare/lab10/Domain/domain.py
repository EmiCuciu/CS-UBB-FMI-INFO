class films:
    def __init__(self, id, title, description, genre):
        self.__id = id
        self.__title = title
        self.__description = description
        self.__genre = genre

    """
    Functii get/set
    """

    def get_id(self):
        return int(self.__id)

    def get_title(self):
        return self.__title

    def get_description(self):
        return self.__description

    def get_genre(self):
        return self.__genre

    def set_title(self, value):
        self.__title = value

    def set_description(self, value):
        self.__description = value
        pass

    def set_genre(self, value):
        self.__genre = value

    def __eq__(self, __value):
        if not isinstance(__value, films):
            return False
        return self.__id == __value.get_id()

    def __str__(self):
        return f"ID-ul:{self.__id}, Titlu: {self.__title}, Descriere: {self.__description}, Gen: {self.__genre}"


class clients:
    def __init__(self, id, name, cnp):
        """
        Se creeaza clasa clientilor
        id - id-ul clientului
        type: integer
        name - numele clientului
        type: string
        cnp - codul numeric personal al clientului
        type: integer
        """
        self.__id = id
        self.__name = name
        self.__cnp = cnp

    """
    Functii get/set
    """

    def get_id(self):
        return int(self.__id)

    def get_name(self):
        return self.__name

    def get_cnp(self):
        return self.__cnp

    def set_name(self, value):
        self.__name = value

    def set_cnp(self, value):
        self.__cnp = value

    def __eq__(self, __value):
        if not isinstance(__value, clients):
            return False
        return self.__id == __value.get_id()

    def __str__(self):
        return f"ID:{self.__id}, Nume: {self.__name}, CNP:{self.__cnp}"






class RentDTO:
    def __init__(self, film_id, client_id):
        self.__film_id = film_id
        self.__client_id = client_id

    def get_film_id(self):
        return self.__film_id

    def get_client_id(self):
        return self.__client_id




def test_create_film():
    film = films(1, "Nasul", "film cu mafioti", "Actiune")
    assert film.get_title() == "Nasul"
    assert film.get_genre() == "Actiune"
    assert film.get_id() == 1


def test_create_client():
    client = clients(1, "Ion", 5021504125886)
    assert client.get_id() == 1
    assert client.get_name() == "Ion"
    assert client.get_cnp() == 5021504125886


test_create_film()
test_create_client()