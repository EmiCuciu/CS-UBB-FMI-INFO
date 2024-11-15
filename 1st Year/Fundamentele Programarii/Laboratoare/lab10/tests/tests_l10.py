import unittest
from Domain.domain import clients,films
from Repository.film_file_repository import FilmFileRepository
from Repository.client_file_repository import ClientFileRepository
from Repository.rent_repository import RentRepository
from Service.film_service import FilmService
from Service.client_service import ClientService
from Service.rent_service import RentService
from Utils.validate import (
    validate_titlu,
    validate_descriere,
    validate_gen,
    validate_cnp,
    validate_nume
)

class TestFilmService(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_add_film(self):
        repo = FilmFileRepository("test_films.txt")
        repo.clear()

        self.assertEqual(repo.get_all_films_repo(), [])

        film = films(1, "Nasul", "mafia siciliana", "Actiune")
        repo.add_film_repo(film)
        self.assertEqual(len(repo.get_all_films_repo()), 1)

    def test_delete_film(self):
        repo = FilmFileRepository("test_films.txt")
        repo.clear()

        film = films(1, "Nasul", "mafia siciliana", "Actiune")
        repo.add_film_repo(film)
        self.assertEqual(len(repo.get_all_films_repo()), 1)

        repo.delete_film_repo(1)
        self.assertEqual(len(repo.get_all_films_repo()), 0)

    def test_add_film_service(self):
        repo = FilmFileRepository("test_films.txt")
        repo.clear()
        service = FilmService(repo)

        self.assertEqual(service.get_all_films_srv(), [])

        service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
        self.assertEqual(len(service.get_all_films_srv()), 1)

    def test_delete_film_service(self):
        repo = FilmFileRepository("test_films.txt")
        repo.clear()
        service = FilmService(repo)

        service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
        self.assertEqual(len(service.get_all_films_srv()), 1)

        service.delete_film_srv(1)
        self.assertEqual(len(service.get_all_films_srv()), 0)

    def test_modify_film_service(self):
        repo = FilmFileRepository("test_films.txt")
        repo.clear()
        service = FilmService(repo)

        service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")

        service.modify_film_srv(1, "Nasul 2", "mai multa mafia", "Actiune")
        modified_film = service.find_film_srv(1)

        self.assertEqual(modified_film.get_title(), "Nasul 2")
        self.assertEqual(modified_film.get_description(), "mai multa mafia")


class TestClientService(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_add_client(self):
        repo = ClientFileRepository("test_clients.txt")
        repo.clear()

        self.assertEqual(repo.get_all_clients_repo(), [])

        client = clients(1, "Emi", "5031030330525")
        repo.add_client_repo(client)
        self.assertEqual(len(repo.get_all_clients_repo()), 1)

    def test_delete_client(self):
        repo = ClientFileRepository("test_clients.txt")
        repo.clear()

        client = clients(1, "Emi", "5031030330525")
        repo.add_client_repo(client)
        self.assertEqual(len(repo.get_all_clients_repo()), 1)

        repo.delete_client_repo(1)
        self.assertEqual(len(repo.get_all_clients_repo()), 0)

    def test_add_client_service(self):
        repo = ClientFileRepository("test_clients.txt")
        repo.clear()
        service = ClientService(repo)

        self.assertEqual(service.get_all_clients_srv(), [])

        service.add_client_srv(1, "Emi", "5031030330525")
        self.assertEqual(len(service.get_all_clients_srv()), 1)

    def test_delete_client_service(self):
        repo = ClientFileRepository("test_clients.txt")
        repo.clear()
        service = ClientService(repo)

        service.add_client_srv(1, "Emi", "5031030330525")
        self.assertEqual(len(service.get_all_clients_srv()), 1)

        service.delete_client_srv(1)
        self.assertEqual(len(service.get_all_clients_srv()), 0)



class TestValidationMethods(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def test_validate_titlu_white_box(self):
        self.assertTrue(validate_titlu("Nasul"))
        self.assertFalse(validate_titlu(""))  # Titlu vid
        self.assertFalse(validate_titlu("   "))  # Titlu format doar din spații

    def test_validate_descriere_white_box(self):
        self.assertTrue(validate_descriere("mafia siciliana"))
        self.assertFalse(validate_descriere(""))  # Descriere vidă
        self.assertFalse(validate_descriere("   "))  # Descriere formată doar din spații

    def test_validate_gen_white_box(self):
        self.assertTrue(validate_gen("Actiune"))
        self.assertFalse(validate_gen(""))  # Gen vid
        self.assertFalse(validate_gen("   "))  # Gen format doar din spații

    def test_validate_cnp_white_box(self):
        self.assertTrue(validate_cnp("5031030330525"))
        self.assertFalse(validate_cnp("12345"))  # CNP prea scurt
        self.assertFalse(validate_cnp("abcde"))  # CNP invalid

    def test_validate_nume_white_box(self):
        self.assertTrue(validate_nume("Emi"))
        self.assertFalse(validate_nume(""))  # Nume vid
        self.assertFalse(validate_nume("   "))  # Nume format doar din spații


if __name__ == '__main__':
    unittest.main()
