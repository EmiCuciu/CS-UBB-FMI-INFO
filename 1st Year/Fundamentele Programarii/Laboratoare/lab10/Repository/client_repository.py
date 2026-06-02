from Domain.domain import clients


class ClientRepository:
    def __init__(self):
        """
        Initializam lista de clienti
        """
        self.__client_list = [
            clients(1, "Emi", 5031030330525),
            clients(2, "Ion", 5021248756213),
            clients(3, "Andra", 6487213554312),
            clients(4, "Vasile", 5031489775432),
            clients(5, "Petru", 5047954331245)
        ]

    def add_client_repo(self, new_client):
        """
        Adauga un client in memorie
        new_client - client nou
        type: clients
        """
        self.__client_list.append(new_client)

    def delete_client_repo(self, id):
        """
        Sterge din lista un client
        id - id-ul clientului care urmeaza sa fie stears
        type: integer
        """
        for cl in self.__client_list:
            if cl.get_id() == id:
                self.__client_list.remove(cl)

    def modify_client_repo(self, id, new_name, new_cnp):
        """
        Modifica o carte dupa id
        """
        for cl in self.__client_list:
            if cl.get_id() == id:
                cl.set_name(new_name)
                cl.set_cnp(new_cnp)

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