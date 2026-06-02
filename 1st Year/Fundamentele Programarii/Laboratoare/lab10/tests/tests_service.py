from Service.film_service import FilmService
from Service.client_service import ClientService
from Service.rent_service import RentService
from Repository.film_file_repository import FilmFileRepository
from Repository.client_file_repository import ClientFileRepository
from Repository.rent_repository import RentRepository


def test_add_film_service():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    assert service.get_all_films_srv() == []

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(service.get_all_films_srv()) == 1

def test_delete_film_service():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    assert len(service.get_all_films_srv()) == 1

    service.delete_film_srv(1)
    assert len(service.get_all_films_srv()) == 0

def test_modify_film_service():
    repo = FilmFileRepository("test_films.txt")
    repo.clear()
    service = FilmService(repo)

    service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")

    service.modify_film_srv(1, "Nasul 2", "mai multa mafia", "Actiune")
    modified_film = service.find_film_srv(1)

    assert modified_film.get_title() == "Nasul 2"
    assert modified_film.get_description() == "mai multa mafia"


def test_add_client_service():
    repo = ClientFileRepository("test_clients.txt")
    repo.clear()
    service = ClientService(repo)

    assert service.get_all_clients_srv() == []

    service.add_client_srv(1, "Emi", "5031030330525")
    assert len(service.get_all_clients_srv()) == 1

def test_delete_client_service():
    repo = ClientFileRepository("test_clients.txt")
    repo.clear()
    service = ClientService(repo)

    service.add_client_srv(1, "Emi", "5031030330525")
    assert len(service.get_all_clients_srv()) == 1

    service.delete_client_srv(1)
    assert len(service.get_all_clients_srv()) == 0

def test_rent_and_return_service():
    film_repo = FilmFileRepository("test_films.txt")
    client_repo = ClientFileRepository("test_clients.txt")
    rent_repo = RentRepository()

    film_repo.clear()
    client_repo.clear()

    film_service = FilmService(film_repo)
    client_service = ClientService(client_repo)
    rent_service = RentService(rent_repo, film_service, client_service)

    film_service.add_film_srv(1, "Nasul", "mafia siciliana", "Actiune")
    client_service.add_client_srv(1, "Emi", "5031030330525")


    rented_films = rent_service.rented_films_by_client_srv(1)
    assert len(rented_films) == 1

    rent_service.return_film_srv(1, 1)

    rented_films_after_return = rent_service.rented_films_by_client_srv(1)
    assert len(rented_films_after_return) == 0
