from Repository.client_repository import ClientRepository
from Domain.domain import clients
from Utils.validate import validate_nume, validate_cnp


class ClientService:
    def __init__(self, repo):
        self.__repo = repo

    def add_client_srv(self, id, name, cnp):
        """
        Adauga un client nou cu parametrii dati
        """
        if validate_nume(name) and validate_cnp(cnp):
            client = clients(id, name, cnp)
            self.__repo.add_client_repo(client)
            return True  # Adăugarea a avut succes
        else:
            return False  # Date invalide pentru client

    def delete_client_srv(self, id):
        """
        Sterge un client dupa id
        """
        self.__repo.delete_client_repo(id)

    def modify_client_srv(self, id, new_name, new_cnp):
        """
        Modifica un client dupa id
        """
        self.__repo.modify_client_repo(id, new_name, new_cnp)

    def verify_not_existent_id_srv(self, id):
        """
        Verifica in repository daca NU exista clientul cu id-ul dat
        """
        self.__repo.verify_not_existent_id_repo(id)

    def verify_existent_id_srv(self, id):
        """
        Verifica in repository daca exista clientul cu id-ul dat
        """
        self.__repo.verify_existent_id_repo(id)

    def find_client_srv(self, id):
        """
        Cauta un client dupa id in lista clientilor si il tipareste
        id - id-ul clientului
        type: integer
        """
        return self.__repo.find_client_repo(id)

    def get_all_clients_srv(self):
        """
        Returneaza lista cu clientii
        """
        return self.__repo.get_all_clients_repo()


