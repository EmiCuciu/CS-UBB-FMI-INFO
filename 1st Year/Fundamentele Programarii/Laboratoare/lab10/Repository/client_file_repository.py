from Domain.domain import clients


class ClientFileRepository:
    def __init__(self, filename):
        self.__client_list = []
        self.__file_name = filename
        self.load_from_file()

    def load_from_file(self):
        """
        Stocheaza datele in memorie din fisierul dat
        """
        try:
            with open(self.__file_name, "r") as file:
                for line in file:
                    contents = line.strip().split(",")
                    id = int(contents[0])
                    name = contents[1]
                    CNP = contents[2]
                    self.__client_list.append(clients(id, name, CNP))
        except FileNotFoundError as nf:
            print(nf)

    def write_to_file(self):
        """
        Scrie in fisier datele stocate in memorie
        """
        with open(self.__file_name, "w") as file:
            for element in self.get_all_clients_repo():
                client_str = f"{element.get_id()},{element.get_name()},{element.get_cnp()}"
                file.write(client_str + "\n")

    def get_all_clients_repo(self):
        """
        Returneaza lista cu clienti din memorie
        """
        return self.__client_list

    def clear(self):
        """
        Curata fisierul
        """
        self.__client_list = []
        self.write_to_file()

    def size(self):
        """
        Returneaza lungimea listei
        """
        return len(self.__client_list)
    def add_client_repo(self, new_client):
        """
        Adauga un client in memorie
        new_client - client nou
        type: clients
        """
        self.__client_list.append(new_client)
        self.write_to_file()

    def delete_client_repo(self, id):
        """
        Sterge din lista un client
        id - id-ul clientului care urmeaza sa fie stears
        type: integer
        """
        cl = self.find_client_repo(id)
        self.__client_list.remove(cl)
        self.write_to_file()

    def modify_client_repo(self, id, new_name, new_cnp):
        """
        Modifica un film dupa id
        """
        cl = self.find_client_repo(id)
        cl.set_name(new_name)
        cl.set_cnp(new_cnp)
        self.write_to_file()

    def verify_not_existent_id_repo(self, id):
        """
        Verifica da NU exista un client cu acest id
        """
        k = 0
        for cl in self.__client_list:
            if cl.get_id() == id:
                k += 1
                break
        if k == 0:
            raise ValueError("Nu exista client cu acest id")

    def verify_existent_id_srv(self, id):
        """
        Verifica daca exista un client cu id-ul dat
        """
        for cl in self.__client_list:
            if cl.get_id() == id:
                return True
        return False

    def verify_existent_id_repo(self, id):
        """
        Verifica daca exista un client cu acest id
        """
        for cl in self.__client_list:
            if cl.get_id() == id:
                raise KeyError("Exista deja client cu acest id")

    def find_client_repo(self, id):
        """
        Cauta un client dupa id in lista clientilor si il returneaza
        id - id-ul clientului
        type: integer
        """
        for cl in self.__client_list:
            if cl.get_id() == id:
                return cl

    def get_all_clients_repo(self):
        """
        Returneaza lista cu clientii
        """
        return self.__client_list